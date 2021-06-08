package ooo.cron.delivery.screens.vacancies_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import ooo.cron.delivery.databinding.FragmentVacanciesBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.contacts_screen.ContactsFragment

/*
 * Created by Muhammad on 05.06.2021
 */



class VacanciesFragment : BaseFragment() {


    private lateinit var binding: FragmentVacanciesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onPhonesClickListener()
    }

    private fun onPhonesClickListener() {
        with(binding) {
            tvFirstPhone.setOnClickListener {
                startCall(tvFirstPhone.text.toString())
            }

            tvSecondPhone.setOnClickListener {
                startCall(tvSecondPhone.text.toString())
            }
        }
    }


    private fun startCall(phone: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CALL_PHONE),
                ContactsFragment.PERMISSION_REQUEST_CODE
            )
        }
    }
}