package ooo.cron.delivery.screens.contacts_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import ooo.cron.delivery.R
import ooo.cron.delivery.databinding.FragmentContactsBinding
import ooo.cron.delivery.screens.base.BaseFragment


/*
 * Created by Muhammad on 02.06.2021
 */



class ContactsFragment : BaseFragment() {


    companion object {
        const val PERMISSION_REQUEST_CODE = 112
    }

    private lateinit var binding: FragmentContactsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSaidContactsClick()
        onMagomedContactsClick()
    }

    private fun onMagomedContactsClick() {
        binding.tvMagomedPhone.setOnClickListener {
            startCall(requireContext().getString(R.string.magomed_phone))
        }
        binding.tvMagomedEmail.setOnClickListener {
            sendEmail(requireContext().getString(R.string.magomed_email))
        }
    }

    private fun onSaidContactsClick() {
        binding.tvSaidPhone.setOnClickListener {
            startCall(requireContext().getString(R.string.said_lev_phone))
        }
        binding.tvSaidEmail.setOnClickListener {
            sendEmail(requireContext().getString(R.string.said_lev_email))
        }
    }


    private fun startCall(phone: String) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        }
    }

    private fun sendEmail(email: String) {
        startActivity(
            Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null
                )
            )
        )

    }
}