package rozihub.rozihubprofessionals.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bussiness_catogory_fragment_show.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.profile.ResponseFromserverProfile


class BussinessCatogoryFragmentShow : BaseFragment() {
    private lateinit var et_booking_per_hour: TextInputEditText
    private lateinit var tv_workingdays: TextView

    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =inflater.inflate(R.layout.fragment_bussiness_catogory_fragment_show, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_booking_per_hour=view.et_booking_per_hour
        tv_workingdays=view.tv_workingdays
        showuserservices()
        return view
    }

    //geeting  profile list
    private fun showuserservices() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServiceProfile(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromserverProfile) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        if(responseFromSerevr.Result.third.booking_per_hour!=null){
            et_booking_per_hour.setText(responseFromSerevr.Result.third.booking_per_hour)
        }
        if(responseFromSerevr.Result.third.booking_opening_days!=null){
            val split:List<String> = responseFromSerevr.Result.third.booking_opening_days.split(',')
            val buffer= StringBuffer()
            if(split.size>0){
                if(split[1].equals("0")){
                    buffer.append("Sunday,")
                    tv_workingdays.text = buffer.toString()
                }
            }

            if(split.size>1){
                if(split[2].equals("1")){
                    buffer.append("Monday,")
                    tv_workingdays.text = buffer.toString()
                }
            }
            if(split.size>2){
                if(split[3].equals("2")){
                    buffer.append("tuesday,")
                    tv_workingdays.text = buffer.toString()
                }
            }
            if(split.size>3){
                if(split[4].equals("3")){
                    buffer.append("Wednesday")
                    tv_workingdays.text = buffer.toString()
                }
            }
            if(split.size>4){
                if(split[5].equals("4")){
                    buffer.append("Thrusday,")
                    tv_workingdays.text = buffer.toString()
                }
            }
            if(split.size>5){
                if(split[6].equals("5")){
                    buffer.append("Friday,")
                    tv_workingdays.text = buffer.toString()
                }
            }
            if(split.size>6){
                if(split[7].equals("6")){
                    buffer.append("Saturday,")
                    tv_workingdays.text = buffer.toString()
                }
            }

            tv_workingdays.text = buffer.toString()
        }










    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


}
