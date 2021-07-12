package ooo.cron.delivery.data.network.models

import com.github.fajaragungpramana.sectionrecyclerview.Section

/*
 * Created by Muhammad on 10.05.2021
 */



 data class RequireAdditiveModel(
    var additiveName: String,
    var additiveList: List<PartnerProductsRes.Additive>
) : Section(additiveName)