package ooo.cron.delivery.screens.pay_dialog_screen

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.DialogOrderBinding
import ooo.cron.delivery.databinding.FragmentCommentBinding
import javax.inject.Inject
import androidx.core.content.ContextCompat.getSystemService




class OrderCommentBottomDialog() : BottomSheetDialogFragment() {

    private val viewModel: OrderViewModel by viewModels {
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
        binding.btnSaveComment.setOnClickListener{
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
