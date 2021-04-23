package ooo.cron.delivery.screens.main_screen

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.databinding.ActivityMainBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryFragment
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

        configureAppBar()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showSavedAddress(address: String) {
        binding.tvMainUserAddress.setBackgroundResource(R.drawable.bg_main_address_correct)
        binding.tvMainUserAddress.text = address
    }

    override fun removeMarketCategoriesProgress() =
        binding.vgMainContent.removeView(binding.vgMainMarketCategoriesProgress.root)

    override fun showMarketCategories(categories: List<MarketCategory>) {
        binding.tlMainMarketCategories.apply {
            clear()
            fillTabs(categories)
        }
    }

    override fun startMarketCategoryFragment(category: MarketCategory) {
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            MarketCategoryFragment().apply {
                arguments = marketCategoryArguments(category)
            }
        ).commit()
    }

    override fun navigateFirstAddressSelection() {
        startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
    }

    override fun navigateAddressSelection() {
        TODO("Not yet implemented")
    }

    private fun injectDependencies() =
        App.appComponent.mainComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)

    private fun configureAppBar() {
        binding.abMain.outlineProvider = null
        onAddressClick()
    }

    private fun onAddressClick() = binding.tvMainUserAddress.setOnClickListener {
        presenter.onClickAddress()
    }

    private fun TabLayout.clear() {
        if (tabCount > 0)
            removeAllTabs()
    }

    private fun TabLayout.fillTabs(categories: List<MarketCategory>) =
        categories.forEach { category ->
            addTab(newTab().setText(category.categoryName))
        }

    private fun marketCategoryArguments(category: MarketCategory) = Bundle().apply {
        putString(
            MarketCategoryFragment.ARGUMENT_MARKET_CATEGORY_NAME,
            category.categoryName
        )
        putInt(
            MarketCategoryFragment.ARGUMENT_MARKET_CATEGORY_ID,
            category.id
        )
    }
}