package ooo.cron.delivery.screens.order_history_detail_screen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import javax.inject.Inject
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.OrderHistoryDetailDish
import ooo.cron.delivery.data.network.models.OrderHistoryDetailNetModel
import ooo.cron.delivery.screens.order_history_detail_screen.domain.usecases.LoadOrderHistoryDetailUseCase
import retrofit2.Response
import java.lang.Exception

class OrderHistoryDetailViewModel @Inject constructor(
    private val dataManager: DataManager,
    private val loadOrderHistoryDetailUseCase: LoadOrderHistoryDetailUseCase
) : ViewModel() {

    private val _orderHistoryDetail = MutableLiveData<Response<OrderHistoryDetailNetModel>>()
    val orderHistoryDetail: LiveData<Response<OrderHistoryDetailNetModel>> get() = _orderHistoryDetail

    private var orderHistoryDetailNetModel: OrderHistoryDetailNetModel ?= null

    private val _orderHistoryDetailList = MutableLiveData<List<OrderHistoryDetailDish>>()
    val orderHistoryDetailList: LiveData<List<OrderHistoryDetailDish>> get() = _orderHistoryDetailList

    private val _serverError = MutableLiveData<String>()
    val error: LiveData<String> get() = _serverError

    fun getOrderHistoryDetail(orderId: String){
        viewModelScope.launch {
            try {
                _orderHistoryDetail.value = loadOrderHistoryDetailUseCase.invoke(
                    token = "Bearer ${dataManager.readToken()?.accessToken}",
                    orderId = orderId
                )

                orderHistoryDetailNetModel = _orderHistoryDetail.value?.body()

                _orderHistoryDetailList.value = deserializeDishes()
            }catch (e: Exception){
                _serverError.value = e.message
            }
        }
    }

    private fun deserializeDishes() =
        Gson().fromJson(orderHistoryDetailNetModel?.orderContent, Array<OrderHistoryDetailDish>::class.java)
            .asList()
}