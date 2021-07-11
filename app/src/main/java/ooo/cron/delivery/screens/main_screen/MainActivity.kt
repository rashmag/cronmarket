package ooo.cron.delivery.screens.main_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior
import com.google.android.material.tabs.TabLayout
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.data.network.models.Promotion
import ooo.cron.delivery.databinding.ActivityMainBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.about_service_screen.AboutServiceFragment
import ooo.cron.delivery.screens.contacts_screen.ContactsFragment
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.main_screen.special_offers_view.models.SlideModel
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryFragment
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
import ooo.cron.delivery.screens.vacancies_screen.VacanciesFragment
import ooo.cron.delivery.utils.dipToPixels
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var binding: ActivityMainBinding

    private var shouldLastBasketSessionBeVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureAppBar()

        configureNavigationDrawer()

        setContinueLastSessionClickListener()

        presenter.onCreateView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResumeView()
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

    override fun showNotAuthorizedMessage() {
        Toast.makeText(
            this,
            getString(R.string.common_user_not_authorized), Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLogOutDialog() {
        LogOutDialog(
            presenter::onLogOutApplied
        ).show(supportFragmentManager, LogOutDialog::class.simpleName)
    }

    override fun showMarketCategories(categories: List<MarketCategory>) {
        binding.tlMainMarketCategories.apply {
            clear()
            fillTabs(categories)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    presenter.onTabSelected(tab!!.position)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    Log.d(MainActivity::class.simpleName, "tab ${tab?.text} unselected")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    Log.d(MainActivity::class.simpleName, "tab ${tab?.text} reselected")
                }
            })
        }
    }

    override fun selectMarketCategory(position: Int) {
        binding.tlMainMarketCategories.getTabAt(position)?.let {
            if (!it.isSelected)
                it.select()
        }
    }

    override fun showAuthorizedUser(username: String) {
        binding.vgMainMenu.ivDrawerProfile.setImageResource(
            R.drawable.ic_drawable_authorized_profile
        )
        binding.vgMainMenu.tvDrawerProfile.text = username

        binding.vgMainMenu.tvDrawerProfileLogInOut.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_drawer_log_out, 0, 0, 0
        )

        binding.vgMainMenu.tvDrawerProfileLogInOut.text = getString(R.string.drawer_log_out)
    }

    override fun showUnauthorizedUser() {
        binding.vgMainMenu.ivDrawerProfile.setImageResource(
            R.drawable.ic_drawable_authorized_profile
        )
        binding.vgMainMenu.tvDrawerProfile.text =
            getString(R.string.drawer_profile_name_unauthorized)

        binding.vgMainMenu.tvDrawerProfileLogInOut.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_drawer_log_out, 0, 0, 0
        )
    }

    override fun shouldLastBasketSessionBeVisible(boolean: Boolean) {
        this.shouldLastBasketSessionBeVisible = boolean
    }

    override fun showContinueLastSession() {
        binding.vgMainContinueLastSession.visibility = View.VISIBLE
    }

    override fun hideContinueLastSession() {
        binding.vgMainContinueLastSession.visibility = View.GONE
    }

    override fun startMarketCategoryFragment(category: MarketCategory) {
        setToolbarTitleVisibility(false, null)
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            MarketCategoryFragment().apply {
                arguments = marketCategoryArguments(category)
            }
        ).commit()

        if (binding.vgMainContent.layoutParams is CoordinatorLayout.LayoutParams) {
            (binding.vgMainContent.layoutParams as CoordinatorLayout.LayoutParams).behavior =
                ScrollingViewBehavior()
            binding.vgMainContent.requestLayout()
        }

        presenter.onStartMarketCategory()
    }

    override fun startAboutServiceFragment() {
        setToolbarTitleVisibility(true, getString(R.string.drawer_menu_item_about_us))
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            AboutServiceFragment()
        ).commit()
    }

    override fun startContactsFragment() {
        setToolbarTitleVisibility(true, getString(R.string.contacts_title))
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            ContactsFragment()
        ).commit()
    }

    override fun startVacanciesFragment() {
        setToolbarTitleVisibility(true, getString(R.string.become_courier_title))
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            VacanciesFragment()
        ).commit()
    }

    override fun reopenMainScreen() {
        val intent = this.intent
        finish()
        startActivity(intent)
    }

    override fun navigateFirstAddressSelection() {
        startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
    }

    override fun navigateAddressSelection() {
        TODO("Not yet implemented")
    }

    override fun navigateLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun navigatePartnerScreen(partnerId: String) {
        startActivity(Intent(this, PartnersActivity::class.java)
            .apply {
                putExtra(PartnersActivity.EXTRA_PARTNER_ID, partnerId)
            }
        )
    }

    override fun showSpecialOffers(promotions: List<Promotion>) {
        binding.imageSlider.viewPager?.pageMargin = resources.dipToPixels(16f).toInt()
        binding.imageSlider.viewPager?.clipToPadding = false

        binding.imageSlider.viewPager?.setPadding(
            resources.dipToPixels(36f).toInt(),
            0,
            resources.dipToPixels(36f).toInt(),
            0
        )
        binding.imageSlider.setImageList(promotions.map { SlideModel(it.imgUri, "") })
    }

    override fun hideSpecialOffers() {
        //TODO
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

    private fun configureNavigationDrawer() {
        val drawerToggle = DuoDrawerToggle(
            this@MainActivity,
            binding.drawerMain,
            binding.tbMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerMain.setDrawerListener(drawerToggle)
        drawerToggle.syncState()

        setOnProfileClick()
        setOnLoginLogOut()

        configureMenuItemsClick {
            //TODO
        }

        binding.vgMainMenu.tvDrawerMenuItemShops.isSelected = true
    }

    private fun setOnProfileClick() {
        binding.vgMainMenu.ivDrawerProfile.setOnClickListener {
            presenter.onProfileClick()
        }
        binding.vgMainMenu.tvDrawerProfile.setOnClickListener {
            presenter.onProfileClick()
        }
    }

    private fun setOnLoginLogOut() {
        binding.vgMainMenu.tvDrawerProfileLogInOut.setOnClickListener {
            presenter.onLogInLogOutClick()
        }
    }

    private fun onAddressClick() = binding.tvMainUserAddress.setOnClickListener {
        presenter.onClickAddress()
    }

    private fun setContinueLastSessionClickListener() {
        binding.btnMainContinueLastSession.setOnClickListener {
            presenter.continueLastSessionCLick()
        }
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

    private fun configureMenuItemsClick(onClick: (menuItem: View) -> Unit) {
        val menuItems = listOf(
            binding.vgMainMenu.tvDrawerMenuItemShops,
            binding.vgMainMenu.tvDrawerMenuItemContacts,
            binding.vgMainMenu.tvDrawerMenuItemAboutUs,
            binding.vgMainMenu.tvDrawerMenuItemVacancies
        )

        menuItems.forEach {
            it.setOnClickListener { clickedView ->
                menuItems.forEach { item ->
                    item.isSelected = item == clickedView
                }

                when (clickedView) {
                    binding.vgMainMenu.tvDrawerMenuItemShops -> startMarketCategoryFragment(
                        presenter.getMarketCategory()
                    )
                    binding.vgMainMenu.tvDrawerMenuItemAboutUs -> startAboutServiceFragment()
                    binding.vgMainMenu.tvDrawerMenuItemContacts -> startContactsFragment()
                    binding.vgMainMenu.tvDrawerMenuItemVacancies -> startVacanciesFragment()
                }
                onClick(clickedView)
            }
        }
    }

    private fun setToolbarTitleVisibility(isVisible: Boolean, title: String?) {
        val visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.abMain.visibility = visibility
        //TODO("Change View.GONE on visibility")
        binding.ivMainSearch.visibility = View.GONE
        binding.tvMainUserAddress.visibility = visibility
        binding.vgMainContinueLastSession.visibility =
            if (shouldLastBasketSessionBeVisible && !isVisible)
                View.VISIBLE
            else
                View.GONE

        if (isVisible) {
            val params = binding.vgMainContent.layoutParams as CoordinatorLayout.LayoutParams
            params.behavior = null
            binding.vgMainContent.layoutParams = params
        }
        binding.tvMainTitle.text = title
        binding.tvMainTitle.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}