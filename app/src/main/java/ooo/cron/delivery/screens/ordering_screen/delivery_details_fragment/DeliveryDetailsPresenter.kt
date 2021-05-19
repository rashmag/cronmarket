package ooo.cron.delivery.screens.ordering_screen.delivery_details_fragment

import kotlinx.coroutines.CoroutineScope
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

/*
 * Created by Muhammad on 19.05.2021
 */



class DeliveryDetailsPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope
) : BaseMvpPresenter<DeliveryDetailsContract.View>(), DeliveryDetailsContract.Presenter {


}