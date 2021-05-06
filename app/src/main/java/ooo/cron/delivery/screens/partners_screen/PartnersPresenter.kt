package ooo.cron.delivery.screens.partners_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.PartnersInfoRes
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import javax.inject.Inject

/*
 * Created by Muhammad on 05.05.2021
 */

@PartnersScope
class PartnersPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
) :
    BaseMvpPresenter<PartnersContract.View>(), PartnersContract.Presenter {


    override fun getPartnerInfo() {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.getPartnersInfo(view?.getPartnerId()!!).handlePartnersInfo() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() })
        }
    }


    private fun Response<PartnersInfoRes>.handlePartnersInfo() {
        if (isSuccessful) {
            view?.showPartnerInfo(body()!!)
        } else {
            view?.showAnyErrorScreen()
        }
    }
}