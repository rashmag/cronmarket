package ooo.cron.delivery.screens.partners_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        partnerId = intent.getStringExtra(EXTRA_PARTNER_ID) as String
        setTitleVisibility()
        presenter.getPartnerInfo()
        onProductRecyclerViewScrollChanged()

    }

    private fun onProductRecyclerViewScrollChanged() {
        ViewCompat.setNestedScrollingEnabled(binding.rvProduct, false)
        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val visiblePosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                val categoryAdapter = binding.rvCategories.adapter as PartnerCategoryAdapter


                if (visiblePosition > -1) {
                    val categoryId = categoryAdapter.getCategoryId(visiblePosition)
                    categoryAdapter.setSelected(categoryId)
                    binding.rvCategories.smoothScrollToPosition(visiblePosition)
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

                Glide.with(binding.root)
                    .load(logo)
                    .into(ivPartnersLogo)

                if (mainWinImg != null) {
                    Glide.with(binding.root)
                        .load(partnerInfo.mainWinImg)
                        .centerCrop()
                        .into(backdrop)
                } else {
                    appbar.setExpanded(false)

                    val scrollViewParams =
                        LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    scrollViewParams.setMargins(
                        0,
                        resources.getDimensionPixelSize(R.dimen.nested_scroll_view_top_margin),
                        0,
                        0
                    )
                    nestedscrollview.layoutParams = scrollViewParams

                    val collapsingParams =
                        CollapsingToolbarLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    collapsingParams.collapseMode =
                        CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF
                    toolbar.layoutParams = collapsingParams


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

    private fun onRatingClick() {
        binding.tvRating.setOnClickListener {
            Toast.makeText(this, "Переход на экран отзывов", Toast.LENGTH_SHORT).show()
        }
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

    /**
     * т.к. наш recyclerView находится в NestedScrollView
     * скроллить до нужного нам итема мы будем по позиции этого итема
     */

    override fun onCategoryClick(position: Int) {
        val originalPos = IntArray(2)
        binding.rvProduct.getChildAt(position).getLocationInWindow(originalPos)
        val x = originalPos[0]
        val y = originalPos[1]

        binding.nestedscrollview.post {
            binding.nestedscrollview.smoothScrollTo(x, y)
        }
    }

    override fun removeProgress() {
        binding.vgPartnersActivityProgress.root.visibility = View.GONE
    }

    override fun showPartnerProducts(
        productCategoriesModel: ArrayList<ProductCategoryModel>
    ) {
        binding.run {
            vgMainView.removeView(binding.vgPartnersActivityProgress.root)
            rvProduct.apply {
                layoutManager = LinearLayoutManager(this@PartnersActivity)
                adapter = PartnerProductAdapter(productCategoriesModel, this@PartnersActivity)
            }
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
        val productBottomSheetDialog = ProductBottomSheetDialog(this, product)
        productBottomSheetDialog.show()
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

            if (scrollRange + verticalOffset == 0) {
                binding.tvTitle.text = binding.tvPartnersName.text
                binding.tvTitle.animate().alpha(1f).setDuration(600).start()
                isShow = true
            } else if (isShow) {
                binding.tvTitle.animate().alpha(0f).setDuration(600).start()
                isShow = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        const val EXTRA_PARTNER_ID = "partnerId"
        const val SPAN_COUNT = 2
    }
}