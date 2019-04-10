package rozihub.rozihubprofessionals.pojo.services

data class ResponseFromServerServiceList(
    val Result: List<Result>,
    val response_code: String,
    val response_message: String
)