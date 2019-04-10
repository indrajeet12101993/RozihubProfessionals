package rozihub.rozihubprofessionals.pojo.imageList

import java.util.ArrayList

data class ResponseFromGalleryList(
    val response_code: String,
    val response_message: String,
    val result: ArrayList<Result>
)