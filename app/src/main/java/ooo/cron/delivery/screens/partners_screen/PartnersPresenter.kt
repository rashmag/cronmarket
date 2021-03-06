package ooo.cron.delivery.screens.partners_screen

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import ooo.cron.delivery.analytics.BaseAnalytics
import ooo.cron.delivery.utils.extensions.orZero
import ooo.cron.delivery.data.local.PreferenceStorage

/*
 * Created by Muhammad on 05.05.2021
 */

@PartnersScope
class PartnersPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
    private val analytics: BaseAnalytics,
    private val prefs: PreferenceStorage
) :
    BaseMvpPresenter<PartnersContract.View>(), PartnersContract.Presenter {

    private lateinit var categoryRes: List<PartnerCategoryRes.Categories>
    private lateinit var partner: PartnersInfoRes
    private val productCategoriesModel = ArrayList<ProductCategoryModel>()
    private var basket: Basket? = null
    private var basketContent: List<BasketDish>? = null

    private var categoryName = ""

    override fun getPartnerInfo() {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.getPartnersInfo(
                    view?.getPartnerId()!!).handlePartnersInfo() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() })
        }
    }

    fun checkPartnerId(): Boolean {
        return dataManager.readPartnerId() != view?.getPartnerId()
    }

    private fun Response<PartnersInfoRes>.handlePartnersInfo() {
        if (isSuccessful) {
            partner = body()!!
            view?.showPartnerInfo(partner)
            dataManager.readSelectedMarketCategory()?.let {
                analytics.trackOpenPartnersCard(
                    partnerName = partner.name,
                    categoryName = it.categoryName,
                    phoneNumber = dataManager.readUserPhone().toString()
                )
            }
        } else {
            view?.showAnyErrorScreen()
        }
    }

    override fun getPartnerCategory() {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.getPartnerCategory(view?.getPartnerId()!!).handlePartnerCategory() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun Response<PartnerCategoryRes>.handlePartnerCategory() {
        if (isSuccessful) {
            categoryRes = body()?.categories!!
            view?.showPartnerCategory(body()!!)
        } else {
            view?.showAnyErrorScreen()
        }
    }


    override fun getPartnerProducts() {
        mainScope.launch {
            withErrorsHandle(
                {
                    dataManager.getPartnerProducts(view?.getPartnerId()!!).handlePartnerProducts()
                    val basketId = dataManager.readUserBasketId()
                    if (basketId != DataManager.EMPTY_UUID) {
                        dataManager.getBasket(basketId).handleBasket()
                        mergeBasketIntoProducts()
                    } else {
                        basket = null
                        dataManager.removeUserBasketId()
                    }
                    view?.removeProgress()
                    view?.showPartnerProducts(productCategoriesModel)
                    view?.updateBasketPreview(basketContent?.sumBy { it.quantity } ?: 0,
                        String.format("%.2f", basket?.amount ?: 0.00))
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun requiredAddressDialogAccepted() {
        view?.showChangeAddressScreen()
    }

    override fun requiredAddressDialogDeclined() {
        view?.showPartnerProducts(productCategoriesModel)
    }

    override fun onBasketClicked() {
        if ((basket?.amount ?: EMPTY_BASKET) < view?.getMinOrderAmount().orZero()) {
            view?.showOrderFromDialog()
        } else {
            partner.schedule.let { schedule ->
                val openTime =
                    if (schedule.begin.isNotEmpty())
                        schedule.begin.split(':')
                            .map { it.toInt() }
                    else
                        listOf(0, 0, 0)

                val closeTime =
                    if (schedule.end.isNotEmpty())
                        schedule.end.split(':')
                            .map { it.toInt() }
                    else listOf(23, 59, 59)

                dataManager.writePartnerOpenHours(openTime[0])
                dataManager.writePartnerCloseTime(closeTime[0])

                prefs.partnerOpenTime = schedule.begin
                prefs.partnerCloseTime = schedule.end

                view?.navigateBasket(
                    openTime.first(),
                    openTime[1],
                    closeTime.first(),
                    closeTime[1],
                    basket
                )
            }
        }
    }

    override fun productClick(product: PartnerProductsRes) {
        analytics.trackOpenProductCard(
            productName = product.name,
            phoneNumber = dataManager.readUserPhone().toString()
        )
        view?.showProductInfo(product)
    }

    override fun minusClick(
        product: PartnerProductsRes,
        quantity: Int
    ) {
        mainScope.launch {
            if (dataManager.readBuildingAddress().isNullOrEmpty()) {
                view?.showChangeAddressDialog()
                return@launch
            }

            if (partner.marketCategoryId == 1 && basket?.id != null && basket?.id != DataManager.EMPTY_UUID) {
                val reducingProduct = BasketDish(
                    DataManager.EMPTY_UUID,
                    product.id,
                    product.name,
                    quantity,
                    product.cost,
                    product.photo,
                    listOf()
                )

                val basketEditor = BasketEditorReq(
                    basket!!.id,
                    partner.id,
                    partner.marketCategoryId,
                    Gson().toJson(reducingProduct)
                )

                withErrorsHandle(
                    {
                        basket = dataManager.decreaseProductInBasket(basketEditor)
                        dataManager.writeUserBasketId(basket!!.id)
                        basketContent = deserializeDishes()
                        mergeBasketIntoProducts()
                        view?.showPartnerProducts(productCategoriesModel)
                        productCategoriesModel.forEach {
                            categoryName = it.categoryName
                        }
                        view?.updateBasketPreview(basketContent?.sumBy { it.quantity } ?: 0,
                            String.format("%.2f", basket!!.amount))
                    },
                    { view?.showConnectionErrorScreen() },
                    { view?.showAnyErrorScreen() }
                )
            }
        }
    }

    override fun plusClick(
        product: PartnerProductsRes,
        additives: List<BasketDishAdditive>,
        quantity: Int
    ) {
        mainScope.launch {
            if (dataManager.readBuildingAddress().isNullOrEmpty()) {
                view?.showChangeAddressDialog()
                return@launch
            }

            if (basket != null && basket?.amount != EMPTY_BASKET &&
                basket?.partnerId != DataManager.EMPTY_UUID &&
                basket?.partnerId != partner.id
            ) {
                view?.showClearBasketDialog(
                    {
                        view?.showPartnerProducts(productCategoriesModel)
                    },
                    {
                        mainScope.launch {
                            basket = dataManager.clearBasket(BasketClearReq(basket!!.id)).copy(
                                partnerId = DataManager.EMPTY_UUID
                            )
                            dataManager.writeUserBasketId(basket!!.id)
                            dataManager.removeUserBasketId()
                            basketContent = deserializeDishes()
                            mergeBasketIntoProducts()
                            view?.showPartnerProducts(productCategoriesModel)
                            dataManager.readChosenCity()?.id?.let {
                                dataManager.writeCurrentCityId(
                                    it
                                )
                            }
                            view?.updateBasketPreview(
                                basketContent?.sumBy { it.quantity } ?: 0,
                                String.format("%.2f", basket!!.amount)
                            )
                            increaseProductInBasket(product, additives, quantity)
                        }
                    }
                )
                return@launch
            }

            increaseProductInBasket(product, additives, quantity)
        }
    }

    override fun likePartner(partnerId: String) {
        mainScope.launch {
            withErrorsHandle(
                {
                    dataManager.likePartner(
                        partnerId = partnerId
                    )
                        .handleLikeResponse()
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun getUserLoggedStatus(): Boolean {
        return dataManager.readToken()?.accessToken?.isNotEmpty() ?: false
    }

    private fun Response<ResponseBody>.handleLikeResponse() {
        if (isSuccessful) {
            view?.showLikePartner()
        } else view?.showAnyErrorScreen()
    }


    override fun unlikePartner(partnerId: String) {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.unlikePartner(partnerId = partnerId).handleUnlikeResponse() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    private fun Response<ResponseBody>.handleUnlikeResponse() {
        if (isSuccessful) {
            view?.showUnlikePartner()
        } else view?.showAnyErrorScreen()
    }

    private suspend fun increaseProductInBasket(
        product: PartnerProductsRes,
        additives: List<BasketDishAdditive>,
        quantity: Int
    ) {
        var dishId = UUID.randomUUID().toString()

        basketContent?.filter {
            it.productId == product.id
        }?.forEach {
            if (it.dishAdditives.isNullOrEmpty() && additives.isNullOrEmpty() ||
                it.dishAdditives.toSet() == additives.toSet()
            ) {
                dishId = it.id
                return@forEach
            }
        }

        dataManager.writePartnerId(partner.id)

        val addingProduct = BasketDish(
            dishId,
            product.id,
            product.name,
            quantity,
            product.cost,
            product.photo,
            additives
        )
        val basketEditor = BasketEditorReq(
            basket?.id ?: DataManager.EMPTY_UUID,
            partner.id,
            partner.marketCategoryId,
            Gson().toJson(addingProduct)
        )

        withErrorsHandle(
            {
/*                val accessToken = dataManager.readToken().accessToken
*//*                basket = if (accessToken.isNotEmpty())
                    dataManager.increaseProductInBasket("Bearer $accessToken", basketEditor)
                else*/
                basket = dataManager.increaseProductInBasket(basketEditor)
                basket.let { it?.let { it1 ->
                    dataManager.writeUserBasketId(it1.id) }
                }
                basketContent = deserializeDishes()
                mergeBasketIntoProducts()
                view?.showPartnerProducts(productCategoriesModel)
                view?.updateBasketPreview(basketContent?.sumBy { it.quantity } ?: 0,
                    String.format("%.2f", basket!!.amount))
            },
            { view?.showConnectionErrorScreen() },
            { view?.showAnyErrorScreen() }
        )
    }

    private fun Response<List<PartnerProductsRes>>.handlePartnerProducts() {
        if (isSuccessful) {

            val productList = ArrayList<PartnerProductsRes>()
            productCategoriesModel.clear()

            for (category in categoryRes) {
                for (product in body()!!) {
                    if (category.id == product.categoryId) {
                        productList.add(product)
                    }
                }

                productCategoriesModel.add(
                    ProductCategoryModel(
                        category.id,
                        category.name,
                        productList.filterIndexed { _, partnerProductsRes ->
                            partnerProductsRes.categoryId == category.id
                        }
                    )
                )
            }
        } else {
            view?.showAnyErrorScreen()
        }
    }

    private suspend fun Response<Basket>.handleBasket() {
        if (isSuccessful && body() != null &&
            body()!!.id != DataManager.EMPTY_UUID
        ) {
            basket = body()
            basketContent = deserializeDishes()
            if (basket!!.partnerId == partner.id)
                view?.updateBasketPreview(
                    basketContent?.sumBy { it.quantity } ?: 0,
                    String.format("%.2f", body()!!.amount))
        } else {
            basket = null
            basketContent = null
            dataManager.removeUserBasketId()
        }
    }

    private fun mergeBasketIntoProducts() {
        productCategoriesModel.forEach { category ->
            category.productList.forEach { product ->
                findProductQuantityInBasket(product)?.let { quantity ->
                    product.inBasketQuantity = quantity
                } ?: kotlin.run { product.inBasketQuantity = 0 }
            }
        }
    }

    private fun findProductQuantityInBasket(product: PartnerProductsRes) =
        basketContent?.filter {
            product.id == it.productId
        }?.sumBy {
            it.quantity
        }

    private fun deserializeDishes() =
        Gson().fromJson(basket!!.content, Array<BasketDish>::class.java)
            .asList()

    private companion object {
        private const val EMPTY_BASKET = 0.0
        private const val EMPTY = ""
    }
}