package rozihub.rozihubprofessionals

import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_otp.*
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.ResponseFromServerProffesion1
import rozihub.rozihubprofessionals.utils.Validation

class OtpActivity : BaseActivity() {
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        forgtext.text = "Please enter the otp code sent to" + " " + intent.getStringExtra("otp") + " " +
                "You choose Method" + " " + intent.getStringExtra("type")

        btn_verify.setOnClickListener {

            if (Validation.isEmptyField(txt_pin_entry.text.toString())) {
                showDialogWithDismiss(getString(R.string.valid_otp))
            } else {
                if (intent.getStringExtra("type") == "email") {
                    verifyOtp("", intent.getStringExtra("otp"), txt_pin_entry.text.toString())
                } else {
                    verifyOtp(intent.getStringExtra("otp"), "", txt_pin_entry.text.toString())
                }


            }

        }
    }

    private fun verifyOtp(number: String, email: String, otp: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerChangePasswordVerify(number, email, otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromSerevr: ResponseFromServerProffesion1) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if (responseFromSerevr.response_code == "1") {
            showDialogWithDismiss(responseFromSerevr.response_message)
        } else {
            dataManager.setUserId(responseFromSerevr.id.toString())
            launchActivityWithFinish<ResetActivity>()
        }


    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
}
