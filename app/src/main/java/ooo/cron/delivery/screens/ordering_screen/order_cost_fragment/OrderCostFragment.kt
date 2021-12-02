package ooo.cron.delivery.screens.ordering_screen.order_cost_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.databinding.FragmentOrderCostBinding
import ooo.cron.delivery.screens.BaseFragment
import javax.inject.Inject

/*
 * Created by Muhammad on 19.05.2021
 */



class OrderCostFragment : BaseFragment(), OrderCostContract.View {

    companion object {
        var AMOUNT = -1.0
        const val KHAS_ID = "2d0c08eb-da25-4afa-8de2-db70a29a9520"
    }

    @Inject
    lateinit var presenter: OrderCostPresenter

    @Inject
    lateinit var binding: FragmentOrderCostBinding

    private var basketModel: Basket? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        presenter.attachView(this)
        super.onCreate(savedInstanceState)
    }

    private fun injectDependencies() {
        App.appComponent.orderCostComponentBuilder()
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
        AMOUNT = requireActivity().intent.getDoubleExtra("AMOUNT", -1.0)
        basketModel = presenter.getBasket()
        binding.tvCostGoods.text = String.format(resources.getString(R.string.price), AMOUNT.toInt())
        val deliveryPrice = resources.getString(R.string.price)
        if (presenter.getDeliveryCityId() == KHAS_ID) {
            binding.tvCostDelivery.text = String.format(resources.getString(R.string.rate_delivery_price))
            binding.tvAllCost.text = String.format(deliveryPrice, AMOUNT.toInt())
        } else {
            binding.tvCostDelivery.text = String.format(deliveryPrice, basketModel!!.deliveryCost.toInt())
            binding.tvAllCost.text = String.format(deliveryPrice,AMOUNT.toInt() + basketModel!!.deliveryCost.toInt() )
        }
    }

    fun getPaymentType() =
        binding.spinnerPaymentMethod.selectedItem.toString()

}