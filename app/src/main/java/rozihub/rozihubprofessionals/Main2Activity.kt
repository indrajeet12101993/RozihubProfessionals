package rozihub.rozihubprofessionals
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.ResponseFromServerProffesion1
import rozihub.rozihubprofessionals.utils.Validation

class Main2Activity :BaseActivity() {
    private var mBussinesspassword: String? = null
    private var mBussinessemail: String? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()

        newuser.setOnClickListener {
            launchActivityWithFinish<MainActivity>()

        }
        forgot.setOnClickListener {

            launchActivity<ForgotPasswordActvity>()

        }
        loginbutton.setOnClickListener {

            if(!validationForLogin()){
                getLogin()

            }


        }


    }


    private fun validationForLogin(): Boolean {


        mBussinesspassword = et_password.text.toString()
        mBussinessemail = et_email.text.toString()
        if (Validation.isEmptyField(mBussinessemail!!)) {
            et_email.error = getString(R.string.error_no_text)
            et_email.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinesspassword!!)) {
            et_password.error = getString(R.string.error_no_text)
            et_password.requestFocus()
            return true

        }




        return false
    }

    private fun getLogin() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postLogin(mBussinessemail!!,mBussinesspassword!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromSerevr: ResponseFromServerProffesion1) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(responseFromSerevr.response_code=="1"){

            showDialogWithDismiss(responseFromSerevr.response_message)
        }else{

            dataManager.setUserId(responseFromSerevr.id.toString())
            launchActivityWithFinish<UserHomeActvity>()


        }




    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
