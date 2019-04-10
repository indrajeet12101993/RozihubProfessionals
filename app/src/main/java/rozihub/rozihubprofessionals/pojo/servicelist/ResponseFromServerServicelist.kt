package rozihub.rozihubprofessionals.pojo.servicelist

data class ResponseFromServerServicelist(
    val Result: List<Result>,
    val response_code: String,
    val response_message: String
)