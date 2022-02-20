package ooo.cron.delivery.screens.order_history_screen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.OrderHistoryNetModel
import ooo.cron.delivery.screens.order_history_screen.domain.usecases.LoadOrderHistoryUseCase
import retrofit2.Response

class OrderHistoryViewModel @Inject constructor(
    private val dataManager: DataManager,
    private val orderHistoryUseCase: LoadOrderHistoryUseCase
): ViewModel() {

    private val _orderHistoryList = MutableLiveData<Response<List<OrderHistoryNetModel>>>()
    val orderHistoryList: LiveData<Response<List<OrderHistoryNetModel>>> get() = _orderHistoryList

    private val _serverError = MutableLiveData<String>()
    val error: LiveData<String> get() = _serverError

    init {
        loadOrderHistory()
    }

    private fun loadOrderHistory(){
        viewModelScope.launch {
            try {
                _orderHistoryList.value = orderHistoryUseCase.invoke("Bearer ${dataManager.readToken().accessToken}")
            }catch (e: Exception){
                _serverError.value = e.message
            }
        }
    }
}