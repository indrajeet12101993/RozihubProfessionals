package rozihub.rozihubprofessionals

import android.content.Intent

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password_actvity.*
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.utils.Validation

class ForgotPasswordActvity : BaseActivity() {
    private var mBussinessmpbile: String? = null
    private var mBussinessmpbileType: String? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_actvity)

        reset.setOnClickListener {

            if(!validationForForgetPassword()){
                if(Validation.isValidEmail(mBussinessmpbile!!)){
                    mBussinessmpbileType="email"
                    if(!Validation.isValidEmail(mBussinessmpbile!!)){
                        showDialogWithDismiss(getString(R.string.valid))
                    }else{

                        getOtp("",mBussinessmpbile!!)
                    }



                }else{
                    mBussinessmpbileType="phone"
                    if(!Validation.isValidPhoneNumber(mBussinessmpbile!!)){
                        getOtp(mBussinessmpbile!!,"")
                    }else{
                        showDialogWithDismiss(getString(R.string.valid))
                    }

                }




            }

        }
    }

    private fun validationForForgetPassword(): Boolean {
        mBussinessmpbile = et_forgetmobile.text.toString()

        if (Validation.isEmptyField(mBussinessmpbile!!)) {
            et_forgetmobile.error = getString(R.string.error_no_text)
            et_forgetmobile.requestFocus()
            return true

        }
        return false

    }

    private fun getOtp(mobile: String,email:String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerForgetGetOtp(mobile,email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(responseFromSerevr.response_code =="1"){

            showDialogWithDismiss(responseFromSerevr.response_message)
        }else{
            val intent = Intent(this@ForgotPasswordActvity, OtpActivity::class.java)
            intent.putExtra("otp",mBussinessmpbile)
            intent.putExtra("type",mBussinessmpbileType)
            startActivity(intent)
            finish()
        }



    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
