package ooo.cron.delivery.screens.main_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.MarketCategory
import ooo.cron.delivery.data.network.models.Promotion
import ooo.cron.delivery.databinding.ActivityMainBinding
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.about_service_screen.AboutServiceFragment
import ooo.cron.delivery.screens.contacts_screen.ContactsFragment
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.main_screen.special_offers_view.models.SlideModel
import ooo.cron.delivery.screens.market_category_screen.MarketCategoryFragment
import ooo.cron.delivery.screens.partners_screen.PartnersActivity
import ooo.cron.delivery.screens.vacancies_screen.VacanciesFragment
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import javax.inject.Inject
import ooo.cron.delivery.screens.main_screen.special_offers_view.adapters.SliderAdapter
import ooo.cron.delivery.screens.order_history_screen.presentation.OrderHistoryFragment
import ooo.cron.delivery.utils.enums.ReturningToScreenEnum
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import java.util.*


class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var binding: ActivityMainBinding

    private var shouldLastBasketSessionBeVisible = false
    private var isFromPartnerScreen = false
    private val viewModel: MainViewModel by viewModels()

    private var swipeTimer: Timer? = null
    private var sliderPosition = 0

    private val partnerActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            isFromPartnerScreen = true
        }

    }

    private val sliderAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SliderAdapter {
            presenter.onPartnerClickedBaner(it.partnerId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureAppBar()
        onVisibleToolbar()
        configureNavigationDrawer()
        configureMarketCategoriesList()
        setContinueLastSessionClickListener()
        initSliderRecycler()

        presenter.onCreateScreen()


    }

    private fun onVisibleToolbar() {
        viewModel.visibleTB.observe(this, androidx.lifecycle.Observer {
            if (it)
                binding.tbMain.makeVisible()
            else
                binding.tbMain.makeGone()
        })

        viewModel.replaceFragment.observe(this, androidx.lifecycle.Observer {
            supportFragmentManager.beginTransaction().replace(
                R.id.container_main,
                OrderHistoryFragment()
            ).commit()
        })
    }

    private fun configureMarketCategoriesList() {
        binding.rvMainMarketCategory.setHasFixedSize(true)
        binding.rvMainMarketCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMainMarketCategory.adapter = MainMarketCategoryAdapter {
            presenter.onMarketCategoryClicked(it)
        }
    }

    override fun onResume() {
        super.onResume()
        setTimerForImageSlider()
        presenter.onResumeView(isFromPartnerScreen)
        isFromPartnerScreen = false
        checkUserLoggedStatus()
    }

    override fun onStop() {
        super.onStop()
        swipeTimer?.cancel()
        swipeTimer?.purge()
    }

    override fun onDestroy() {
        super.onDestroy()
        swipeTimer?.cancel()
        swipeTimer?.purge()
        presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == PartnersActivity.RESULT_CODE)
            isFromPartnerScreen = true

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showSavedAddress(address: String) {
        with(binding.tvMainUserAddress) {
            if (address.isNotEmpty()) {
                setBackgroundResource(R.drawable.bg_main_address_correct)
                text = address
            } else {
                setBackgroundResource(R.drawable.bg_main_address_incorrect)
                text = getString(R.string.main_address_incorrect)
            }
        }
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
        with(binding.rvMainMarketCategory) {
            (adapter as MainMarketCategoryAdapter).submitList(null)
            (adapter as MainMarketCategoryAdapter).submitList(categories)
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
        binding.vgMainContinueLastSession.startBottomAnimate(true)
    }

    override fun hideContinueLastSession() {
        binding.vgMainContinueLastSession.startBottomAnimate(false)
    }

    override fun hideContinueLastSessionMainMenu() {
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
            presenter.onCheckEmptyBasket()
        }

        presenter.onStartMarketCategory()
    }

    override fun startOrdersHistoryFragment() {
        setToolbarTitleVisibility(true, getString(R.string.drawer_menu_item_my_orders_title))
        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            OrderHistoryFragment()
        ).commit()
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
        val intent = Intent(this, FirstAddressSelectionActivity::class.java)
        intent.putExtra(RETURNING_SCREEN_KEY, ReturningToScreenEnum.FROM_MAIN as? Parcelable)
        startActivity(intent)
    }

    override fun navigateAddressSelection() {
        TODO("Not yet implemented")
    }

    override fun setPartnerClickedBaner(partnerId: String?) {
        startActivityForResult(
            Intent(this, PartnersActivity::class.java)
                .apply {
                    putExtra(EXTRA_PARTNER_ID, partnerId)
                }, RESULT_CODE
        )
    }

    override fun navigateLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun navigatePartnerScreen(partnerId: String) {

        startActivityForResult(
            Intent(this, PartnersActivity::class.java)
                .apply {
                    putExtra(PartnersActivity.EXTRA_PARTNER_ID, partnerId)
                },
            PartnersActivity.RESULT_CODE
        )
    }

    override fun showSpecialOffers(promotions: List<Promotion>) {
        with(binding) {
            imageSlider.makeVisible()

            sliderAdapter.setData(promotions.map { SlideModel(it.imgUri, it.id, it.partnerId) })
        }
    }

    override fun hideSpecialOffers() {
        binding.imageSlider.makeGone()
    }

    private fun initSliderRecycler() {
        with(binding.imageSlider) {
            PagerSnapHelper().attachToRecyclerView(this)
            adapter = sliderAdapter
        }
    }

    private fun setTimerForImageSlider() {

        with(binding.imageSlider) {
            val sliderHandler = Handler()
            val sliderRunnable = Runnable {
                val position =
                    (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (position + 1 >= sliderAdapter.itemCount) {
                    smoothScrollToPosition(0)
                } else {
                    smoothScrollToPosition(position + 1)
                }
            }

            swipeTimer = Timer()
            swipeTimer?.schedule(object : TimerTask() {
                override fun run() {
                    sliderHandler.post(sliderRunnable)
                }
            }, IMAGE_SLIDE_DELAY, IMAGE_SLIDE_PERIOD)
        }
    }

    private fun checkUserLoggedStatus() {
        binding.vgMainMenu.tvDrawerMenuItemsOrders.isVisible = presenter.getUserLoggedStatus()
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

    private fun marketCategoryArguments(category: MarketCategory) = Bundle().apply {
        putString(
            MarketCategoryFragment.ARGUMENT_MARKET_CATEGORY_NAME,
            category.categoryName
        )
        putInt(
            MarketCategoryFragment.ARGUMENT_MARKET_CATEGORY_ID,
            category.id
        )
        putString(
            MarketCategoryFragment.ARGUMENT_MARKET_CATEGORY_IMAGE,
            category.categoryImgUri
        )
    }

    private fun configureMenuItemsClick(onClick: (menuItem: View) -> Unit) {
        with(binding) {

            val menuItems = listOf(
                vgMainMenu.tvDrawerMenuItemShops,
                vgMainMenu.tvDrawerMenuItemsOrders,
                vgMainMenu.tvDrawerMenuItemContacts,
                vgMainMenu.tvDrawerMenuItemAboutUs,
                vgMainMenu.tvDrawerMenuItemVacancies
            )

            menuItems.forEach {
                it.setOnClickListener { clickedView ->
                    menuItems.forEach { item ->
                        item.isSelected = item == clickedView
                    }

                    when (clickedView) {
                        vgMainMenu.tvDrawerMenuItemShops -> startMarketCategoryFragment(
                            presenter.getMarketCategory()
                        )
                        vgMainMenu.tvDrawerMenuItemsOrders -> startOrdersHistoryFragment()
                        vgMainMenu.tvDrawerMenuItemAboutUs -> startAboutServiceFragment()
                        vgMainMenu.tvDrawerMenuItemContacts -> startContactsFragment()
                        vgMainMenu.tvDrawerMenuItemVacancies -> startVacanciesFragment()
                    }
                    onClick(clickedView)
                }
            }
        }
    }

    override fun showBasketAmount(basketAmount: String) {
        binding.tvBasketAmount.text = getString(R.string.main_btn_amount, basketAmount)
    }

    override fun showPartnerName(partnerName: String?) {
        with(binding.tvBasketTitle) {
            if (partnerName.isNullOrEmpty()) {
                text = getString(R.string.basket_title)
            } else {
                text = partnerName
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }

    private fun setToolbarTitleVisibility(isVisible: Boolean, title: String?) {
        with(binding) {
            val visibility = if (isVisible) View.GONE else View.VISIBLE
            abMain.visibility = visibility
            //TODO("Change View.GONE on visibility")
            ivMainSearch.visibility = View.GONE
            tvMainUserAddress.visibility = visibility
            vgMainContinueLastSession.visibility =
                if (shouldLastBasketSessionBeVisible && !isVisible)
                    View.VISIBLE
                else
                    View.GONE

            if (isVisible) {
                val params = vgMainContent.layoutParams as CoordinatorLayout.LayoutParams
                params.behavior = null
                vgMainContent.layoutParams = params
            }
            tvMainTitle.text = title
            tvMainTitle.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    companion object {
        const val RESULT_CODE = 1
        const val EXTRA_PARTNER_ID = "partnerId"
        const val RETURNING_SCREEN_KEY = "RETURNING_SCREEN_KEY"
        private const val IMAGE_SLIDE_DELAY = 0L
        private const val IMAGE_SLIDE_PERIOD = 3000L
        private const val FIRST_IMAGE = 0
    }
}