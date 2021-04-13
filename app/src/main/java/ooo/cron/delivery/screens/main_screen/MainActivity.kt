package ooo.cron.delivery.screens.main_screen

import android.os.Bundle
import ooo.cron.delivery.App
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun injectDependencies() =
        App.appComponent.mainComponentBuilder().build().inject(this)
}