package rozihub.rozihubprofessionals.base

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.libraries.places.api.Places
import rozihub.rozihubprofessionals.constants.AppConstants.API_KEY_PLCES_SDK
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.dataprefence.SharedPrefsHelper

class RozihubApplicaion:MultiDexApplication() {

    companion object {
        lateinit var baseApplicationInstance: RozihubApplicaion
            private set
    }
    lateinit var dataManager: DataManager


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        if (!Places.isInitialized()) {
            Places.initialize(this, API_KEY_PLCES_SDK)
        }


        baseApplicationInstance= this
        val sharedPrefsHelper = SharedPrefsHelper(applicationContext)
        dataManager = DataManager(sharedPrefsHelper)
    }

    fun getdatamanger(): DataManager {

        return dataManager
    }

}