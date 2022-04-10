package ooo.cron.delivery.screens.onboard_screen.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_onboard.*
import kotlinx.android.synthetic.main.fragment_order_history_detail.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.onboard_screen.adapter.ViewPagerAdapter
import ooo.cron.delivery.data.network.models.OnboardingModel
import javax.inject.Inject
import javax.inject.Named

class OnboardActivity : BaseActivity(){

    @Inject
    @Named("Onboard")
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<OnboardViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {

        App.appComponent.onboardComponentBuilder()
            .inflater(layoutInflater)
            .build()
            .inject(this)

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
            viewModel.sendMessageInAnalytics(getString(R.string.completed_onboard))
            startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
            finish()
        }
    }

    private fun setStatusBarColor() {
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.onboardBackground)
    }

    private fun setViewPager() {
        val viewPagerAdapter = ViewPagerAdapter()
        view_pager_onboard.adapter = viewPagerAdapter
        viewPagerAdapter.setData(setDataInOnboardModel())
        view_pager_onboard.currentItem = 0

        view_pager_onboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                page_indicator_view.setSelected(position)
                when (position) {
                    ONE_FRAGMENT -> btn_next_onboard.text = getString(R.string.onboard_one_slide)
                    TWO_FRAGMENT -> btn_next_onboard.text = getString(R.string.onboard_two_slide)
                    THREE_FRAGMENT -> btn_next_onboard.text = getString(R.string.onboard_three_slide)
                    FOUR_FRAGMENT -> btn_next_onboard.text = getString(R.string.onboard_four_slide)
                    else -> btn_next_onboard.text = getString(R.string.onboard_one_slide)
                }
            }
        })
    }

    private fun setDataInOnboardModel(): ArrayList<OnboardingModel> {
        val listOnboard = arrayListOf(
            OnboardingModel(
                back = R.drawable.back_onboard_one,
                image = R.drawable.delivery_onboard,
                title = resources.getString(R.string.title_delivery_one),
                subTitle = resources.getString(R.string.sub_title_delivery_one)
            ),
            OnboardingModel(
                back = R.drawable.back_onboard_two,
                image = R.drawable.status_onboard,
                title = resources.getString(R.string.status_delivery),
                subTitle = resources.getString(R.string.sub_title_status_delivery)
            ),
            OnboardingModel(
                back = R.drawable.back_onboard_three,
                image = R.drawable.pay_onboard,
                title = resources.getString(R.string.pay_title_onboard),
                subTitle = resources.getString(R.string.sub_title_pay_onboard)
            ),
            OnboardingModel(
                back = R.drawable.back_onboard_four,
                image = R.drawable.best_institution_onboard,
                title = resources.getString(R.string.institution_title_onboard),
                subTitle = resources.getString(R.string.institution_description_onboard)
            )
        )
        return listOnboard
    }

    companion object {
        const val ONE_FRAGMENT = 0
        const val TWO_FRAGMENT = 1
        const val THREE_FRAGMENT = 2
        const val FOUR_FRAGMENT = 3
    }
}