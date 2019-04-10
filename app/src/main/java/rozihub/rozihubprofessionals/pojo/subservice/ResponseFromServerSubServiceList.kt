package rozihub.rozihubprofessionals.pojo.subservice

data class ResponseFromServerSubServiceList(
    val Result: List<Result>,
    val response_code: String,
    val response_message: String
)