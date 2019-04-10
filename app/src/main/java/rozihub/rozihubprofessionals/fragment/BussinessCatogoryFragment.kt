package rozihub.rozihubprofessionals.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_dialog_recycler.*
import kotlinx.android.synthetic.main.fragment_bussiness_catogory.view.*
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.UserHomeActvity
import rozihub.rozihubprofessionals.adapter.CustomDialogRecyclerAdapter
import rozihub.rozihubprofessionals.adapter.CustomDiloagServiesListAdapter
import rozihub.rozihubprofessionals.adapter.CustomDiloagServiesSubListAdapter
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.callBackInterface.ListnerForDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.services.ResponseFromServerServiceList
import rozihub.rozihubprofessionals.pojo.services.Result
import rozihub.rozihubprofessionals.pojo.subservice.ResponseFromServerSubServiceList
import rozihub.rozihubprofessionals.utils.UtiliyMethods
import rozihub.rozihubprofessionals.utils.Validation
import rozihub.rozihubprofessionals.viewmodel.SharedViewModel
import java.util.ArrayList


class BussinessCatogoryFragment : BaseFragment(), ListnerForCustomDialog, ListnerForDialog {
    override fun selctok() {
        launchActivityWithFinish(UserHomeActvity())
    }


    override fun selectPosition(position: Int) {
        mCustomDialogServicesList!!.dismiss()
        if(mCustomDialogType=="1"){
            mServiceId=mListServices[position].id
            et_catogory.setText(mListServices[position].name)
        }
        if(mCustomDialogType=="2"){
            mSubServiceId=mListServicesSubList[position].subid
            et_subcatogory.setText(mListServicesSubList[position].subname)
        }
        if(mCustomDialogType=="3"){
            et_booking_per_hour.setText(mListFeatured[position])
        }

    }
    private lateinit var mListServices: List<Result>
    private lateinit var mListServicesSubList: List<rozihub.rozihubprofessionals.pojo.subservice.Result>
    private var mCustomDialogType: String?= null
    private var mServiceId: String?= null
    private var mSubServiceId: String?= null
    private var mBookingPerHour: String?= null
    private var mBookingSeviceName: String?= null
    private var mBookingSubServiceNane: String?= null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    var sharedViewModel: SharedViewModel? = null
    private var mCustomDialogServicesList: Dialog? = null
    private lateinit var et_catogory: TextInputEditText
    private lateinit var et_subcatogory: TextInputEditText
    private lateinit var et_booking_per_hour: TextInputEditText
    private val mStringBufferWorkingDays= StringBuffer()
    private lateinit var mListFeatured: ArrayList<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view=inflater.inflate(R.layout.fragment_bussiness_catogory, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()

        et_catogory=view.et_catogory
        et_subcatogory=view.et_subcatogory
        et_booking_per_hour=view.et_booking_per_hour

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }
        view.tv_next.setOnClickListener {
            if (!validationForUserRegistration()) {
                startRegistrationForProfession3()
            }
        }
        view.tv_previous.setOnClickListener {
            sharedViewModel?.fragmentPositon?.postValue(22)
            replaceFragment(BussinessFragment())
        }


        et_catogory.setOnClickListener {
            servicelist()

        }

        et_subcatogory.setOnClickListener {


            if(mServiceId.isNullOrEmpty()){
                showDialogWithDismiss("please select Catogory First")
            }else{
                servicesublist()
            }


        }

        et_booking_per_hour.setOnClickListener {
            mListFeatured=ArrayList<String>()
            for(i in 5000 downTo 50 step 50){
                mListFeatured.add(i.toString())

            }
            mCustomDialogType="3"
            showCustomDialog(mListFeatured!!,mCustomDialogType!!)
        }

       view.cb_sunday.setOnCheckedChangeListener { buttonView, isChecked ->
           if(isChecked){
               mStringBufferWorkingDays.append(",")
               mStringBufferWorkingDays.append("0")

           }else{
           }

        }

