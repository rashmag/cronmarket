package ooo.cron.delivery.screens.pay_dialog_screen

import ooo.cron.delivery.R
import ooo.cron.delivery.screens.InformDialog

class OrderInformDialog(): InformDialog() {
    override val title = getString(R.string.order_inform_attention)
    override val message = getString(R.string.order_inform_delivery_cost_message)
    override val btn_title = getString(R.string.order_inform_attention_btn)
}