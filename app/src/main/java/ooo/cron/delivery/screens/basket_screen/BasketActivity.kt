package ooo.cron.delivery.screens.basket_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_basket.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.databinding.ActivityBasketBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.login_screen.LoginActivity
import ooo.cron.delivery.screens.pay_dialog_screen.OrderBottomDialog
import ooo.cron.delivery.screens.pay_dialog_screen.PayClickCallback
import ooo.cron.delivery.utils.extensions.startBottomAnimate
import ooo.cron.delivery.utils.itemdecoration.SpaceItemDecoration
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

class BasketActivity : BaseActivity(), BasketContract.View, PayClickCallback {

    private val orderBottomDialog = OrderBottomDialog()

    @Inject
    protected lateinit var presenter: BasketContract.Presenter

    @Inject
    protected lateinit var binding: ActivityBasketBinding

    private val adapter by lazy(LazyThreadSafetyMode.NONE){
        BasketAdapter(marketCategoryId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val basketModel = intent.getParcelableExtra<Basket>(BASKET_MODEL)

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

    private fun initAdapter(){
        with(binding) {
            rvBasketContent.layoutManager = LinearLayoutManager(this@BasketActivity, RecyclerView.VERTICAL, false)
            rvBasketContent.addItemDecoration(
                SpaceItemDecoration(
                    MARGIN_SPACING_VALUE_34
                )
            )
            rvBasketContent.adapter = adapter

            val itemTouchHelper = ItemTouchHelper(SwipeHelper(this@BasketActivity) {
                if (it is BasketAdapter.ProductViewHolder)
                    presenter.removeItemClicked(it.product)
            })
            itemTouchHelper.attachToRecyclerView(rvBasketContent)
        }
    }

    override fun updateBasket(basket: List<BasketDish>, personsQuantity: Int) {
        adapter.setProducts(
            basket,
            personsQuantity,
            { dish, extraQuantity -> presenter.plusClick(dish, extraQuantity) },
            { dish, unwantedQuantity -> presenter.minusClick(dish, unwantedQuantity) },
            { presenter.personsQuantityEdited(it) }
        )
    }

    override fun showClearBasketDialog() {
        ClearBasketDialog {
            presenter.clearBasketAccepted()
        }.show(
            supportFragmentManager,
            ClearBasketDialog::class.simpleName
        )
    }

    //Метод для показа ордер боттом диалога(Основной боттом диалог)
    override fun showMakeOrderBottomDialog(basket: Basket?) {
        showBottomDialog(orderBottomDialog)
    }

    //Метод для показа любого боттом диалога
    fun showBottomDialog(bottomDialog: BottomSheetDialogFragment) {
        bottomDialog.show(
            supportFragmentManager,
            bottomDialog::class.simpleName
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
        const val AMOUNT = "AMOUNT"
        const val BASKET_MODEL = "BASKET_MODEL"

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

    override fun onPayClicked(isSuccessPayment: Boolean) {
        if (isSuccessPayment) {
            Toast.makeText(this, "Оплата прошла успешно", Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(this, "Произошла ошибка. Попробуйте повторить", Toast.LENGTH_SHORT).show()

    }
}