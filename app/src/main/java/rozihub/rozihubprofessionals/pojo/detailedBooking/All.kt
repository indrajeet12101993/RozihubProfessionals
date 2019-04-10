package rozihub.rozihubprofessionals.pojo.detailedBooking

data class All(
    val book_id: String,
    val booking_address: String,
    val booking_city: String,
    val booking_date: String,
    val booking_pincode: String,
    val booking_time: String,
    val curr_date: String,
    val currency: String,
    val fname: String,
    val lname: String,
    val name: String,
    val payment_link: Any,
    val payment_mode: String,
    val phone: String,
    val servicename: String,
    val services_id: String,
    val shop_id: String,
    val shop_name: String,
    val status: String,
    val token: String,
    val total_amt: String,
    val txnid: Any,
    val txnid_final: Any,
    val user_email: String,
    val user_id: String
)