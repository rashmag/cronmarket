package ooo.cron.delivery.screens.basket_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import javax.inject.Inject
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.ActivityBasketBinding
import ooo.cron.delivery.screens.base.BaseActivity
import ooo.cron.delivery.screens.base.adapters.AdapterHeader
import ooo.cron.delivery.screens.base.adapters.AdapterSpace
import ooo.cron.delivery.screens.base.adapters.SpacingValue
import ooo.cron.delivery.screens.basket_screen.adapters.AdapterBasket
import ooo.cron.delivery.screens.basket_screen.adapters.AdapterTableware
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.ordering_screen.OrderingActivity
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.extensions.uiLazy
import ooo.cron.delivery.utils.itemdecoration.SpaceItemDecoration

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

class BasketActivity : BaseActivity(), BasketContract.View {

    @Inject
    protected lateinit var presenter: BasketContract.Presenter

    @Inject
    protected lateinit var binding: ActivityBasketBinding

    private val concatAdapter: ConcatAdapter by uiLazy {
        ConcatAdapter()
    }

    private val adapter by uiLazy {
        AdapterBasket(
            plusClick = { dish, extraQuantity ->
                presenter.plusClick(dish, extraQuantity)
            },
            minusClick = { dish, unwantedQuantity ->
                presenter.minusClick(dish, unwantedQuantity)
            }
        )
    }

    private val adapterTableware by uiLazy {
        AdapterTableware(
            onQuantityEditClick = { quantity ->
                presenter.personsQuantityEdited(quantity)
            }
        )
    }

    private var orderAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        val basketModel = intent.getParcelableExtra<Basket>(BASKET_MODEL)
        orderAmount = intent.getIntExtra(MIN_AMOUNT_ORDER, 0)

        App.appComponent.basketComponentBuilder()
            .inflater(layoutInflater)
            .basketModel(basketModel)
            .build()
            .inject(this)

        presenter.attachView(this)
        initActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivBasketBack.setOnClickListener {
            finish()
        }

        binding.ivBasketUrn.setOnClickListener {
            presenter.clearClicked()
        }

        binding.btnBasketOrder.setOnClickListener {
            presenter.clickMakeOrder()
        }

        initAdapter()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    private fun initAdapter() {

        with(concatAdapter) {
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_4))
            addAdapter(AdapterHeader(title = getString(R.string.basket_order_title)))
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_4))
            addAdapter(adapter)
            addAdapter(AdapterSpace(space = SpacingValue.SPACE_16))
            if (presenter.getMarketCategoryId() == RESTAURANT) {
                addAdapter(adapterTableware)
                addAdapter(AdapterSpace(space = SpacingValue.SPACE_16))
            }
        }

        with(binding) {
            rvBasketContent.addItemDecoration(
                SpaceItemDecoration(
                    MARGIN_SPACING_VALUE_34
                )
            )
            rvBasketContent.adapter = concatAdapter

            val itemTouchHelper = ItemTouchHelper(SwipeHelper(this@BasketActivity) {
                if (it is AdapterBasket.ProductViewHolder)
                    presenter.removeItemClicked(it.product)
            })
            itemTouchHelper.attachToRecyclerView(rvBasketContent)
        }
    }

    override fun updateBasket(basket: List<BasketDish>, personsQuantity: Int) {
        adapter.submitList(basket)
        adapterTableware.updateQuantity(personsQuantity)
    }

    override fun showClearBasketDialog() {
        ClearBasketDialog {
            presenter.clearBasketAccepted()
        }.show(
            supportFragmentManager,
            ClearBasketDialog::class.simpleName
        )
    }

    override fun getMinOrderAmount() = orderAmount

    override fun showOrderFromDialog() {
        AlertDialog.Builder(this)
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

    override fun navigateMakeOrderScreen(basket: Basket?) {
        startActivity(
            Intent(this, OrderingActivity::class.java).apply {
                putExtras(intent!!.extras!!)
                putExtra(BASKET_MODEL, basket)
            }
        )
    }

    override fun navigateAuthorization() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun updateBasketAmount(price: String) {
        binding.tvBasketAmount.text = getString(R.string.price, price)
        binding.tvBasketAmount.startBottomAnimate(true)
    }

    override fun close() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.personsQuantityEdited(0)
        presenter.detachView()
    }

    override fun marketCategoryId(): Int {
        return presenter.getMarketCategoryId()
    }

    companion object {
        const val PARTNER_OPEN_HOURS = "PARTNER_OPEN_HOURS"
        const val PARTNER_OPEN_MINUTES = "PARTNER_OPEN_MINUTES"
        const val PARTNER_CLOSE_HOURS = "PARTNER_CLOSE_HOURS"
        const val PARTNER_CLOSE_MINUTES = "PARTNER_CLOSE_MINUTES"
        const val MIN_AMOUNT_ORDER = "MIN_AMOUNT_ORDER"
        const val BASKET_MODEL = "BASKET_MODEL"
        const val EMPTY_TITLE = " "

        const val RESTAURANT = 1

        const val MARGIN_SPACING_VALUE_34 = 34

        private lateinit var activity: Activity
        private fun initActivity(activity: Activity) {
            if (!this::activity.isInitialized || this.activity != activity)
                this.activity = activity
        }

        fun stopActivity() {
            if (::activity.isInitialized) {
                activity.finish()
            }
        }
    }
}