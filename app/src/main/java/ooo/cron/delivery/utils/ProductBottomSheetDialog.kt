package ooo.cron.delivery.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDishAdditive
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.data.network.models.RequireAdditiveModel
import ooo.cron.delivery.databinding.DialogProductInfoBinding
import ooo.cron.delivery.screens.partners_screen.AdditiveRecyclerAdapter
import ooo.cron.delivery.screens.partners_screen.AdditivesAdapter
import ooo.cron.delivery.screens.partners_screen.RequireAdditivesAdapter


/*
 * Created by Muhammad on 17.05.2021
 */


class ProductBottomSheetDialog(
    private val mContext: Context,
    private val product: PartnerProductsRes,
    private val onAddClick: (
        product: PartnerProductsRes,
        additives: List<BasketDishAdditive>,
        quantity: Int
    ) -> Unit,
    private val onMinusClick: (
        product: PartnerProductsRes,
        quantity: Int
    ) -> Unit,
    private var quantity: Int
) :
    BottomSheetDialog(
        mContext, R.style.BottomSheetDialogTheme
    ),
    AdditivesAdapter.OnRequireAdditivesListener,
    AdditiveRecyclerAdapter.onDopProductClickListener {


    private lateinit var binding: DialogProductInfoBinding
    private var additiveList = ArrayList<RequireAdditiveModel>()

    private var portionPriceCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogProductInfoBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.dialog_product_info, null)
        )
        setContentView(binding.root)
        initView()
        window?.setLayout(
            MATCH_PARENT,
            WRAP_CONTENT
        )
    }

    override fun onStart() {
        super.onStart()
        setOnShowListener {
            setupFullHeight(it as BottomSheetDialog)
        }
        setupDismissOnStateChanged(this)
    }

    private fun initView() {

        portionPriceCount = quantity * product.cost

        with(binding) {
            tvName.text = product.name
            tvPortionCount.text = quantity.toString()
            tvCost.text = portionPriceCount.toString()
            tvDescription.text = product.description

            Glide.with(binding.root)
                .load(product.photo)
                .apply(
                    RequestOptions().apply {
                        transform(CenterCrop(), GranularRoundedCorners(24f, 24f, 0f, 0f))
                    }
                )
                .into(ivProduct)

            ivPlus.setOnClickListener {
                quantity += 1
                tvPortionCount.text = quantity.toString()

                portionPriceCount += product.cost
                tvCost.text = portionPriceCount.toString()
            }

            ivMinus.setOnClickListener {
                // Кол-во порций
                if (quantity > 1) {
                    quantity -= 1
                    tvPortionCount.text = quantity.toString()
                }else {
                    quantity = 1
                }

                // Цена порции
                if(portionPriceCount > product.cost) {
                    portionPriceCount -= product.cost
                    tvCost.text = portionPriceCount.toString()
                }else{
                    tvCost.text = portionPriceCount.toString()
                }
            }

            product.requiredAdditiveGroups.forEach {
                additiveList.add(RequireAdditiveModel(it.name, it.additives))
            }
            if (product.requiredAdditiveGroups.isNotEmpty()) {
                initRequireAdditivesRecycler(additiveList)
            }

            if (product.additives.isNotEmpty()) {
                rvAdditives.apply {
                    layoutManager = LinearLayoutManager(context)
                }
                val additiveRecyclerAdapter = AdditiveRecyclerAdapter(product.additives)
                additiveRecyclerAdapter.setListener(this@ProductBottomSheetDialog)
                rvAdditives.adapter = additiveRecyclerAdapter
            } else {
                vgAdditives.visibility = View.GONE
            }

            btnAdd.setOnClickListener {
                val adapterRequireAdditives = rvRequireAdditives.adapter
                val adapterAdditives = rvAdditives.adapter
                val additives = when {
                    adapterRequireAdditives is RequireAdditivesAdapter -> {
                        if (adapterAdditives is AdditiveRecyclerAdapter) {
                            adapterRequireAdditives.getCheckedAdditives() + adapterAdditives.getCheckedAdditives()
                        } else {
                            adapterRequireAdditives.getCheckedAdditives()
                        }
                    }
                    adapterAdditives is AdditiveRecyclerAdapter -> {
                        adapterAdditives.getCheckedAdditives()
                    }
                    else -> emptyList()
                }

                if (quantity >= product.inBasketQuantity) {
                    var qua = quantity - product.inBasketQuantity
                    onAddClick(
                        product,
                        additives.map { BasketDishAdditive(it.id, it.name, it.cost.toDouble()) },
                        qua
                    )
                } else {
                    onMinusClick(
                        product,
                        product.inBasketQuantity - quantity
                    )
                }
                dismiss()
            }
        }
        binding.tvCost.text = product.cost.toString()
    }

    private fun initRequireAdditivesRecycler(requiredAdditiveGroups: ArrayList<RequireAdditiveModel>) {
        binding.rvRequireAdditives.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RequireAdditivesAdapter(requiredAdditiveGroups,
                this@ProductBottomSheetDialog)
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupDismissOnStateChanged(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    override fun setIncreasedPriceDopProduct(increasedPriceDopProduct: String) {
        val price = binding.tvCost.text.toString().toInt().plus(increasedPriceDopProduct.toInt())
        binding.tvCost.text = price.toString()
    }

    override fun setReducePriceDopProduct(reducePriceDopProduct: String) {
        val price = binding.tvCost.text.toString().toInt().minus(reducePriceDopProduct.toInt())
        binding.tvCost.text = price.toString()
    }

}