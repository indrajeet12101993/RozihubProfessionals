package rozihub.rozihubprofessionals.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_today.view.*
import rozihub.rozihubprofessionals.MyvenueDetailActvity
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.adapter.AllVeneueAdaptervar
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ApiResultDeatiledbooking
import rozihub.rozihubprofessionals.callBackInterface.ListnerForNaviagtionItem
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.detailedBooking.ResponseFromServerDetailedBooking
import rozihub.rozihubprofessionals.pojo.detailedBooking.Today


class TodayFragment : BaseFragment() {


    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var mListBookingToday: List<Today>

    private lateinit var mInterface: ApiResultDeatiledbooking
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_today, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        view.rv_todaybookings.layoutManager = layoutManager
        newDetailedbookingList(object :ApiResultDeatiledbooking, ListnerForNaviagtionItem {
            override fun itemSelcectPosition(position: Int) {
                val mainIntent = Intent(activity, MyvenueDetailActvity::class.java)
                startActivity(mainIntent)
            }

            override fun resultNewBooking(result: ResponseFromServerDetailedBooking) {


                if(result.Result.todayrevenue==0){
                    view.tv_nobookings.text = "No Bookings for today "
                    view.tv_nobookings.visibility = View.VISIBLE

                   // showDialogWithDismiss("No  Bookings  for today")
                }
                else{
                 if(!result.Result.today.isEmpty()){
                     mListBookingToday=result.Result.today
                     val veneuadapter= AllVeneueAdaptervar(result.Result.today,this)
                     view.rv_todaybookings.adapter=veneuadapter
                 }

                }

            }

        })
        return view
    }


    //geeting  detailedBookingList
    private fun newDetailedbookingList(interfacedetailbooking: ApiResultDeatiledbooking) {
        mInterface = interfacedetailbooking
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postDetailBookingList(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerDetailedBooking) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mInterface.resultNewBooking(responseFromSerevr)


    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }



}
