package ooo.cron.delivery.screens.base_mvp

/**
 * Created by Ramazan Gadzhikadiev on 08.04.2021.
 */

interface MvpView

interface MvpPresenter<V : MvpView> {
    fun attachView(view: V)
    fun detachView()
}

abstract class BaseMvpPresenter<V: MvpView>: MvpPresenter<V> {
    protected var view: V? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}