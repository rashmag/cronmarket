package ooo.cron.delivery.screens.main_screen.special_offers_view.interfaces

import ooo.cron.delivery.screens.main_screen.special_offers_view.constants.ActionTypes


/**
 * Created by Deniz Coşkun on 10/10/2020.
 * denzcoskun@hotmail.com
 * İstanbul
 */
interface TouchListener {
    /**
     * Click listener touched item function.
     *
     * @param  touched  slider boolean
     */
    fun onTouched(touched: ActionTypes)
}