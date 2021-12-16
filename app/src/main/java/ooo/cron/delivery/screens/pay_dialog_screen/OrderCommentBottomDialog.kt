package ooo.cron.delivery.screens.pay_dialog_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ooo.cron.delivery.App
import ooo.cron.delivery.databinding.FragmentCommentBinding
import javax.inject.Inject


class OrderCommentBottomDialog() : BottomSheetDialogFragment() {

    private val viewModel: OrderViewModel by activityViewModels {
        factory.create()
    }

    @Inject
    lateinit var binding: FragmentCommentBinding

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
        binding.etComments.requestFocus()
        binding.btnSaveComment.setOnClickListener {
            viewModel.setComment(binding.etComments.text.toString())
            dismiss()
        }
    }

    private fun injectDependencies() {
        App.appComponent.orderComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }
}
