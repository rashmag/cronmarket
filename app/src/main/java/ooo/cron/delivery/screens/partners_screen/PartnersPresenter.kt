package ooo.cron.delivery.screens.partners_screen

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.data.network.request.BasketClearReq
import ooo.cron.delivery.data.network.request.BasketEditorReq
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import java.util.*
import javax.inject.Inject

/*
 * Created by Muhammad on 05.05.2021
 */

@PartnersScope
class PartnersPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val mainScope: CoroutineScope,
) :
    BaseMvpPresenter<PartnersContract.View>(), PartnersContract.Presenter {

    private lateinit var categoryRes: List<PartnerCategoryRes.Categories>
    private lateinit var partner: PartnersInfoRes
    private val productCategoriesModel = ArrayList<ProductCategoryModel>()
    private var basket: Basket? = null
    private var basketContent: List<BasketDish>? = null

    override fun getPartnerInfo() {
        mainScope.launch {
            withErrorsHandle(
                { dataManager.getPartnersInfo(view?.getPartnerId()!!).handlePartnersInfo() },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() })
        }
    }


    private fun Response<PartnersInfoRes>.handlePartnersInfo() {
        if (isSuccessful) {
            partner = body()!!
            view?.showPartnerInfo(body()!!)
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
                    val basketId = dataManager.readUserBasket()
                    if (basketId != DataManager.EMPTY_UUID) {
                        dataManager.getBasket(basketId).handleBasket()
                        mergeBasketIntoProducts()
                    } else {
                        basket = null
                    }
                    view?.removeProgress()
                    view?.showPartnerProducts(productCategoriesModel)
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun minusClick(
        product: PartnerProductsRes,
        quantity: Int
    ) {
        mainScope.launch {
            if (dataManager.readBuildingAddress().isNullOrEmpty()) {
                view?.showChangeAddress()
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
                        dataManager.writeUserBasket(basket!!.id)
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
        }
    }

    override fun plusClick(
        product: PartnerProductsRes,
        additives: List<BasketDishAdditive>,
        quantity: Int
    ) {
        mainScope.launch {
            if (dataManager.readBuildingAddress().isNullOrEmpty()) {
                view?.showChangeAddress()
                return@launch
            }

            if (basket != null &&
                basket!!.partnerId != DataManager.EMPTY_UUID &&
                basket!!.partnerId != partner.id
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
                            dataManager.writeUserBasket(basket!!.id)
                            dataManager.removeUserBasket()
                            basketContent = deserializeDishes()
                            mergeBasketIntoProducts()
                            view?.showPartnerProducts(productCategoriesModel)
                            view?.updateBasketPreview(
                                basketContent?.sumBy { it.quantity } ?: 0,
                                String.format("%.2f", basket!!.amount)
                            )
                        }
                    }
                )
                return@launch
            }

            if (partner.marketCategoryId == 1) {
                var dishId = UUID.randomUUID().toString()
                basketContent?.forEach { basketDish ->
                    var isSameDish = basketDish.productId == product.id
                    basketDish.dishAdditives.forEach { basketAdditive ->
                        additives.forEach { selectedAdditive ->
                            isSameDish = basketAdditive.id == selectedAdditive.id
                        }
                    }
                    if (isSameDish) {
                        dishId = basketDish.id
                    }
                }

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
                        val accessToken = dataManager.readToken().accessToken
                        basket = if (accessToken.isNotEmpty())
                            dataManager.increaseProductInBasket("Bearer $accessToken", basketEditor)
                        else
                            dataManager.increaseProductInBasket(basketEditor)
                        dataManager.writeUserBasket(basket!!.id)
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
        }
    }

    private fun Response<List<PartnerProductsRes>>.handlePartnerProducts() {
        if (isSuccessful) {

            val productList = ArrayList<PartnerProductsRes>()
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

    private fun Response<Basket>.handleBasket() {
        if (isSuccessful && body() != null &&
            body()!!.id != DataManager.EMPTY_UUID
        ) {
            basket = body()
            if (basket?.marketCategoryId == 1) {
                basketContent = deserializeDishes()
            }
            if (basket!!.partnerId == partner.id)
                view?.updateBasketPreview(
                    basketContent?.sumBy { it.quantity } ?: 0,
                    String.format("%.2f", body()!!.amount))
        } else {
            basket = null
            basketContent = null
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
}