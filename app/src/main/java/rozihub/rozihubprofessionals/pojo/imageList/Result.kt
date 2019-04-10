package rozihub.rozihubprofessionals.pojo.imageList

import java.io.Serializable

data class Result(
    val id: String,
    val image: String,
    val shop_id: String,
    val user_id: String
):Serializable