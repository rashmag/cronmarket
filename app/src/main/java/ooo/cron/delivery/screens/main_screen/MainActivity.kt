package ooo.cron.delivery.screens.main_screen

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import ooo.cron.delivery.App
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.databinding.ActivityMainBinding
import ooo.cron.delivery.screens.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showSavedAddress() {
        TODO("Not yet implemented")
    }

    override fun removeMarketCategoriesProgress() =
        binding.root.removeView(binding.vgMainMarketCategoriesProgress.root)

    override fun showMarketCategories(categories: List<MarketCategory>) {
        binding.tlMainMarketCategories.apply {
            clear()
            fillTabs(categories)
        }
    }

    private fun injectDependencies() =
        App.appComponent.mainComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)

    private fun TabLayout.clear() {
        if (tabCount > 0)
            removeAllTabs()
    }

    private fun TabLayout.fillTabs(categories: List<MarketCategory>) =
        categories.forEach { category ->
            addTab(newTab().setText(category.categoryName))
        }
}