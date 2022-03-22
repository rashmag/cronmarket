package ooo.cron.delivery.screens.splash_screen

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */

class SplashScreenPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope
) : BaseMvpPresenter<SplashScreenContract.View>(),
    SplashScreenContract.Presenter {

    override fun onStartView() {
        mainScope.launch {
            withErrorsHandle(
                {
                    if (dataManager.getStableVersion() == STABLE_API_VERSION) {
                        if (dataManager.readChosenCity().id.isEmpty())
                            view?.navigateFirstAddressScreen()
                        else
                            view?.navigateMainScreen()
                    } else {
                        view?.showUpdateVersionDialog()
                    }
                },
                {
                    view?.showConnectionErrorScreen()
                },
                {
                    view?.showAnyErrorScreen()
                }
            )
        }
    }

    override fun updateAccepted() {
        view?.navigateGooglePlay()
    }

    override fun updateDeclined() {
        view?.finish()
    }

    companion object {
        const val STABLE_API_VERSION = "7"
    }

}