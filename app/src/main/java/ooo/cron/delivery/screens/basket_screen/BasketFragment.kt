package ooo.cron.delivery.screens.basket_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_basket.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.FragmentBasketBinding
import ooo.cron.delivery.screens.BaseFragment
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.pay_dialog_screen.OrderBottomDialog
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.itemdecoration.SpaceItemDecoration
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Maya Nasrueva on 28.12.2021
 * */

class BasketFragment : BaseFragment() {

    /*@Inject
    lateinit var presenter: BasketContract.Presenter*/

    @Inject
    lateinit var binding: FragmentBasketBinding

    @Inject
    lateinit var factory: BasketViewModelFactory.Factory

    private val viewModel: BasketViewModel by viewModels {
        factory.create()
    }
    private var basket: Basket? = null
    private lateinit var adapter: BasketAdapter
    private var isRestaurant: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        //presenter.attachView(this)
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
        //presenter.onStartView() // Todo убрать presenter +
        viewModel.onStart()
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
            viewModel.onMakeOrderClicked()
        }
        initAdapter()

        viewModel.basket.observe(viewLifecycleOwner, {
            basket = it
            isRestaurant = it.marketCategoryId
            updateBasket(deserializeDishes(basket), basket!!.cutleryCount)
            val formatter = DecimalFormat("#.##").apply {
                roundingMode = RoundingMode.CEILING
            }
            updateBasketAmount(formatter.format(basket!!.amount))

        })

        viewModel.basketClearAccept.observe(viewLifecycleOwner, {
            closeBasketScreen()
        })

        viewModel.navigationAuth.observe(viewLifecycleOwner, {
            navigateAuthorization()
        })

        viewModel.showingMakeOrderDialog.observe(viewLifecycleOwner, {
            showMakeOrderBottomDialog()
        })

        viewModel.connectionErrorScreen.observe(viewLifecycleOwner, {
            showConnectionErrorScreen()
        })

        viewModel.anyErrorScreen.observe(viewLifecycleOwner, {
            showAnyErrorScreen()
        })
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
                //presenter.removeItemClicked(it.product) // Todo убрать presenter +
                    viewModel.onItemRemoveClicked(it.product)
            })
            itemTouchHelper.attachToRecyclerView(rvBasketContent)
        }
    }

    private fun updateBasket(basket: List<BasketDish>, partnersQuantity: Int) {
        adapter.setProducts(
            basket,
            partnersQuantity,
            isRestaurant!!, //?
            { dish, extraQuantity -> viewModel.onPlusClicked(dish, extraQuantity) }, // Todo убрать presenter +
            { dish, unwantedQuantity -> viewModel.onMinusClicked(dish, unwantedQuantity) }, // Todo убрать presenter +
            { viewModel.onPersonsQuantityEdited(it) } // Todo убрать presenter +
        )
    }

    private fun showClearBasketDialog() {
        ClearBasketDialog {
            //presenter.clearBasketAccepted() // Todo убрать presenter +
            viewModel.onClearBasketAccepted()
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

    private fun navigateAuthorization() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun updateBasketAmount(price: String) {
        binding.tvBasketAmount.text = getString(R.string.price, price)
        binding.tvBasketAmount.startBottomAnimate(true)
    }

    private fun closeBasketScreen() {
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.personsQuantityEdited(0) // Todo убрать presenter +
        viewModel.onPersonsQuantityEdited(0)
        //presenter.detachView() // Todo убрать presenter +
    }

    private fun deserializeDishes(basket: Basket?) =
        Gson().fromJson(basket?.content, Array<BasketDish>::class.java)
            .asList()
}