package ooo.cron.delivery.screens.partners_screen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject
import kotlinx.android.synthetic.main.dialog_partners_info.bottom_sheet
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDishAdditive
import ooo.cron.delivery.data.network.models.DeliveryCost
import ooo.cron.delivery.data.network.models.PartnerCategoryRes
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.data.network.models.ProductCategoryModel
import ooo.cron.delivery.databinding.ActivityPartnersBinding
import ooo.cron.delivery.screens.base.AcceptDialog
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.basket_screen.BasketActivity
import ooo.cron.delivery.screens.first_address_selection_screen.FirstAddressSelectionActivity
import ooo.cron.delivery.utils.CustomLayoutManager
import ooo.cron.delivery.screens.partners_screen.bottom_sheet_dialog.ProductBottomSheetDialog
import ooo.cron.delivery.utils.enums.ReturningToScreenEnum
import ooo.cron.delivery.utils.extensions.makeGone
import ooo.cron.delivery.utils.extensions.makeVisible
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.setDrawableStart
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.extensions.uiLazy


/*
 * Created by Muhammad on 02.05.2021
 */

class PartnersActivity : BaseActivity(), PartnersContract.View,
    CategoryAdapter.OnProductClickListener {

    @Inject
    lateinit var presenter: PartnersPresenter

    @Inject
    lateinit var binding: ActivityPartnersBinding

    private lateinit var partnerId: String
    private var isOpen = false
    private var openHours: Int? = null
    private var openMinutes: Int? = null

    private var nestedScrollViewConfigured = false

    private lateinit var productsLayoutManager: CustomLayoutManager

    private lateinit var productsAdapter: PartnerProductAdapter

    private val deliveryTypePriceAdapter by uiLazy {
        DeliveryTypePriceAdapter()
    }

    var scrollRange = -1
    var overScroll = -1
    private var minOrderAmount = 0

    private val deliveryTypePrices = arrayListOf<DeliveryCost>()
    private var deliveryTypeName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setResult(RESULT_CODE)
        setContentView(binding.root)

        partnerId = intent.getStringExtra(EXTRA_PARTNER_ID) as String
        isOpen = intent.getBooleanExtra(EXTRA_IS_OPEN, true)
        openHours = intent.getIntExtra(EXTRA_OPEN_HOURS, 0)
        openMinutes = intent.getIntExtra(EXTRA_OPEN_MINUTES, 0)

        productsLayoutManager = object : CustomLayoutManager(this) {
            override fun scrollVerticallyBy(
                dy: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                overScroll = dy - scrollRange

                if (overScroll < 0) binding.scrolledErrorContainer.visibility = View.GONE
                else showBottomCloseShopError()

                return scrollRange
            }
        }

        setTitleVisibility()
        showCloseShopError()
        setImageSize()
        onProductRecyclerViewScrollChanged()
        initPartnerRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.getPartnerInfo()
    }

    private fun setImageSize() {
        if (!isOpen) {

            val dimensionInDp =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, HEADER_IMAGE_SIZE_WHEN_PARTNER_CLOSE, resources.displayMetrics
                ).toInt()

            binding.imageContainer.apply {
                layoutParams.height = dimensionInDp
                requestLayout()
            }
        }
    }

    private fun initPartnerRecyclerView() {
        productsAdapter = PartnerProductAdapter(isOpen)
        productsAdapter.setProductClickListener(this)
        binding.rvProduct.apply {
            layoutManager = productsLayoutManager
            adapter = productsAdapter
        }
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
                        categoryAdapter.setSelected(visiblePosition)
                    } else {
                        binding.rvCategories.smoothScrollToPosition(firstCompletelyVisiblePosition)
                        categoryAdapter.setSelected(firstCompletelyVisiblePosition)
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

    override fun showLikePartner() {
        binding.cbFavorite.isChecked = true
    }

    override fun showUnlikePartner() {
        binding.cbFavorite.isChecked = false
    }

    override fun showPartnerInfo(partnerInfo: PartnersInfoRes) {
        presenter.getPartnerCategory()
        with(partnerInfo) {
            binding.run {
                tvPartnersName.text = name
                tvPartnersCategory.text = shortDescription
                cbFavorite.isChecked = isFavorite
                cbFavorite.setOnClickListener {
                    if (cbFavorite.isChecked) {
                        presenter.likePartner(partnerInfo.id)
                    }
                    else {
                        presenter.unlikePartner(partnerId)
                    }
                }

                // Типы доставок --Начало-------------------------------------------

                openDeliveryTypeBottomSheet()

                deliveryFrames?.forEach { deliveryFrames ->

                    deliveryTypePrices.clear()
                    deliveryTypePrices.addAll(deliveryFrames.deliveryCosts)
                    deliveryTypeName = deliveryFrames.deliveryTypeName

                    if (deliveryFrames.deliveryTypeName == DELIVERY_CRON_MARKET) {
                        deliveryTypeTitle.setDrawableStart(R.drawable.ic_cron_delivers)
                    } else {
                        deliveryTypeTitle.setDrawableStart(R.drawable.ic_partner_delivers)
                    }

                    val formatPrice = getString(R.string.price)
                    if (deliveryFrames.deliveryCosts.first().deliveryCost == deliveryFrames.deliveryCosts.last().deliveryCost) {
                        deliveryTypeTitle.text = getString(
                            R.string.partners_screen_delivery_type_title_first,
                            String.format(
                                formatPrice,
                                deliveryFrames.deliveryCosts.last().deliveryCost
                            )
                        )
                    } else {
                        deliveryTypeTitle.text = getString(
                            R.string.partners_screen_delivery_type_title,
                            deliveryFrames.deliveryCosts.last().deliveryCost,
                            String.format(
                                formatPrice,
                                deliveryFrames.deliveryCosts.first().deliveryCost
                            ),
                        )
                    }
                }

                // Типы доставок --Конец-------------------------------------------

                minOrderAmount = minAmountOrder

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
//        onInfoButtonClick(partnerInfo)
        onRatingClick()
    }

    private fun openDeliveryTypeBottomSheet() {

        binding.deliveryTypeTitle.setOnClickListener {
            val bottomSheetInfoDialog =
                BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            val bottomSheetView =
                LayoutInflater.from(this).inflate(R.layout.delivery_type_bottom_sheet, bottom_sheet)
            bottomSheetView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
                bottomSheetInfoDialog.dismiss()
            }

            val title = bottomSheetView.findViewById<TextView>(R.id.title)

            if (deliveryTypeName == DELIVERY_CRON_MARKET) {
                title.setDrawableStart(R.drawable.ic_cron_delivers)
                title.text = getString(R.string.partners_screen_delivery_type_delivers_cron)
            } else {
                title.setDrawableStart(R.drawable.ic_partner_delivers)
                title.text = getString(R.string.partners_screen_delivery_type_delivers_partner)
            }

            val recyclerPrice = bottomSheetView.findViewById<RecyclerView>(R.id.recycler_delivery_type_price)

            recyclerPrice?.adapter = deliveryTypePriceAdapter
            deliveryTypePriceAdapter.submitList(deliveryTypePrices)

            bottomSheetInfoDialog.setContentView(bottomSheetView)
            bottomSheetInfoDialog.show()
        }
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
                adapter = PartnerCategoryAdapter(
                    categoryRes = body,
                    onCategoryClick = { position ->
                        onCategoryClick(position)
                    }
                )
            }
        }
    }

    private fun onCategoryClick(position: Int) {
        collapseToolbar()

        (binding.rvProduct.layoutManager as? CustomLayoutManager)?.scrollToPositionWithOffset(position, 0)

        val centerOfScreen = binding.rvCategories.width / 3
        (binding.rvCategories.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
            position,
            centerOfScreen
        )

        nestedScrollViewConfigured = false
    }

    override fun removeProgress() {
        binding.vgPartnersActivityProgress.root.visibility = View.GONE
    }

    override fun showPartnerProducts(
        productCategoriesModel: ArrayList<ProductCategoryModel>
    ) {

        productsLayoutManager.setScrollEnabled(true)
        with(binding) {
            vgMainView.removeView(vgPartnersActivityProgress.root)
            productsAdapter.submitList(productCategoriesModel)
            productsAdapter.setSectionData(productCategoriesModel)
        }
    }

    override fun showClearBasketDialog(onDismiss: () -> Unit, onAccept: () -> Unit) {
        ClearBasketDialog(onDismiss, onAccept).show(
            supportFragmentManager,
            AcceptDialog::class.simpleName
        )
    }

    override fun getMinOrderAmount() = minOrderAmount

    override suspend fun updateBasketPreview(quantity: Int, basketPrice: String) {
        with(binding) {
            btnPartnerBasketPrice.text = getString(R.string.partner_basket_price, basketPrice)

            vgPartnerBasket.run {
                startBottomAnimate(
                    quantity > 0 &&
                            isOpen &&
                            presenter.checkPartnerId().not()
                )
            }

            with(
                View.OnClickListener { presenter.onBasketClicked() }
            ) {
                vgPartnerBasket.setOnClickListener(this)
                btnPartnerBasketPrice.setOnClickListener(this)
            }
        }
    }

    override fun showChangeAddressDialog() {
        RequiredAddressDialog(
            { presenter.requiredAddressDialogDeclined() },
            { presenter.requiredAddressDialogAccepted() }
        ).show(supportFragmentManager, RequiredAddressDialog::class.simpleName)
    }

    override fun showChangeAddressScreen() {
        startActivity(
            Intent(this, FirstAddressSelectionActivity::class.java)
                .putExtra(RETURNING_SCREEN_KEY, ReturningToScreenEnum.FROM_PARTNERS as? Parcelable)
        )
    }

    override fun showOrderFromDialog() {
        AlertDialog.Builder(this)
            .setTitle(EMPTY_TITLE)
            .setIcon(R.mipmap.ic_launcher)
            .setMessage(getString(R.string.partners_activity_dialog_min_price_title, minOrderAmount.toString()))
            .setCancelable(false)
            .setPositiveButton(R.string.partners_activity_dialog_btn_ok_title) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showProductInfo(product: PartnerProductsRes) {
        ProductBottomSheetDialog(
            mContext = this,
            product = product,
            onAddClick = presenter::plusClick,
            onMinusClick = presenter::minusClick,
            quantity = product.inBasketQuantity + 1
        ).show()
    }

    override fun navigateBasket(
        openHours: Int,
        openMinutes: Int,
        closeHours: Int,
        closeMinutes: Int,
        basket: Basket?
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
                ).putExtra(
                    BasketActivity.BASKET_MODEL, basket
                ).putExtra(
                    BasketActivity.MIN_AMOUNT_ORDER, minOrderAmount
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

        with(binding) {
            appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange.orZero()
                }


                if (scrollRange + verticalOffset < 150) {
                    vgPartnerInfo.animate().alpha(0f).setDuration(0).start()
                    vgRating.makeGone()
                } else if (isShow.not()) {
                    vgPartnerInfo.animate().alpha(1f).setDuration(600).start()
                    vgRating.makeVisible()
                }


                if (scrollRange + verticalOffset == 0) {
                    tvTitle.text = tvPartnersName.text
                    tvTitle.animate().alpha(1f).setDuration(0).start()
                    isShow = true
                } else if (isShow) {
                    tvTitle.animate().alpha(0f).setDuration(600).start()

                    isShow = false
                }
                productsLayoutManager.setScrollEnabled(true)
                println("scrollRange ${scrollRange + verticalOffset == 0}")
            })
        }
    }



    private fun showCloseShopError() {
        val hours = if ((openHours?.div(10) ?: 0) > 0) openHours else "0$openHours"
        val minutes = if ((openMinutes?.div(10) ?: 0) > 0) openMinutes else "0$openMinutes"

        binding.tvCloseShopError.isVisible = isOpen == false
        binding.tvCloseShopError.text = binding.root.context.getString(
            R.string.partner_closed,
            "${hours}:${minutes}"
        )
    }

    private fun showBottomCloseShopError() {
        val hours = if ((openHours?.div(10) ?: 0) > 0) openHours else "0$openHours"
        val minutes = if ((openMinutes?.div(10) ?: 0) > 0) openMinutes else "0$openMinutes"

        binding.scrolledErrorContainer.startBottomAnimate(isOpen == false)
        binding.tvScrollShopError.text = binding.root.context.getString(
            R.string.partner_closed,
            "${hours}:${minutes}"
        )
    }



    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        const val RESULT_CODE = 1

        const val EXTRA_PARTNER_ID = "partnerId"
        const val EXTRA_IS_OPEN = "is_open"
        const val EXTRA_OPEN_HOURS = "open_hours"
        const val EXTRA_OPEN_MINUTES = "open_minutes"

        const val RETURNING_SCREEN_KEY = "RETURNING_SCREEN_KEY"

        private const val NUMBER_SERVINGS_ON_BOTTOM_SHEET = 1
        private const val EMPTY_QUANTITY = 0

        const val HEADER_IMAGE_SIZE_WHEN_PARTNER_CLOSE = 590f

        const val EMPTY_TITLE = " "

        private const val DELIVERY_CRON_MARKET = "Доставка КронМаркет"
    }
}