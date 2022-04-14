package ooo.cron.delivery.screens.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val _visibleTB = MutableLiveData<Boolean>()
    val visibleTB:LiveData<Boolean> get() = _visibleTB
    val _replaceFragment = MutableLiveData<Boolean>()
    val replaceFragment:LiveData<Boolean> get() = _replaceFragment

    fun onVisibleToolbar(isVisible: Boolean) {
        _visibleTB.value = isVisible
    }
    fun onReplaceFragment() {
        _replaceFragment.value = true
    }
}