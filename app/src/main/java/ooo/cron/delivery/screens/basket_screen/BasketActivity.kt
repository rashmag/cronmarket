package ooo.cron.delivery.screens.basket_screen

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_basket.*
import ooo.cron.delivery.App
import ooo.cron.delivery.data.network.models.BasketItem
import ooo.cron.delivery.databinding.ActivityBasketBinding
import ooo.cron.delivery.screens.BaseActivity
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
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvBasketContent.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter.setProducts(createBasketItems())
        binding.rvBasketContent.adapter = adapter

        binding.ivBasketBack.setOnClickListener {
            finish()
        }

        val itemTouchHelper = ItemTouchHelper(SwipeHelper(this) {
            Log.d("asdsad", "touched")
        })
        itemTouchHelper.attachToRecyclerView(rv_basket_content)
    }

    private fun createBasketItems() = listOf(
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,

            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
        BasketItem(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "First",
            10,
            100f,
            Uri.parse("https://www.study.ru/uploads/server/u9W0t6PiqVGBmMAt.jpg")
        ),
    )

}