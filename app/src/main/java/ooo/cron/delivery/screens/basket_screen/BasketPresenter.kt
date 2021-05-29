package ooo.cron.delivery.screens.basket_screen

import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.05.2021.
 */
class BasketPresenter @Inject constructor() :
    BaseMvpPresenter<BasketContract.View>(),
    BasketContract.Presenter