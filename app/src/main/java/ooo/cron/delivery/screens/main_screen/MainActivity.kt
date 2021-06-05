package ooo.cron.delivery.screens.main_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.databinding.ActivityMainBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.about_service_screen.AboutServiceFragment
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryFragment
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
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

        configureNavigationDrawer()

        setContinueLastSessionClickListener()

        presenter.onCreateView()
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

    override fun showNotAuthorizedMessage() {
        Toast.makeText(
            this,
            getString(R.string.common_user_not_authorized), Toast.LENGTH_SHORT
        ).show()
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

    override fun showContinueLastSession() {
        binding.vgMainContinueLastSession.visibility = View.VISIBLE
    }

    override fun startMarketCategoryFragment(category: MarketCategory) {
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            MarketCategoryFragment().apply {
                arguments = marketCategoryArguments(category)

            }
        ).commit()
    }

    override fun startAboutServiceFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            AboutServiceFragment()
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
            //TODO()
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
                    binding.vgMainMenu.tvDrawerMenuItemAboutUs -> startAboutServiceFragment()
                }

                onClick(clickedView)
            }
        }
    }
}