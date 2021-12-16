package ooo.cron.delivery.screens.pay_dialog_screen

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.item_main_market_category.view.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.DialogOrderBinding
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderBottomDialog() : BottomSheetDialogFragment() {
    /*
     @Inject
     lateinit var commentBottomDialog: OrderCommentBottomDialog
 */
    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var binding: DialogOrderBinding

    @Inject
    lateinit var factory: OrderViewModelFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.commentTextLiveData.observe(viewLifecycleOwner) {
            with(binding.etComments) {
                text = if(it.isNotBlank()) it else getString(R.string.order_comment)
                val bg =
                    if (it.isNotBlank()) R.drawable.bg_true_light else R.drawable.bg_main_address_correct
                setBackgroundResource(bg)
                gravity = if (it.isNotBlank()) Gravity.START else Gravity.CENTER
                val endIcon = if(it.isNotBlank())R.drawable.ic_market_category_tag_check else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, endIcon, 0)
            }
        }

        binding.etComments.showSoftInputOnFocus = false
        binding.etComments.setOnClickListener {
            OrderCommentBottomDialog().show(parentFragmentManager, "")
        }
    }

    private fun injectDependencies() {
        App.appComponent.orderComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }
}