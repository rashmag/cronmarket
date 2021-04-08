package ooo.cron.delivery.screens.main_screen

import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

@MainScope
class MainPresenter @Inject constructor(): BaseMvpPresenter<MainContract.View>(), MainContract.Presenter