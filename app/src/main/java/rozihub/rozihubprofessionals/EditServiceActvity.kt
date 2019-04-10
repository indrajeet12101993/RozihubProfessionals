package rozihub.rozihubprofessionals


import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_service_actvity.*
import kotlinx.android.synthetic.main.custom_dialog_recycler.*
import rozihub.rozihubprofessionals.adapter.CustomDiloagServiesListAdapter
import rozihub.rozihubprofessionals.adapter.CustomDiloagServiesSubListAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
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

class EditServiceActvity : BaseActivity() , ListnerForCustomDialog, ListnerForDialog {
    private lateinit var mListServices: List<Result>
    private lateinit var mListServicesSubList: List<rozihub.rozihubprofessionals.pojo.subservice.Result>
    private var mCustomDialogType: String?= null
    private var mServiceId: String?= null
    private var mSubServiceId: String?= null
    private var mListServuceidintent: rozihub.rozihubprofessionals.pojo.servicelist.Result?= null
    private var mServicePrice: String?= null
    private var mServiceTime: String?= null
    private var mBookingSeviceName: String?= null
    private var mBookingSubServiceNane: String?= null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    private var mCustomDialogServicesList: Dialog? = null

    override fun selctok() {
        finish()
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


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=" Edit  Services"

        mListServuceidintent=    intent.getSerializableExtra("id") as rozihub.rozihubprofessionals.pojo.servicelist.Result
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_catogory.setText(mListServuceidintent!!.servicename)
        et_subcatogory.setText(mListServuceidintent!!.subservicename)
        et_price.setText(mListServuceidintent!!.price)
        et_time.setText(mListServuceidintent!!.time)
        mServiceId=mListServuceidintent!!.service_id
        mSubServiceId=mListServuceidintent!!.subservice_id
        tv_next.setOnClickListener {
            if (!validationForUserRegistration()) {
                addServices()
            }
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
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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


    private fun showCustomDialogForServiceList(list: List<rozihub.rozihubprofessionals.pojo.subservice.Result>, type:String) {

        mCustomDialogServicesList = Dialog(this)
        val window: Window = mCustomDialogServicesList!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogServicesList!!.setCanceledOnTouchOutside(false)
        mCustomDialogServicesList!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogServicesList!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mCustomDialogServicesList!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter = CustomDiloagServiesSubListAdapter(list,this)
        mCustomDialogServicesList!!.custom_recycler.adapter=veneuadapter
        mCustomDialogServicesList!!.show()

    }
    private fun showCustomDialog(list: List<Result>, type:String) {

        mCustomDialogServicesList = Dialog(this)
        val window: Window = mCustomDialogServicesList!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogServicesList!!.setCanceledOnTouchOutside(false)
        mCustomDialogServicesList!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogServicesList!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mCustomDialogServicesList!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter = CustomDiloagServiesListAdapter(list,this)
        mCustomDialogServicesList!!.custom_recycler.adapter=veneuadapter
        mCustomDialogServicesList!!.show()

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
            showDialogWithDismiss("please select another Catogory (No Sub catogory found)")
        }else{
            showCustomDialogForServiceList(mListServicesSubList,mCustomDialogType!!)
        }






    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    private fun validationForUserRegistration(): Boolean {


        mBookingSeviceName = et_catogory.text.toString()
        mBookingSubServiceNane = et_subcatogory.text.toString()
        mServicePrice = et_price.text.toString()
        mServiceTime = et_time.text.toString()

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
        if (Validation.isEmptyField(mServicePrice!!)) {
            et_price.error = getString(R.string.error_no_text)
            et_price.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mServiceTime!!)) {
            et_time.error = getString(R.string.error_no_text)
            et_time.requestFocus()
            return true

        }


        return false
    }


    //add services
    private fun addServices() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .addServices(dataManager.getUserId()!!,mServiceId!!,
                    mSubServiceId!!,mServiceTime!!,mServicePrice!!,mListServuceidintent!!.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        UtiliyMethods.showDialogwithMessage(this,responseFromSerevr.response_message,this )



    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
}