        view.cbmonday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("1")
            }else{ }

        }
        view.cbtuesday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("2")
            }else{
            }

        }

        view.cbwednesday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("3")
            }else{ }

        }
        view.cbThrusday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("4")
            }else{
            }

        }

        view.cbfriday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("5")
            }else{}

        }

        view.cbsaturday.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mStringBufferWorkingDays.append(",")
                mStringBufferWorkingDays.append("6")
            }else{}

        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {


        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    //geeting  servicelist
    private fun servicelist() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getServiceList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_servicesublist, this::handleErrorservicesublist)
        )
    }


    private fun handleResponse_servicesublist(responseFromSerevr: ResponseFromServerServiceList) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mListServices=responseFromSerevr.Result
        mCustomDialogType="1"
        showCustomDialog(mListServices,mCustomDialogType!!)




    }


    private fun handleErrorservicesublist(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }




    //geeting  servicelist
    private fun servicesublist() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServiceList(mServiceId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerSubServiceList) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mListServicesSubList=responseFromSerevr.Result
        mCustomDialogType="2"
        if(mListServicesSubList.isEmpty()){
            showDialogWithDismiss("No Sub Catogory")
        }else{
            showCustomDialogForServiceList(mListServicesSubList,mCustomDialogType!!)
        }





    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }




    private fun showCustomDialog(list: List<Result>, type:String) {

        mCustomDialogServicesList = Dialog(context)
        val window: Window = mCustomDialogServicesList!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogServicesList!!.setCanceledOnTouchOutside(false)
        mCustomDialogServicesList!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogServicesList!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        mCustomDialogServicesList!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter = CustomDiloagServiesListAdapter(list,this)
        mCustomDialogServicesList!!.custom_recycler.adapter=veneuadapter
        mCustomDialogServicesList!!.show()

    }

    private fun showCustomDialogForServiceList(list: List<rozihub.rozihubprofessionals.pojo.subservice.Result>, type:String) {

        mCustomDialogServicesList = Dialog(context)
        val window: Window = mCustomDialogServicesList!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogServicesList!!.setCanceledOnTouchOutside(false)
        mCustomDialogServicesList!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogServicesList!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        mCustomDialogServicesList!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter = CustomDiloagServiesSubListAdapter(list,this)
        mCustomDialogServicesList!!.custom_recycler.adapter=veneuadapter
        mCustomDialogServicesList!!.show()

    }
    private fun showCustomDialog( list: ArrayList<String>, type:String) {

        mCustomDialogServicesList = Dialog(context)
        val window: Window = mCustomDialogServicesList!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogServicesList!!.setCanceledOnTouchOutside(false)
        mCustomDialogServicesList!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogServicesList!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        mCustomDialogServicesList!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter: CustomDialogRecyclerAdapter = CustomDialogRecyclerAdapter(list,this)
        mCustomDialogServicesList!!.custom_recycler.adapter=veneuadapter
        mCustomDialogServicesList!!.show()

    }

    private fun validationForUserRegistration(): Boolean {


        mBookingSeviceName = et_catogory.text.toString()
        mBookingSubServiceNane = et_subcatogory.text.toString()
        mBookingPerHour = et_booking_per_hour.text.toString()

        if (Validation.isEmptyField(mBookingSeviceName!!)) {
            et_catogory.error = getString(R.string.error_no_text)
            et_catogory.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBookingSubServiceNane!!)) {
            et_subcatogory.error = getString(R.string.error_no_text)
            et_subcatogory.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBookingPerHour!!)) {
            et_booking_per_hour.error = getString(R.string.error_no_text)
            et_booking_per_hour.requestFocus()
            return true

        }

        if(mStringBufferWorkingDays.toString().isNullOrEmpty()){
            showDialogWithDismiss("Please Select Business Working Days")
            return true
        }
        return false
    }

    //geeting  number for profession3
    private fun startRegistrationForProfession3() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerTransportprofession3(dataManager.getUserId()!!,mServiceId!!,
                    mSubServiceId!!,mStringBufferWorkingDays.toString()!!,mBookingPerHour!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        UtiliyMethods.showDialogwithMessage(activity!!,responseFromSerevr.response_message,this )



    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
