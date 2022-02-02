package ooo.cron.delivery.screens.basket_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.data.network.models.Basket
import ooo.cron.delivery.databinding.ActivityBasketBinding
import ooo.cron.delivery.screens.BaseActivity
import ooo.cron.delivery.screens.pay_dialog_screen.PayClickCallback
import ooo.cron.delivery.screens.payment_status_screen.PaymentStatusFragment
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */

class BasketActivity : BaseActivity(), PayClickCallback {

    @Inject
    protected lateinit var binding: ActivityBasketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val basketModel = intent.getParcelableExtra<Basket>(BASKET_MODEL)
        App.appComponent.basketComponentBuilder()
            .bindInflater(layoutInflater)
            .basketModel(basketModel)
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        openRootFragment(BasketFragment())
    }

    fun openRootFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_basket_fragments, fragment)
            .commit()
    }
    override fun onPayClicked(isSuccessPayment: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_basket_fragments, PaymentStatusFragment.newInstance(isSuccessPayment))
            .addToBackStack("pay_status")
            .commit()
    }

    companion object {
        const val PARTNER_OPEN_HOURS = "PARTNER_OPEN_HOURS"
        const val PARTNER_OPEN_MINUTES = "PARTNER_OPEN_MINUTES"
        const val PARTNER_CLOSE_HOURS = "PARTNER_CLOSE_HOURS"
        const val PARTNER_CLOSE_MINUTES = "PARTNER_CLOSE_MINUTES"
        const val AMOUNT = "AMOUNT"
        const val BASKET_MODEL = "BASKET_MODEL"

        const val MARGIN_SPACING_VALUE_34 = 34
    }
}