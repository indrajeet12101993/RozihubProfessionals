package rozihub.rozihubprofessionals


import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.constants.AppConstants.SPLASH_DELAY
import rozihub.rozihubprofessionals.dataprefence.DataManager

class SplashActvity : BaseActivity(){
    private var mDelayHandler: Handler? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_actvity)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if(dataManager.getLoggedIn()){

                launchActivityWithFinish<UserHomeActvity>()

            }else{
                launchActivityWithFinish<Main2Activity>()
            }



        }
    }

    override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onBackPressed()
    }
}
