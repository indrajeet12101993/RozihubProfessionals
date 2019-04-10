package rozihub.rozihubprofessionals.pojo.detailedBooking

data class Result(
    val All: List<All>,
    val Allrevenue: Int,
    val today: List<Today>,
    val todayrevenue: Int,
    val tommorow: List<Tommorow>,
    val tommorwrevenue: Int
)