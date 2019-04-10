package rozihub.rozihubprofessionals.callBackInterface

import rozihub.rozihubprofessionals.pojo.detailedBooking.ResponseFromServerDetailedBooking

interface ApiResultDeatiledbooking {

    fun resultNewBooking(result: ResponseFromServerDetailedBooking)
}