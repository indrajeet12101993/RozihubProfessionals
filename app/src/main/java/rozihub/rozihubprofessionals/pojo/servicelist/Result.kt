package rozihub.rozihubprofessionals.pojo.servicelist

import java.io.Serializable

data class Result (
    val id: String,
    val price: String,
    val service_id: String,
    val serviceimage: String,
    val servicename: String,
    val shop_id: String,
    val shop_name: String,
    val subservice_id: String,
    val subserviceimage: String,
    val subservicename: String,
    val time: String,
    val user_id: String
):Serializable