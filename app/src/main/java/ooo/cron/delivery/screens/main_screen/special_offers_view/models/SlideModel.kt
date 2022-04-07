package ooo.cron.delivery.screens.main_screen.special_offers_view.models

import ooo.cron.delivery.screens.main_screen.special_offers_view.constants.ScaleTypes

/**
 * Created by Deniz Coşkun on 6/23/2020.
 * denzcoskun@hotmail.com
 * İstanbul
 */
class SlideModel {

    var imageUrl: String? = null
    var imagePath: Int? = 0
    var title: String? = null
    var scaleType: ScaleTypes? = null
    var id: String? = null
    var partnerId: String? = null

    constructor(imageUrl: String?, id: String? = null, partnerId: String? = null, title: String? = null, scaleType: ScaleTypes?  = null) {
        this.imageUrl = imageUrl
        this.id = id
        this.partnerId = partnerId
        this.title = title
        this.scaleType = scaleType
    }

    constructor(imagePath: Int?, title: String?  = null, scaleType: ScaleTypes?  = null) {
        this.imagePath = imagePath
        this.title = title
        this.scaleType = scaleType
    }

    constructor(imageUrl: String?, scaleType: ScaleTypes?) {
        this.imageUrl = imageUrl
        this.scaleType = scaleType
    }

    constructor(imagePath: Int?, scaleType: ScaleTypes?) {
        this.imagePath = imagePath
        this.scaleType = scaleType
    }

}