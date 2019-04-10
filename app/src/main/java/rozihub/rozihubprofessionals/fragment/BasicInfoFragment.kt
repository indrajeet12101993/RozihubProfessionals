package rozihub.rozihubprofessionals.fragment


import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.utils.UtiliyMethods
import rozihub.rozihubprofessionals.utils.Validation
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.libraries.places.widget.Autocomplete
import android.content.Intent
import android.view.*


import com.google.android.libraries.places.api.model.Place
import rozihub.rozihubprofessionals.constants.AppConstants.REQUEST_PICK_PLACE
import java.util.*
import com.google.android.libraries.places.widget.AutocompleteActivity
import kotlinx.android.synthetic.main.custom_otp.*

import kotlinx.android.synthetic.main.fragment_basic_info.view.*
import rozihub.rozihubprofessionals.base.RozihubApplicaion

import rozihub.rozihubprofessionals.callBackInterface.ListnerForDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.pojo.ResponseFromServerProffesion1


class BasicInfoFragment : BaseFragment(), ListnerForDialog {
    override fun selctok() {
        mCustomDialog!!.dismiss()
        replaceFragment(BussinessFragment())

    }

    private lateinit var et_name: TextInputEditText
    private lateinit var et_number: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var et_adress: TextInputEditText
    private lateinit var et_city: TextInputEditText
    private lateinit var et_state: TextInputEditText
    private lateinit var et_pin_code: TextInputEditText
    private var mBussinessName: String? = null
    private var mBussinessNumber: String? = null
    private var mBussinesspassword: String? = null
    private var mBussinessemail: String? = null
    private var mBussinessadress: String? = null
    private var mBussinesscity: String? = null
    private var mBussinessstate: String? = null
    private var mBussinesspincode: String? = null
    private var mLat: String? = null
    private var mLong: String? = null
    private var mCustomDialog: Dialog? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic_info, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_name = view.et_name
        et_number = view.et_number
        et_password = view.et_password
        et_email = view.et_email
        et_adress = view.et_adress
        et_city = view.et_city
        et_state = view.et_state
        et_pin_code = view.et_pin_code
        view.tt_pasword.isPasswordVisibilityToggleEnabled=true
        view.tv_next.setOnClickListener {
            if (!validationForUserRegistration()) {
                sendOtp()
            }





        }

        et_adress.setOnClickListener {
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).setCountry("in").build(activity!!)
            startActivityForResult(intent, REQUEST_PICK_PLACE)

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PICK_PLACE) {
            if (resultCode == RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                et_adress.setText(place.name.toString())
                mLat = place.latLng!!.latitude.toString()
                mLong = place.latLng!!.longitude.toString()
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    private fun validationForUserRegistration(): Boolean {

        mBussinessName = et_name.text.toString()
        mBussinessNumber = et_number.text.toString()
        mBussinesspassword = et_password.text.toString()
        mBussinessemail = et_email.text.toString()
        mBussinessadress = et_adress.text.toString()
        mBussinesscity = et_city.text.toString()
        mBussinessstate = et_state.text.toString()
        mBussinesspincode = et_pin_code.text.toString()

        if (Validation.isEmptyField(mBussinessName!!)) {
            et_name.error = getString(R.string.error_no_text)
            et_name.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinessNumber!!)) {
            et_number.error = getString(R.string.error_no_text)
            et_number.requestFocus()
            return true

        }
        if (Validation.isValidPhoneNumber(mBussinessNumber!!)) {
            et_number.error = getString(R.string.valid_input_number)
            et_number.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinesspassword!!)) {
            et_password.error = getString(R.string.error_no_text)
            et_password.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinessemail!!)) {
            et_email.error = getString(R.string.error_no_text)
            et_email.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinessadress!!)) {
            et_adress.error = getString(R.string.error_no_text)
            et_adress.requestFocus()
            return true

        }

        if (Validation.isEmptyField(mBussinesscity!!)) {

            et_city.error = getString(R.string.error_no_text)
            et_city.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinessstate!!)) {

            et_state.error = getString(R.string.error_no_text)
            et_state.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinesspincode!!)) {

            et_pin_code.error = getString(R.string.error_no_text)
            et_pin_code.requestFocus()
            return true

        }





        return false
    }

    private fun startRegistrationForProfession1() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerTransportprofession1(mBussinessName!!,mBussinesspassword!!,mBussinessNumber!!,
                        mBussinessemail!!,mBussinessadress!!,mLat!!,mLong!!,mBussinesscity!!,mBussinessstate!!,mBussinesspincode!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerProffesion1) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(responseFromSerevr.response_code=="1"){
            showDialogWithDismiss(responseFromSerevr.response_message)

        }else{
            dataManager.setUserId(responseFromSerevr.id.toString())
            UtiliyMethods.showDialogwithMessage(activity!!,responseFromSerevr.response_message,this )
        }





    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    //geeting  number for otp
    private fun sendOtp() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserPhoneNumber(mBussinessNumber!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        showCustomDialog()




    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun showCustomDialog() {

        mCustomDialog = Dialog(activity!!)
        val window: Window = mCustomDialog!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialog!!.setCanceledOnTouchOutside(false)
        mCustomDialog!!.setContentView(R.layout.custom_otp)


       mCustomDialog!!.forgtext.text = "Will send you six digit OTP to reset Password"+" "+mBussinessNumber

        mCustomDialog!!.btn_verify.setOnClickListener {

           if(Validation.isEmptyField( mCustomDialog!!.txt_pin_entry.text.toString())){
               showDialogWithDismiss(getString(R.string.valid_otp))
            }

           else{


               verifyOtp(mCustomDialog!!.txt_pin_entry.text.toString())
            }



        }

        mCustomDialog!!.show()

    }

    private fun verifyOtp(otp: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserOtpVerify(mBussinessNumber!!,otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(responseFromSerevr.response_code=="1"){
            showDialogWithDismiss(responseFromSerevr.response_message)
        }else{
            startRegistrationForProfession1()

        }




    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
