package ooo.cron.delivery.screens.partners_screen

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ooo.cron.delivery.data.DataManager
import ooo.cron.delivery.data.network.models.*
import ooo.cron.delivery.screens.base_mvp.BaseMvpPresenter
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.math.round

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
                    dataManager.getBasket(dataManager.readUserBasket()).handleBasket()

                    productCategoriesModel.forEach { category ->
                        category.productList.forEach { product ->
                            findProductQuantityInBasket(product)?.let { quantity ->
                                product.inBasketQuantity = quantity
                            } ?: kotlin.run { product.inBasketQuantity = 0 }
                        }
                    }
                    view?.showPartnerProducts(productCategoriesModel)
                },
                { view?.showConnectionErrorScreen() },
                { view?.showAnyErrorScreen() }
            )
        }
    }

    override fun priceClick(product: PartnerProductsRes, position: Int) {
        TODO("Not yet implemented")
    }

    override fun minusClick(product: PartnerProductsRes, position: Int) {
        TODO("Not yet implemented")
    }

    override fun plusClick(product: PartnerProductsRes, position: Int) {
        TODO("Not yet implemented")
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
            body()!!.id != DataManager.EMPTY_UUID &&
            body()!!.partnerId == view?.getPartnerId()
        ) {
            basket = body()
            if (basket?.marketCategoryId == 1) {
                basketContent = deserializeDishes()
            }
            view?.showBasketPreview(
                body()!!.id,
                basketContent?.sumBy { it.quantity } ?: 0,
                String.format("%.2f", body()!!.amount))
        } else {
            basket = null
            basketContent = null
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