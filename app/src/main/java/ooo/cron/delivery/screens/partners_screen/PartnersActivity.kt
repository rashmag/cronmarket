package ooo.cron.delivery.screens.partners_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_partners_info.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.databinding.ActivityPartnersBinding
import ooo.cron.delivery.databinding.DialogPartnersInfoBinding
import ooo.cron.delivery.screens.BaseActivity
import javax.inject.Inject


/*
 * Created by Muhammad on 02.05.2021
 */



class PartnersActivity : BaseActivity(), PartnersContract.View {

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
//        partnerId = intent.getStringExtra("partnerId") as String
        partnerId = "89a6d43a-99b3-4260-98d3-03e8bb6e915a"
        setTitleVisibility()
        presenter.getPartnerInfo()
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
        with(partnerInfo) {
            binding.run {
                vgMainView.removeView(vgPartnersActivityProgress.root)
                tvPartnersName.text = name
                tvPartnersCategory.text = shortDescription
                tvRating.text = rating.toString()
                tvFreeDelivery.text = String.format(
                    getString(R.string.partners_activity_min_order_template),
                    minAmountOrder
                )

                Glide.with(binding.root)
                    .load(partnerInfo.logo)
                    .centerCrop()
                    .into(backdrop)
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
}