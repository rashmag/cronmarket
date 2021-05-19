package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.App
import ooo.cron.delivery.databinding.FragmentDeliveryDetailsBinding
import ooo.cron.delivery.screens.BaseFragment
import javax.inject.Inject

/*
 * Created by Muhammad on 18.05.2021
 */



class DeliveryDetailsFragment : BaseFragment(), DeliveryDetailsContract.View {

    @Inject
    lateinit var presenter: DeliveryDetailsPresenter

    @Inject
    lateinit var binding: FragmentDeliveryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
    }

    private fun injectDependencies() {
        App.appComponent.deliveryDetailsComponentBuilder()
            .buildInstance(layoutInflater)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onChooseDeliveryTimeClick()
    }

    private fun onChooseDeliveryTimeClick() {
        with(binding) {
            rbInTime.setOnCheckedChangeListener { compoundButton, b ->

                vgChooseDeliveryTime.visibility =
                    if (b) View.VISIBLE else View.GONE
            }
        }
    }
}