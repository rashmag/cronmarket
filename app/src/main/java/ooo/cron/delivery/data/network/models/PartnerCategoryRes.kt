package ooo.cron.delivery.data.network.models

/*
 * Created by Muhammad on 08.05.2021
 */



data class PartnerCategoryRes(
    var categories: List<Categories>,
    var validationErrors: ValidationErrors
) {
    data class Categories(
        var id: String,
        var name: String,
        var img: String
    )

    data class ValidationErrors(
        var fieldName: String,
        var errorMessages: List<String>
    )
}