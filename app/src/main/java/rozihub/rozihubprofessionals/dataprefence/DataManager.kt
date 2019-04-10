package rozihub.rozihubprofessionals.dataprefence

class DataManager(internal var mSharedPrefsHelper: SharedPrefsHelper) {
    fun clear() {
        mSharedPrefsHelper.clear()
    }



    fun setLoggedIn(isLogin:Boolean) {
        mSharedPrefsHelper.loggedInMode =isLogin
    }

    fun getLoggedIn():Boolean {
        return mSharedPrefsHelper.loggedInMode
    }

    fun setUserId(userId:String) {
        mSharedPrefsHelper.userId =userId
    }

    fun getUserId():String? {
        return mSharedPrefsHelper.userId
    }

    fun setUserDetails(userDetails: String){
        mSharedPrefsHelper.userDetails =userDetails
    }
    fun getUserDetails():String?{
        return mSharedPrefsHelper.userDetails
    }

}