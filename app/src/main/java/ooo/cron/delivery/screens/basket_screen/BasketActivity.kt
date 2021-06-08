package ooo.cron.delivery.screens.basket_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_basket.*
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.BasketDish
import ooo.cron.delivery.data.network.models.BasketItem
import ooo.cron.delivery.databinding.ActivityBasketBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.ordering_screen.OrderingActivity
import java.util.*
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

class BasketActivity : BaseActivity(), BasketContract.View {

    @Inject
    protected lateinit var presenter: BasketContract.Presenter

    @Inject
    protected lateinit var adapter: BasketAdapter

    @Inject
    protected lateinit var binding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.basketComponentBuilder()
            .inflater(layoutInflater)
            .build()
            .inject(this)
        presenter.attachView(this)
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

        val itemTouchHelper = ItemTouchHelper(SwipeHelper(this) { it ->
            if (it is BasketAdapter.ProductViewHolder)
                presenter.removeItemClicked(it.product)
        })
        itemTouchHelper.attachToRecyclerView(rv_basket_content)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStartView()
    }

    override fun updateBasket(basket: List<BasketDish>, personsQuantity: Int) {
        binding.rvBasketContent.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter.setProducts(
            basket,
            personsQuantity,
            { dish, extraQuantity -> presenter.plusClick(dish, extraQuantity) },
            { dish, unwantedQuantity -> presenter.minusClick(dish, unwantedQuantity) },
            { presenter.personsQuantityEdited(it) }
        )
        binding.rvBasketContent.adapter = adapter
    }

    override fun showClearBasketDialog() {
        ClearBasketDialog {
            presenter.clearBasketAccepted()
        }.show(
            supportFragmentManager,
            ClearBasketDialog::class.simpleName
        )
    }

    override fun navigateMakeOrderScreen() {
        startActivity(
            Intent(this, OrderingActivity::class.java)
                .putExtras(intent!!.extras!!)
        )
    }

    override fun updateBasketAmount(price: String) {
        binding.tvBasketAmount.text = getString(R.string.price, price)
    }

    override fun close() {
        finish()
    }

    companion object {
        const val PARTNER_OPEN_HOURS = "PARTNER_OPEN_HOURS"
        const val PARTNER_OPEN_MINUTES = "PARTNER_OPEN_MINUTES"
        const val PARTNER_CLOSE_HOURS = "PARTNER_CLOSE_HOURS"
        const val PARTNER_CLOSE_MINUTES = "PARTNER_CLOSE_MINUTES"
    }
}