package ooo.cron.delivery.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnerProductsRes
import ooo.cron.delivery.databinding.DialogProductInfoBinding
import ooo.cron.delivery.screens.partners_screen.AdditivesAdapter
import ooo.cron.delivery.screens.partners_screen.RequireAdditivesAdapter


/*
 * Created by Muhammad on 17.05.2021
 */



class ProductBottomSheetDialog(context: Context, private val product: PartnerProductsRes) :
    BottomSheetDialog(context, R.style.BottomSheetDialogTheme),
    AdditivesAdapter.OnRequireAdditivesListener {


    private lateinit var binding: DialogProductInfoBinding

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


    private fun initView() {
        with(binding) {
            tvName.text = product.name
            tvCost.text = product.cost.toString()
            tvDescription.text = product.description

            Glide.with(binding.root)
                .load(product.photo)
                .into(ivProduct)

            ivPlus.setOnClickListener {
                if (tvPortionCount.text != "10") {
                    tvPortionCount.text =
                        (tvPortionCount.text.toString()
                            .toInt() + 1).toString()


                    tvCost.text =
                        (tvCost.text.toString()
                            .toInt() + product.cost).toString()
                }
            }


            ivMinus.setOnClickListener {
                if (tvPortionCount.text != "1") {
                    tvPortionCount.text =
                        (tvPortionCount.text.toString()
                            .toInt() - 1).toString()


                    tvCost.text =
                        (tvCost.text.toString()
                            .toInt() - product.cost).toString()
                }
            }


            if (product.requiredAdditiveGroups.isNotEmpty()) {
                initRequireAdditivesRecycler(product.requiredAdditiveGroups)
            }

            if (product.additives.isNotEmpty()) {
                lvAdditives.apply {
                    choiceMode = ListView.CHOICE_MODE_MULTIPLE
                    adapter = ooo.cron.delivery.screens.partners_screen.AdditiveArrayAdapter(
                        context,
                        R.layout.item_additive, product.additives
                    )

                }
            } else {
                vgAdditives.visibility =
                    android.view.View.GONE
            }
        }
    }

    private fun initRequireAdditivesRecycler(requiredAdditiveGroups: List<PartnerProductsRes.RequiredAdditiveGroups>) {
        binding.rvRequireAdditives.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RequireAdditivesAdapter(requiredAdditiveGroups, this@ProductBottomSheetDialog)
        }
    }

}