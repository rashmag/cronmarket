package ooo.cron.delivery.screens.market_category_screen

import androidx.paging.PageKeyedDataSource
import ooo.cron.delivery.data.network.models.Partner

/**
 * Created by Ramazan Gadzhikadiev on 23.04.2021.
 */

class PartnersDataSource constructor(
    val onLoadInitial: (callback: LoadInitialCallback<Int, Partner>) -> Unit,
    val onLoadAfter: (
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Partner>
    ) -> Unit
) :
    PageKeyedDataSource<Int, Partner>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Partner>
    ) {
        onLoadInitial(callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Partner>) {
        onLoadAfter(params, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Partner>) {}

}