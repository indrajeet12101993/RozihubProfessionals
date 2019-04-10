package rozihub.rozihubprofessionals.fragment
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText

import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.viewmodel.SharedViewModel
import java.util.*
import android.app.TimePickerDialog
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.custom_dialog_recycler.*
import kotlinx.android.synthetic.main.fragment_bussiness.view.*


import rozihub.rozihubprofessionals.adapter.CustomDialogRecyclerAdapter
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.callBackInterface.ListnerForDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.ResponseFromServerProffesion1
import rozihub.rozihubprofessionals.utils.UtiliyMethods
import rozihub.rozihubprofessionals.utils.Validation



class BussinessFragment : BaseFragment(), ListnerForDialog, ListnerForCustomDialog {
    override fun selectPosition(position: Int) {
        mCustomDialog!!.dismiss()
        // 1-- for featured dialog
        if(mCustomDialogType=="1"){
            et_featured.setText( mListFeatured!![position])
        }
        if(mCustomDialogType=="2"){
            et_service.setText( mListServices!![position])
        }

    }

    override fun selctok() {
         replaceFragment(BussinessCatogoryFragment())
    }

    var sharedViewModel: SharedViewModel? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    private lateinit var et_busssiness_name: TextInputEditText
    private lateinit var et_description: TextInputEditText
    private lateinit var et_start_time: TextInputEditText
    private lateinit var et_end_time: TextInputEditText
    private lateinit var et_opening_days: TextInputEditText
    private lateinit var et_featured: TextInputEditText
    private lateinit var et_service: TextInputEditText
    private  var mBussinessName:String?=null
    private  var mBussinessDescription:String?=null
    private  var mBussinessStartTime:String?=null
    private  var mBussinessEndTime:String?=null
    private  var mBussinessOpeningDays:String?=null
    private  var mBussinessFeatured:String?=null
    private  var mService:String?=null
    private  var MTimepiker:String?=null
    var mHour: Int? = null
    var mMinute: Int? = null
    private var mCustomDialog: Dialog? = null
    private lateinit var mListFeatured: ArrayList<String>
    private lateinit var mListServices: ArrayList<String>
    private var mCustomDialogType: String?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_bussiness, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_busssiness_name=view.et_busssiness_name
        et_description=view.et_description
        et_opening_days=view.et_opening_days
        et_start_time=view.et_start_time
        et_end_time=view.et_end_time
        et_featured=view.et_featured
        et_service=view.et_service


        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }
        view.tv_next.setOnClickListener {
            if (!validationForUserRegistration()) {
                startRegistrationForProfession2()
            }

          //  replaceFragment(BussinessCatogoryFragment())

        }
        view.tv_previous.setOnClickListener {
            sharedViewModel?.fragmentPositon?.postValue(11)
            replaceFragment(BasicInfoFragment())
        }




        et_start_time.setOnClickListener {
            MTimepiker="1"
            // Get Current Time
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    et_start_time.setText(hourOfDay.toString() + ":" + minute) },
                mHour!!,
                mMinute!!,
                false
            )
            timePickerDialog.show()


        }
        et_end_time.setOnClickListener {

            // Get Current Time
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    et_end_time.setText(hourOfDay.toString() + ":" + minute) },
                mHour!!,
                mMinute!!,
                false
            )
            timePickerDialog.show()


        }
        et_featured.setOnClickListener {

            mListFeatured=ArrayList<String>()
            mListFeatured.add("yes")
            mListFeatured.add("No")
            mCustomDialogType="1"
            showCustomDialog("Select Featured",mListFeatured!!,mCustomDialogType!!)

        }
        et_service.setOnClickListener {
            mListServices=ArrayList<String>()

            for(i in 1..50){
                mListServices.add(i.toString())
            }



            mCustomDialogType="2"
            showCustomDialog("Select Services",mListServices!!,mCustomDialogType!!)

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


    private fun validationForUserRegistration(): Boolean {

        mBussinessName = et_busssiness_name.text.toString()

        mBussinessDescription = et_description.text.toString()
        mBussinessStartTime = et_start_time.text.toString()
        mBussinessEndTime = et_end_time.text.toString()
        mBussinessOpeningDays = et_opening_days.text.toString()
        mBussinessFeatured = et_featured.text.toString()
        mService = et_service.text.toString()

        if (Validation.isEmptyField(mBussinessName!!)) {
            et_busssiness_name.error = getString(R.string.error_no_text)
            et_busssiness_name.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinessDescription!!)) {
            et_description.error = getString(R.string.error_no_text)
            et_description.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinessStartTime!!)) {
            et_start_time.error = getString(R.string.error_no_text)
            et_start_time.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinessEndTime!!)) {
            et_end_time.error = getString(R.string.error_no_text)
            et_end_time.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinessOpeningDays!!)) {
            et_opening_days.error = getString(R.string.error_no_text)
            et_opening_days.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinessFeatured!!)) {

            et_featured.error = getString(R.string.error_no_text)
            et_featured.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mService!!)) {

            et_service.error = getString(R.string.error_no_text)
            et_service.requestFocus()
            return true

        }





        return false
    }


    //geeting  number for profession2
    private fun startRegistrationForProfession2() {

        mBussinessName = et_busssiness_name.text.toString()
        mBussinessDescription = et_description.text.toString()
        mBussinessStartTime = et_start_time.text.toString()
        mBussinessEndTime = et_end_time.text.toString()
        mBussinessOpeningDays = et_opening_days.text.toString()
        mBussinessFeatured = et_featured.text.toString()
        mService = et_service.text.toString()
        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerTransportprofession2(dataManager.getUserId()!!,mBussinessName!!,
                    mBussinessDescription!!,mBussinessStartTime!!,mBussinessEndTime!!,mBussinessOpeningDays!!,mBussinessFeatured!!,mService!!)
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

    private fun showCustomDialog(title:String,list: ArrayList<String>,type:String) {

        mCustomDialog = Dialog(context)
        val window: Window = mCustomDialog!!.getWindow()
         window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialog!!.setCanceledOnTouchOutside(false)
        mCustomDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialog!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        mCustomDialog!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter:CustomDialogRecyclerAdapter= CustomDialogRecyclerAdapter(list,this)
        mCustomDialog!!.custom_recycler.adapter=veneuadapter
        mCustomDialog!!.show()

    }



}
