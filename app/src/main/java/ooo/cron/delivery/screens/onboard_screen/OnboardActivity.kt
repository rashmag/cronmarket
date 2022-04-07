package ooo.cron.delivery.screens.onboard_screen

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_onboard.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import javax.inject.Inject

class OnboardActivity : BaseActivity(),OnboardContact.View {

    @Inject
    protected lateinit var presenter: OnboardContact.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {

        App.appComponent.onboardComponentBuilder()
            .inflater(layoutInflater)
            .build()
            .inject(this)

        presenter.attachView(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        btn_next_onboard.setOnClickListener {
            clickNext()
        }
        setStatusBarColor()
        setViewPager()
    }

    private fun clickNext() {
        if (view_pager_onboard.currentItem != 3) {
            view_pager_onboard.currentItem += 1
        } else {
            startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
            finish()
        }
    }

    private fun setStatusBarColor() {
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.onboardBackground)
    }

    private fun setViewPager() {
        view_pager_onboard.adapter = ViewPagerAdapter(supportFragmentManager)
        view_pager_onboard.currentItem = 0

        view_pager_onboard.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                page_indicator_view.setSelected(position)
                when (position) {
                    ONE_FRAGMENT -> btn_next_onboard.text = "Далее"
                    TWO_FRAGMENT -> btn_next_onboard.text = "Продолжайте"
                    THREE_FRAGMENT -> btn_next_onboard.text = "Что ещё?"
                    FOUR_FRAGMENT -> {
                        btn_next_onboard.text = "Отлично!"
                        presenter.sendMessageOnboardCompleted()
                    }
                    else -> btn_next_onboard.text = "Далее"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    companion object {
        const val ONE_FRAGMENT = 0
        const val TWO_FRAGMENT = 1
        const val THREE_FRAGMENT = 2
        const val FOUR_FRAGMENT = 3
    }
}