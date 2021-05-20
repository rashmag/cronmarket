package ooo.cron.delivery.data.network.models

import com.github.fajaragungpramana.sectionrecyclerview.Section

/*
 * Created by Muhammad on 10.05.2021
 */



 data class ProductCategoryModel(
    var categoryId: String,
    var categoryName: String,
    var productList: List<PartnerProductsRes>
) : Section(categoryName)