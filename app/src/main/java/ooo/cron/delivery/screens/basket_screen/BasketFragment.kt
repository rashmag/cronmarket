package ooo.cron.delivery.screens.basket_screen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.FragmentBasketBinding
import ooo.cron.delivery.screens.BaseMVVMFragment
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.pay_dialog_screen.OrderBottomDialog
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.itemdecoration.SpaceItemDecoration
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 28.12.2021
 * */

class BasketFragment : BaseMVVMFragment() {

    companion object {
        const val MIN_ORDER_AMOUNT_FLAG = "min_order_amount"
        const val EMPTY_TITLE = " "

        fun newInstance(minAmount : Int): BasketFragment {
            val bundle = Bundle().apply {
                putInt(MIN_ORDER_AMOUNT_FLAG, minAmount)
            }
            return BasketFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var binding: FragmentBasketBinding

    @Inject
    lateinit var factory: BasketViewModelFactory.Factory

    override val baseViewModel: BasketViewModel by viewModels {
        factory.create()
    }
    private var basket: Basket? = null
    private var adapter: BasketAdapter? = null
    private var isRestaurant: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    private fun injectDependencies() {
        App.appComponent.basketComponentBuilder()
            .bindInflater(layoutInflater)
            .build()
            .inject(this)
    }

    override fun onStart() {
        super.onStart()
        baseViewModel.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBasketBack.setOnClickListener {
            activity?.finish()
        }
        binding.ivBasketUrn.setOnClickListener {
            showClearBasketDialog()
        }
        binding.btnBasketOrder.setOnClickListener {
            baseViewModel.onMakeOrderClicked(arguments?.getInt(MIN_ORDER_AMOUNT_FLAG).orZero())
        }
        initAdapter()

        baseViewModel.basket.observe(viewLifecycleOwner) {
            basket = it.first
            isRestaurant = it.first.marketCategoryId
            updateBasket(it.second, it.first.cutleryCount)
            val formatter = DecimalFormat("#.##").apply {
                roundingMode = RoundingMode.CEILING
            }
            updateBasketAmount(formatter.format(it.first.amount))
        }

        baseViewModel.basketClearAccept.observe(viewLifecycleOwner) {
            closeBasketScreen()
        }

        baseViewModel.navigationAuth.observe(viewLifecycleOwner) {
            navigateAuthorization()
        }

        baseViewModel.showingMakeOrderDialog.observe(viewLifecycleOwner) {
            showMakeOrderBottomDialog()
        }
        baseViewModel.showingOrderFromDialog.observe(viewLifecycleOwner) {
            arguments?.getInt(MIN_ORDER_AMOUNT_FLAG)?.let { it -> showOrderFromDialog(it) }
        }
    }

    private fun initAdapter() {
        with(binding) {
            rvBasketContent.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvBasketContent.addItemDecoration(
                SpaceItemDecoration(
                    BasketActivity.MARGIN_SPACING_VALUE_34
                )
            )
            adapter = BasketAdapter()
            rvBasketContent.adapter = adapter

            val itemTouchHelper = ItemTouchHelper(SwipeHelper(requireContext()) {
                if (it is BasketAdapter.ProductViewHolder)
                    baseViewModel.onItemRemoveClicked(it.product)
            })
            itemTouchHelper.attachToRecyclerView(rvBasketContent)
        }
    }

    private fun updateBasket(basket: List<BasketDish>, partnersQuantity: Int) {
        isRestaurant?.let {
            adapter?.setProducts(
                basket,
                partnersQuantity,
                it,
                { dish, extraQuantity -> baseViewModel.onPlusClicked(dish, extraQuantity) },
                { dish, unwantedQuantity -> baseViewModel.onMinusClicked(dish, unwantedQuantity) },
                { quantity ->  baseViewModel.onPersonsQuantityEdited(quantity) }
            )
        }
    }

    //Todo сделать материал диалог
    private fun showClearBasketDialog() {
        ClearBasketDialog {
            baseViewModel.onClearBasketAccepted()
        }.show(
            parentFragmentManager,
            ClearBasketDialog::class.simpleName
        )
    }

    private fun showMakeOrderBottomDialog() {
        showBottomDialog(OrderBottomDialog())
    }

    //Метод для показа любого боттом диалога
    private fun showBottomDialog(bottomDialog: BottomSheetDialogFragment) {
        bottomDialog.show(
            parentFragmentManager,
            bottomDialog::class.simpleName
        )
    }

    fun showOrderFromDialog(orderAmount: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(EMPTY_TITLE)
            .setIcon(R.mipmap.ic_launcher)
            .setMessage(getString(R.string.partners_activity_dialog_min_price_title, orderAmount.toString()))
            .setCancelable(false)
            .setPositiveButton(R.string.partners_activity_dialog_btn_ok_title) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun navigateAuthorization() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun updateBasketAmount(price: String) {
        binding.tvBasketAmount.text = getString(R.string.price, price)
        binding.tvBasketAmount.startBottomAnimate(true)
        binding.tvBasketDeliveryCost.startBottomAnimate(true)
    }

    private fun closeBasketScreen() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseViewModel.onPersonsQuantityEdited(0)
    }
}