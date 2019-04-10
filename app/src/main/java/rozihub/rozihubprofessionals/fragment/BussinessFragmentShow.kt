package rozihub.rozihubprofessionals.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bussiness_fragment_show.view.*


import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.profile.ResponseFromserverProfile


class BussinessFragmentShow : BaseFragment() {
    private lateinit var et_busssiness_name: TextInputEditText
    private lateinit var et_description: TextInputEditText
    private lateinit var et_start_time: TextInputEditText
    private lateinit var et_end_time: TextInputEditText
    private lateinit var et_opening_days: TextInputEditText
    private lateinit var et_featured: TextInputEditText
    private lateinit var et_service: TextInputEditText

    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_bussiness_fragment_show, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_busssiness_name=view.et_busssiness_name
        et_description=view.et_description
        et_opening_days=view.et_opening_days
        et_start_time=view.et_start_time
        et_end_time=view.et_end_time
        et_featured=view.et_featured
        et_service=view.et_service
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

        if(responseFromSerevr.Result.second.shop_name!=null){
            et_busssiness_name.setText(responseFromSerevr.Result.second.shop_name)
        }
        if(responseFromSerevr.Result.second.description!=null){
            et_description.setText(responseFromSerevr.Result.second.description)
        }
        if(responseFromSerevr.Result.second.start_time!=null){
            et_start_time.setText(responseFromSerevr.Result.second.start_time)
        }
        if(responseFromSerevr.Result.second.end_time!=null){
            et_end_time.setText(responseFromSerevr.Result.second.end_time)
        }
        if(responseFromSerevr.Result.second.featured!=null){
            et_featured.setText(responseFromSerevr.Result.second.featured)
        }
        if(responseFromSerevr.Result.second.upto_distance!=null){
            et_service.setText(responseFromSerevr.Result.second.upto_distance)
        }
        if(responseFromSerevr.Result.second.adv_booking!=null){
            et_opening_days.setText(responseFromSerevr.Result.second.adv_booking)
        }













    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
