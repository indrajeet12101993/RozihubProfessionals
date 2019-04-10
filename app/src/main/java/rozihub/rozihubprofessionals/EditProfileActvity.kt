package rozihub.rozihubprofessionals

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_profile_actvity.*
import kotlinx.android.synthetic.main.custom_dialog_recycler.*
import rozihub.rozihubprofessionals.adapter.CustomDialogRecyclerAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.constants.AppConstants
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.profile.ResponseFromserverProfile

import java.util.*


class EditProfileActvity : BaseActivity(), ListnerForCustomDialog {
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

    private var mLat: String? = null
    private var mLong: String? = null
    private var mBussinessName: String? = null
    private var mBussinessemail: String? = null
    private var mBussinessadress: String? = null
    private var mBussinesscity: String? = null
    private var mBussinessstate: String? = null
    private var mBussinesspincode: String? = null
    private  var mBussinessName1:String?=null
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
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Edit Profile"
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        mLat=""
        mLong=""
        mBussinessStartTime=""
        mBussinessEndTime=""
        et_adress.setOnClickListener {
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            ).setCountry("in").build(this)
            startActivityForResult(intent, AppConstants.REQUEST_PICK_PLACE)

        }

        et_start_time.setOnClickListener {
            MTimepiker="1"
            // Get Current Time
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            // Launch Time Picker Dialog
            val timePickerDialog = TimePickerDialog(this,
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
            val timePickerDialog = TimePickerDialog(this,
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
            mListFeatured.add("1")
            mListFeatured.add("2")
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

        tv_next.setOnClickListener {
            mBussinessName = et_name.text.toString()
            mBussinessemail = et_email.text.toString()
            mBussinessadress = et_adress.text.toString()
            mBussinesscity = et_city.text.toString()
            mBussinessstate = et_state.text.toString()
            mBussinesspincode = et_pin_code.text.toString()
            mBussinessName1 = et_busibessname.text.toString()
            mBussinessDescription = et_description.text.toString()
            mBussinessStartTime = et_start_time.text.toString()
            mBussinessEndTime = et_end_time.text.toString()
            mBussinessOpeningDays = et_opening_days.text.toString()
            mBussinessFeatured = et_featured.text.toString()
            mService = et_service.text.toString()

            editprofile()

        }

        showuserservices()
    }

    //edit profile
    private fun editprofile() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .editProfile(dataManager.getUserId()!!,
                    mBussinessName!!, mBussinessemail!!,mBussinessadress!!,mLat!!,mLong!!,mBussinesscity!!,mBussinessstate!!,mBussinesspincode!!,
                    mBussinessName1!!, mBussinessDescription!!,mBussinessStartTime!!,mBussinessEndTime!!,mBussinessOpeningDays!!,mBussinessFeatured!!,mService!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        showDialogWithDismiss(responseFromSerevr.response_message)



    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AppConstants.REQUEST_PICK_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                et_adress.setText(place.name.toString())
                mLat = place.latLng!!.latitude.toString()
                mLong = place.latLng!!.longitude.toString()
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                val status = Autocomplete.getStatusFromIntent(data!!)

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private fun showCustomDialog(title:String,list: ArrayList<String>,type:String) {

        mCustomDialog = Dialog(this)
        val window: Window = mCustomDialog!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialog!!.setCanceledOnTouchOutside(false)
        mCustomDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialog!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this!!)
        mCustomDialog!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter: CustomDialogRecyclerAdapter = CustomDialogRecyclerAdapter(list,this)
        mCustomDialog!!.custom_recycler.adapter=veneuadapter
        mCustomDialog!!.show()

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

        if(responseFromSerevr.Result.first.username!=null){
            et_name.setText(responseFromSerevr.Result.first.username)
        }


        if(responseFromSerevr.Result.first.seller_email!=null) {
            et_email.setText(responseFromSerevr.Result.first.seller_email)
        }
        if(responseFromSerevr.Result.first.address!=null) {
            et_adress.setText(responseFromSerevr.Result.first.address)
        }
        if(responseFromSerevr.Result.first.city!=null) {
            et_city.setText(responseFromSerevr.Result.first.city)
        }
        if(responseFromSerevr.Result.first.state!=null) {
            et_state.setText(responseFromSerevr.Result.first.state)
        }
        if(responseFromSerevr.Result.first.pin_code!=null) {
            et_pin_code.setText(responseFromSerevr.Result.first.pin_code)
        }

        if(responseFromSerevr.Result.second.shop_name!=null){
            et_busibessname.setText(responseFromSerevr.Result.second.shop_name)
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
