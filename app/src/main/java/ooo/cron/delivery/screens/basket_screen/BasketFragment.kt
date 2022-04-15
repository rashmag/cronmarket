package ooo.cron.delivery.screens.basket_screen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.FragmentBasketBinding
import ooo.cron.delivery.screens.base.BaseMVVMFragment
import ooo.cron.delivery.screens.base.adapters.AdapterHeader
import ooo.cron.delivery.screens.base.adapters.AdapterSpace
import ooo.cron.delivery.screens.base.adapters.SpacingValue
import ooo.cron.delivery.screens.basket_screen.BasketActivity.Companion.MARGIN_SPACING_VALUE_34
import ooo.cron.delivery.screens.basket_screen.adapters.AdapterBasket
import ooo.cron.delivery.screens.basket_screen.adapters.AdapterTableware
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.pay_dialog_screen.OrderBottomDialog
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.extensions.uiLazy
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
        const val RESTAURANT = 1
        const val ADDRESS = "ADDRESS"
        const val IS_OPEN = "IS_OPEN"

        fun newInstance(
            minAmount : Int,
            address: String ?= "",
            isOpen: Boolean
        ): BasketFragment {
            val bundle = Bundle().apply {
                putInt(MIN_ORDER_AMOUNT_FLAG, minAmount)
                putString(ADDRESS, address)
                putBoolean(IS_OPEN, isOpen)
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

    private val concatAdapter: ConcatAdapter by uiLazy {
        ConcatAdapter()
    }

    private val adapter by uiLazy {
        AdapterBasket(
            plusClick = { dish, extraQuantity ->
                baseViewModel.onPlusClicked(dish, extraQuantity)
            },
            minusClick = { dish, unwantedQuantity ->
                baseViewModel.onMinusClicked(dish, unwantedQuantity)
            }
        )
    }

    private val adapterTableware by uiLazy {
        AdapterTableware(
            onQuantityEditClick = { quantity ->
                baseViewModel.onPersonsQuantityEdited(quantity)
            }
        )
    }
    private var basket: Basket? = null
    private var isRestaurant: Int? = null

    private val address: String? by uiLazy {
        requireArguments().getString(ADDRESS)
    }

    private val isOpen: Boolean by uiLazy {
        requireArguments().getBoolean(IS_OPEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        if(address?.isNotEmpty() == true){
            showMakeOrderBottomDialog()
        }
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
            Log.d("basket", basket.toString())
            isRestaurant = it.first.marketCategoryId
            updateBasket(it.second, it.first.cutleryCount)
            val formatter = DecimalFormat("#.##").apply {
                roundingMode = RoundingMode.CEILING
            }
            updateBasketAmount(formatter.format(it.first.amount+it.first.deliveryCost), it.first.deliveryCost.toInt().toString())
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
        baseViewModel.marketCategoryId.observe(viewLifecycleOwner) {
            if (it == RESTAURANT) {
                with(concatAdapter) {
                    addAdapter(adapterTableware)
                    addAdapter(AdapterSpace(space = SpacingValue.SPACE_16))
                }
            }
        }
    }

    private fun initAdapter() {
        with(concatAdapter) {
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_4))
            addAdapter(AdapterHeader(title = getString(R.string.basket_order_title)))
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_4))
            addAdapter(adapter)
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_16))
            baseViewModel.onAdapterInited()
        }

        with(binding) {
            rvBasketContent.addItemDecoration(
                SpaceItemDecoration(
                    MARGIN_SPACING_VALUE_34
                )
            )
            rvBasketContent.adapter = concatAdapter

            val itemTouchHelper = ItemTouchHelper(SwipeHelper(requireContext()) {
                if (it is AdapterBasket.ProductViewHolder)
                    baseViewModel.onItemRemoveClicked(it.product)
            })
            itemTouchHelper.attachToRecyclerView(rvBasketContent)
        }
    }

    private fun updateBasket(basket: List<BasketDish>, personsQuantity: Int) {
        adapter.submitList(basket)
        adapterTableware.updateQuantity(personsQuantity)
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
        // Если мы вернулись с экрана "Выбор города", то на поп-апе оплаты не показывай вначале "Выбор времени доставки"
        if(address?.isNotEmpty() == true){
            showBottomDialog(OrderBottomDialog.newInstance(isOpen, false))
        }else{
            showBottomDialog(OrderBottomDialog.newInstance(isOpen, true))
        }
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

    private fun updateBasketAmount(price: String, deliveryCost: String) {
        binding.tvBasketAmount.text = getString(R.string.price, price)
        binding.tvBasketAmount.startBottomAnimate(true)
        binding.tvBasketDeliveryCost.text = getString(R.string.delivery_cost, deliveryCost)
    }

    private fun closeBasketScreen() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseViewModel.onPersonsQuantityEdited(0)
    }
}