package ooo.cron.delivery.screens.splash_screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.R
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import javax.inject.Inject

/**
 * Created by Ramazan Gadzhikadiev on 10.06.2021.
 */

class SplashScreenPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
    private val analytics: BaseAnalytics
) : BaseMvpPresenter<SplashScreenContract.View>(),
    SplashScreenContract.Presenter {

    override fun onStartView() {
        mainScope.launch {
            withErrorsHandle(
                {
                    if (dataManager.getStableVersion() == STABLE_API_VERSION) {
                        if(dataManager.readFirstOpenApp()){
                            view?.showOnboard()
                            dataManager.writeFirstOpenApp()
                        }else{
                            analytics.trackOpenOnboardScreen(R.string.skip_onboard.toString())
                            if (dataManager.readChosenCity()?.id != null)
                                view?.navigateMainScreen()
                            else
                                view?.navigateFirstAddressScreen()
                        }
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