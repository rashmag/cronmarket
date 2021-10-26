package ooo.cron.delivery.screens.partners_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_partners_info.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.databinding.ActivityPartnersBinding
import ooo.cron.delivery.screens.AcceptDialog
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.basket_screen.BasketActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.utils.CustomLayoutManager
import ooo.cron.delivery.utils.ProductBottomSheetDialog
import java.util.*
import javax.inject.Inject


/*
 * Created by Muhammad on 02.05.2021
 */



class PartnersActivity : BaseActivity(), PartnersContract.View,
    PartnerCategoryAdapter.OnCategoryClickListener, CategoryAdapter.OnProductClickListener {

    @Inject
    lateinit var presenter: PartnersPresenter

    @Inject
    lateinit var binding: ActivityPartnersBinding

    private lateinit var partnerId: String

    private var nestedScrollViewConfigured = false

    private lateinit var productsLayoutManager: CustomLayoutManager

    private lateinit var productsAdapter: PartnerProductAdapter

    var scrollRange = -1
    var overScroll = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setResult(RESULT_CODE)
        setContentView(binding.root)
        partnerId = intent.getStringExtra(EXTRA_PARTNER_ID) as String

        productsLayoutManager = object : CustomLayoutManager(this) {
            override fun scrollVerticallyBy(
                dy: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                overScroll = dy - scrollRange

//                productsLayoutManager.setScrollEnabled(overScroll < 0)
                return scrollRange
            }
        }

        setTitleVisibility()
        onProductRecyclerViewScrollChanged()
        initPartnerRecyclerView()
    }

    private fun initPartnerRecyclerView() {
        productsAdapter = PartnerProductAdapter()
        productsAdapter.setProductClickListener(this)
        binding.rvProduct.apply {
            layoutManager = productsLayoutManager
            adapter = productsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getPartnerInfo()
    }

    private fun onProductRecyclerViewScrollChanged() {
        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val layoutManger = recyclerView.layoutManager as LinearLayoutManager

                val visiblePosition = layoutManger.findFirstVisibleItemPosition()
                val firstCompletelyVisiblePosition =
                    layoutManger.findFirstCompletelyVisibleItemPosition()

                val categoryAdapter = binding.rvCategories.adapter as PartnerCategoryAdapter
                if (visiblePosition > -1) {
                    if (firstCompletelyVisiblePosition == -1) {
                        binding.rvCategories.smoothScrollToPosition(visiblePosition)
                        val categoryId = categoryAdapter.getCategoryId(visiblePosition)
                        categoryAdapter.setSelected(categoryId)
                    } else {
                        binding.rvCategories.smoothScrollToPosition(firstCompletelyVisiblePosition)
                        val categoryId =
                            categoryAdapter.getCategoryId(firstCompletelyVisiblePosition)
                        categoryAdapter.setSelected(categoryId)
                    }
                }
            }
        })
    }

    private fun injectDependencies() =
        App.appComponent
            .partnersComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)


    override fun getPartnerId(): String {
        return partnerId
    }

    override fun showPartnerInfo(partnerInfo: PartnersInfoRes) {
        presenter.getPartnerCategory()
        with(partnerInfo) {
            binding.run {
                tvPartnersName.text = name
                tvPartnersCategory.text = shortDescription
                tvRating.text =
                    if (feedbackCount == 0)
                        rating.toString()
                    else
                        "$rating ($feedbackCount)"

                tvFreeDelivery.text =
                    if (minAmountDelivery == null)
                        String.format(
                            getString(R.string.partners_activity_min_order_template),
                            minAmountOrder
                        )
                    else
                        String.format(
                            getString(R.string.partners_activity_free_delivery_template),
                            minAmountDelivery
                        )

                if (partnerCardImg != null) {
                    Glide.with(binding.root)
                        .load(partnerInfo.partnerCardImg)
                        .centerCrop()
                        .into(backdrop)
                } else {

                    binding.vgPartnerInfo.animate().alpha(0f).setDuration(600).start()


                    val collapsingParams =
                        appbar.layoutParams as CollapsingToolbarLayout.LayoutParams
                    collapsingParams.collapseMode =
                        CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF
                    appbar.layoutParams = collapsingParams
                    appbar.setExpanded(false)

                    nestedScrollViewConfigured = true


                    val appBarParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    appBarParams.height = LinearLayout.LayoutParams.WRAP_CONTENT

                    appbar.layoutParams = appBarParams
                }
            }
        }
        onBackButtonClick()
        onInfoButtonClick(partnerInfo)
        onRatingClick()
    }

    private fun collapseToolbar() {
        productsLayoutManager.setScrollEnabled(true)

        if (!nestedScrollViewConfigured) {
            binding.vgPartnerInfo.animate().alpha(0f).setDuration(600).start()
            binding.appbar.setExpanded(false)
            binding.tvTitle.text = binding.tvPartnersName.text
            binding.tvTitle.animate().alpha(1f).setDuration(600).start()
//
//
//            val appBarParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//            appBarParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
//            binding.appbar.layoutParams = appBarParams
            nestedScrollViewConfigured = true
        }
    }

    private fun onRatingClick() {
//        binding.tvRating.setOnClickListener {
//            Toast.makeText(this, "Переход на экран отзывов", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun onBackButtonClick() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onInfoButtonClick(partnerInfo: PartnersInfoRes) {
        binding.ivInfo.setOnClickListener {
            val bottomSheetInfoDialog =
                BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            val bottomSheetView =
                LayoutInflater.from(this).inflate(R.layout.dialog_partners_info, bottom_sheet)
            bottomSheetView.findViewById<Button>(R.id.btn_close).setOnClickListener {
                bottomSheetInfoDialog.dismiss()
            }

            bottomSheetView.findViewById<TextView>(R.id.tv_title).text = partnerInfo.name
            bottomSheetView.findViewById<TextView>(R.id.tv_body).text = partnerInfo.fullDescription

            bottomSheetInfoDialog.setContentView(bottomSheetView)
            bottomSheetInfoDialog.show()
        }
    }

    override fun showPartnerCategory(body: PartnerCategoryRes) {
        binding.run {
            presenter.getPartnerProducts()
            rvCategories.apply {
                layoutManager =
                    LinearLayoutManager(
                        this@PartnersActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                adapter = PartnerCategoryAdapter(body, this@PartnersActivity)
            }
        }
    }

    override fun onCategoryClick(position: Int) {
        collapseToolbar()


        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = position

        binding.rvProduct.layoutManager?.startSmoothScroll(smoothScroller)

        val centerOfScreen = binding.rvCategories.width / 3
        (binding.rvCategories.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            centerOfScreen
        )
    }

    override fun removeProgress() {
        binding.vgPartnersActivityProgress.root.visibility = View.GONE
    }

    override fun showPartnerProducts(
        productCategoriesModel: ArrayList<ProductCategoryModel>
    ) {

        productsLayoutManager.setScrollEnabled(false)
        binding.run {
            vgMainView.removeView(binding.vgPartnersActivityProgress.root)
            productsAdapter.setData(productCategoriesModel)
        }
    }

    override fun showClearBasketDialog(onDismiss: () -> Unit, onAccept: () -> Unit) {
        ClearBasketDialog(onDismiss, onAccept).show(
            supportFragmentManager,
            AcceptDialog::class.simpleName
        )
    }

    override fun updateBasketPreview(quantity: Int, basketPrice: String) {
        binding.tvPartnerBasket.text = getString(R.string.partner_basket, quantity)
        binding.btnPartnerBasketPrice.text = getString(R.string.partner_basket_price, basketPrice)

        binding.vgPartnerBasket.visibility =
            if (quantity > 0)
                View.VISIBLE
            else
                View.GONE

        with(
            View.OnClickListener { presenter.onBasketClicked() }
        ) {
            binding.vgPartnerBasket.setOnClickListener(this)
            binding.btnPartnerBasketPrice.setOnClickListener(this)
        }
    }

    override fun showChangeAddressDialog() {
        RequiredAddressDialog(
            { presenter.requiredAddressDialogDeclined() },
            { presenter.requiredAddressDialogAccepted() }
        ).show(supportFragmentManager, RequiredAddressDialog::class.simpleName)
    }

    override fun showChangeAddressScreen() {
        startActivity(Intent(this, FirstAddressSelectionActivity::class.java))
    }

    override fun showProductInfo(product: PartnerProductsRes) {
        ProductBottomSheetDialog(
            this,
            product,
            presenter::plusClick
        ).show()
    }

    override fun navigateBasket(
        openHours: Int,
        openMinutes: Int,
        closeHours: Int,
        closeMinutes: Int
    ) {
        startActivity(
            Intent(this@PartnersActivity, BasketActivity::class.java)
                .putExtra(
                    BasketActivity.PARTNER_OPEN_HOURS, openHours,
                ).putExtra(
                    BasketActivity.PARTNER_OPEN_MINUTES, openMinutes
                ).putExtra(
                    BasketActivity.PARTNER_CLOSE_HOURS, closeHours
                ).putExtra(
                    BasketActivity.PARTNER_CLOSE_MINUTES, closeMinutes
                )
        )
    }

    override fun onProductClick(product: PartnerProductsRes) {
        presenter.productClick(product)
    }

    override fun onPlusClick(
        product: PartnerProductsRes,
        additives: List<BasketDishAdditive>,
        quantity: Int
    ) =
        presenter.plusClick(product, additives, quantity)

    override fun onMinusClick(product: PartnerProductsRes, quantity: Int) =
        presenter.minusClick(product, quantity)

    private fun setTitleVisibility() {
        var isShow = false
        var scrollRange = -1

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }


            if (scrollRange + verticalOffset < 150) {
                binding.vgPartnerInfo.animate().alpha(0f).setDuration(600).start()
            } else if (!isShow) {
                binding.vgPartnerInfo.animate().alpha(1f).setDuration(600).start()
            }


            if (scrollRange + verticalOffset == 0) {
                binding.tvTitle.text = binding.tvPartnersName.text
                binding.tvTitle.animate().alpha(1f).setDuration(600).start()
                isShow = true
            } else if (isShow) {
                binding.tvTitle.animate().alpha(0f).setDuration(600).start()

                isShow = false
            }

            productsLayoutManager.setScrollEnabled(scrollRange + verticalOffset == 0)
            println("scrollRange ${scrollRange + verticalOffset == 0}")
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        const val RESULT_CODE = 1

        const val EXTRA_PARTNER_ID = "partnerId"
    }
}