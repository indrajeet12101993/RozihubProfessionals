package rozihub.rozihubprofessionals


import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reset.*
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.utils.Validation

class ResetActivity : BaseActivity() {
    lateinit var dataManager: DataManager
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mNewPassword: String? = null
    private var mConfirmPassword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()

        update.setOnClickListener{

            if(!validationForForgetPassword()){
                resetPassword()
            }

        }

    }


    private fun resetPassword() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .changedPassword(dataManager.getUserId()!!,mConfirmPassword!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        endActivityWithClearTask<Main2Activity>()

    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    private fun validationForForgetPassword(): Boolean {
        mNewPassword = et_newpassword.text.toString()
        mConfirmPassword = et_confirmpassword.text.toString()

        if (Validation.isEmptyField(mNewPassword!!)) {
            et_newpassword.error = getString(R.string.error_no_text)
            et_newpassword.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mConfirmPassword!!)) {
            et_confirmpassword.error = getString(R.string.error_no_text)
            et_confirmpassword.requestFocus()
            return true

        }

        if(mNewPassword!= mConfirmPassword){

            showDialogWithDismiss("Password Dont match")
            return true
        }
        return false

    }
}
