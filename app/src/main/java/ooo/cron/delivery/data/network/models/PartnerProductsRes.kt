package ooo.cron.delivery.data.network.models

import com.github.fajaragungpramana.sectionrecyclerview.Section

/*
 * Created by Muhammad on 08.05.2021
 */



data class PartnerProductsRes(
    var id: String,
    var name: String,
    var cost: Double,
    var photo: String,
    var portionSize: String,
    var description: String,
    var categoryId: String,
    var additives: List<Additive>,
    var requiredAdditiveGroups: List<RequiredAdditiveGroup>,
    var inBasketQuantity: Int = 0
) {

    data class RequiredAdditiveGroup(
        var name: String,
        var additives: List<Additive>
    ) : Section(name)

    data class Additive(
        var id: String,
        var name: String,
        var cost: Int,
        var isChecked: Boolean
    )
}
