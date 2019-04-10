package rozihub.rozihubprofessionals.fragment



import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_scrolling.view.*
import kotlinx.android.synthetic.main.fragment_new_booking.view.*
import rozihub.rozihubprofessionals.BookingDetailActivity
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.adapter.AllNewBookingAdapter
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ApiResultnewbookig
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.newBooking.ResponseFromServerNewBookooing
import rozihub.rozihubprofessionals.pojo.newBooking.Result


class NewBookingFragment : BaseFragment() {


    private lateinit var mListNewBooking: List<Result>
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var allNewBookingAdapter: AllNewBookingAdapter
    private lateinit var mApiResultnewbookig: ApiResultnewbookig
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_new_booking, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        val layoutManager: LinearLayoutManager = LinearLayoutManager(activity!!)
        view.rv_new_bookings.layoutManager = layoutManager
        view.rv_new_bookings.isNestedScrollingEnabled=false
        mListNewBooking = ArrayList<Result>()

        newbookingList(object : ApiResultnewbookig, ListnerForCustomDialog {
            override fun selectPosition(position: Int) {

                startintent(mListNewBooking[position])

            }

            override fun resultNewBooking(result: List<Result>) {
                if (result.isEmpty()) {
                    view.tv_nobookings.text = "No New Bookings"
                    view.tv_nobookings.visibility = View.VISIBLE

                    //showDialogWithDismiss("No New Bookings")
                } else {
                    mListNewBooking=result
                    Glide.with(activity!!).load("https://rozihub.com/local/images/subservicephoto/"+result[0].subimage).into(view.iv_lekh_view)
                    allNewBookingAdapter = AllNewBookingAdapter(result, this)
                    view.rv_new_bookings.adapter = allNewBookingAdapter


                }
            }
        })



        return view
    }


    //geeting  servicelist
    private fun newbookingList(apiResultnewbookig: ApiResultnewbookig) {
        mApiResultnewbookig = apiResultnewbookig
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postNewBookingList(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerNewBookooing) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mApiResultnewbookig.resultNewBooking(responseFromSerevr.Result)


    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun startintent(id: Result) {

        val intent = Intent(activity, BookingDetailActivity::class.java)
        intent.putExtra("id",id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        } else {

            startActivity(intent)
        }
    }


}
