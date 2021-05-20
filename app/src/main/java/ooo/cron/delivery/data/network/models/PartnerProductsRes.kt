package ooo.cron.delivery.data.network.models

/*
 * Created by Muhammad on 08.05.2021
 */



data class PartnerProductsRes(
    var id: String,
    var name: String,
    var cost: Int,
    var photo: String,
    var portionSize: String,
    var description: String,
    var categoryId: String,
    var additives: List<Additives>,
    var nonRequiredAdditives: List<NonRequiredAdditives>,
    var requiredAdditiveGroups: List<RequiredAdditiveGroups>
) {

    data class NonRequiredAdditives(
        var id: String,
        var name: String,
        var cost: Int
    )


    data class RequiredAdditiveGroups(
        var name: String,
        var requiredAdditives: List<RequireAdditives>
    ) {
        data class RequireAdditives(
            var id: String,
            var name: String
        )
    }

    data class Additives(
        var id: String,
        var name: String,
        var cost: Int
    )
}
