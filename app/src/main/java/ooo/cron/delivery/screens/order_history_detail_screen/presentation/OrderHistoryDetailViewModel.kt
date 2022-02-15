package ooo.cron.delivery.screens.order_history_detail_screen.presentation

import android.util.Log
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

    var orderId = ""

    init {
        getOrderHistoryDetail(orderId)
    }

    fun getOrderHistoryDetail(orderId: String){
        viewModelScope.launch {
            try {
                _orderHistoryDetail.value = loadOrderHistoryDetailUseCase.getOrderHistoryDetail(
                    token = "Bearer ${dataManager.readToken().accessToken}",
                    orderId = orderId
                )

                orderHistoryDetailNetModel = _orderHistoryDetail.value?.body()

                Log.d("MYLIST", deserializeDishes().toString())

                _orderHistoryDetailList.value = deserializeDishes()
            }catch (e: Exception){
                Log.d("HELLOHELLO", e.message.toString())
            }
        }
    }

    private fun deserializeDishes() =
        Gson().fromJson(orderHistoryDetailNetModel?.orderContent, Array<OrderHistoryDetailDish>::class.java)
            .asList()
}