package ooo.cron.delivery.screens.pay_dialog_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.assisted.AssistedInject
import ooo.cron.delivery.data.OrderPrefsRepository
import ooo.cron.delivery.data.OrderRestRepository

/**
 * Created by Maya Nasrueva on 14.12.2021
 * */

class OrderViewModel @AssistedInject constructor(
    private val restRepo: OrderRestRepository,
    private val prefsRepo: OrderPrefsRepository
) : ViewModel() {

    init {
        Log.e("app", "inited")
    }

    private val _commentTextLivedata = MutableLiveData("")
    val commentTextLiveData: LiveData<String> = _commentTextLivedata

    fun setComment(comment: String) {
        _commentTextLivedata.postValue(comment)

    }

}