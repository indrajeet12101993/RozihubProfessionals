package rozihub.rozihubprofessionals.networking

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.ResponseFromServerProffesion1
import rozihub.rozihubprofessionals.pojo.ResponsePrice
import rozihub.rozihubprofessionals.pojo.detailedBooking.ResponseFromServerDetailedBooking
import rozihub.rozihubprofessionals.pojo.getImage.ResponseFromServerGetImage
import rozihub.rozihubprofessionals.pojo.imageList.ResponseFromGalleryList
import rozihub.rozihubprofessionals.pojo.newBooking.ResponseFromServerNewBookooing
import rozihub.rozihubprofessionals.pojo.profile.ResponseFromserverProfile
import rozihub.rozihubprofessionals.pojo.servicelist.ResponseFromServerServicelist
import rozihub.rozihubprofessionals.pojo.services.ResponseFromServerServiceList
import rozihub.rozihubprofessionals.pojo.subservice.ResponseFromServerSubServiceList

interface ApiRequestEndPoint {

    @FormUrlEncoded
    @POST("Api/sendotp")
    fun postServerUserPhoneNumber(@Field("mob") phone: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Profile")
    fun postServiceProfile(@Field("id") id: String): Observable<ResponseFromserverProfile>


    @FormUrlEncoded
    @POST("Api/Getgalleryimages")
    fun getGallery(@Field("shop_id") id: String): Observable<ResponseFromGalleryList>

    @FormUrlEncoded
    @POST("Api/deletegalleryimages")
    fun deleteImages(@Field("id") id: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Validateotp")
    fun postServerUserOtpVerify(@Field("mob") mob: String,@Field("otp") otp: String): Observable<ResponseFromServerGeneric>


    @FormUrlEncoded
    @POST("Api/checkotp")
    fun postServerChangePasswordVerify(@Field("mobile") mobile: String,@Field("email") email: String,@Field("otp") otp: String): Observable<ResponseFromServerProffesion1>


    @FormUrlEncoded
    @POST("Api/ForgetPassword")
    fun postServerForgetGetOtp(@Field("mobile") mob: String,@Field("email") otp: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Changepassword")
    fun changedPassword(@Field("id") id: String,@Field("password") password: String): Observable<ResponseFromServerGeneric>


    @FormUrlEncoded
    @POST("https://rozihub.com/api/login")
    fun postLogin(@Field("email") mob: String,@Field("password") otp: String): Observable<ResponseFromServerProffesion1>

    @FormUrlEncoded
    @POST("https://rozihub.com/api/addproffesion1")
    fun postServerTransportprofession1(@Field("username") username: String,
                                        @Field("password") password: String,
                                        @Field("mob") mob: String,
                                        @Field("seller_email") seller_email: String,
                                        @Field("address") address: String,
                                        @Field("lat") lat: String,
                                        @Field("longg") longg: String,
                                        @Field("city") city: String,
                                        @Field("state") state: String,
                                        @Field("pin_code") pin_code: String
    ): Observable<ResponseFromServerProffesion1>

    @FormUrlEncoded
    @POST("Api/addprofession2")
    fun postServerTransportprofession2(@Field("id") id: String,
                                       @Field("business_name") business_name: String,
                                       @Field("description") description: String,
                                       @Field("start_time") start_time: String,
                                       @Field("end_time") end_time: String,
                                       @Field("advnce_booking") advnce_booking: String,
                                       @Field("featured") featured: String,
                                       @Field("servicerange") servicerange: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Editprofile")
    fun editProfile(   @Field("shop_id") id: String,
                       @Field("username") username: String,
                       @Field("seller_email") seller_email: String,
                       @Field("address") address: String,
                       @Field("lat") lat: String,
                       @Field("longg") longg: String,
                       @Field("city") city: String,
                       @Field("state") state: String,
                       @Field("pin_code") pin_code: String,
                       @Field("business_name") business_name: String,
                       @Field("description") description: String,
                       @Field("start_time") start_time: String,
                       @Field("end_time") end_time: String,
                       @Field("advnce_booking") advnce_booking: String,
                       @Field("featured") featured: String,
                       @Field("servicerange") servicerange: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/addprofession3")
    fun postServerTransportprofession3(@Field("id") id: String,
                                       @Field("service_id") business_name: String,
                                       @Field("sub_service_id") description: String,
                                       @Field("working_days") start_time: String,
                                       @Field("booking_per_hour") servicerange: String
    ): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/Addsellerservices")
    fun addServices(@Field("shop_id") shop_id: String,
                                       @Field("service_id") service_id: String,
                                       @Field("sub_service_id") sub_service_id: String,
                                       @Field("time") time: String,
                                       @Field("price") price: String,
                                       @Field("id") id: String
    ): Observable<ResponseFromServerGeneric>



    @FormUrlEncoded
    @POST("Api/Subservices")
    fun postServiceList(@Field("id") id: String): Observable<ResponseFromServerSubServiceList>


    @FormUrlEncoded
    @POST("Api/Booking")
    fun postNewBookingList(@Field("shop_id") id: String): Observable<ResponseFromServerNewBookooing>

    @FormUrlEncoded
    @POST("https://rozihub.com/api/payment")
    fun postCreatePayment(@Field("booking_id") booking_id: String,@Field("amount") amount: String): Observable<ResponseFromServerGeneric>

    @FormUrlEncoded
    @POST("Api/pricecal")
    fun priceCalculation(@Field("shop_id") shop_id: String,@Field("hour") hour: String): Observable<ResponsePrice>

    @FormUrlEncoded
    @POST("Api/getProfilepic")
    fun getIamge(@Field("shop_id") id: String): Observable<ResponseFromServerGetImage>

    @FormUrlEncoded
    @POST("Api/Shopservices")
    fun postservicelist(@Field("shop_id") id: String): Observable<ResponseFromServerServicelist>

    @FormUrlEncoded
    @POST("Api/DetailedBooking")
    fun postDetailBookingList(@Field("shop_id") id: String): Observable<ResponseFromServerDetailedBooking>

    @GET("Api/services")
    fun getServiceList(): Observable<ResponseFromServerServiceList>

    @Multipart
    @POST("Api/Updatepic")
    fun uploadImage(@Part image: MultipartBody.Part,
                    @Part("shop_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

    @Multipart
    @POST("Api/Addgallery")
    fun uploadImageForGallery(@Part image: MultipartBody.Part,
                    @Part("shop_id") user_id: RequestBody

    ): Observable<ResponseFromServerGeneric>

}