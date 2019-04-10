package rozihub.rozihubprofessionals.base

import android.app.Activity
import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.utils.UtiliyMethods

abstract class BaseActivity: AppCompatActivity() {
    lateinit var dialog: ProgressDialog


    protected inline fun <reified T : Activity> Activity.launchActivity() {
        val intent = Intent(this, T::class.java)
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }



    }


    protected inline fun <reified T : Activity> Activity.launchActivityWithFinish() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
        finish()

    }

    protected inline fun <reified T : Activity> Activity.endActivity() {
        val intent = Intent(this, T::class.java)
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;

        startActivity(intent)
        // set an exit transition



    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun hideKeyboard(view: View) {
        UtiliyMethods.hideKeyboard(this, view)
    }
    fun showKeyboard(view: View) {
        UtiliyMethods.showKeyboard(this, view)

    }

    fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(findViewById<View>(android.R.id.content),
            message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView
            .findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.show()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun showDialogLoading() {

        dialog = ProgressDialog(this)
        dialog.setMessage("Please wait...")
        dialog.setTitle("Loading...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.isIndeterminate = true
        if(!dialog.isShowing){
            dialog.show()
        }

    }

    fun hideDialogLoading() {


        if (dialog != null && dialog.isShowing)
            dialog.cancel()


    }
    fun showDialogWithDismiss(message: String) {
        UtiliyMethods.showDialogwithDismiss(this, message)
    }
    protected inline fun <reified T : Activity> Activity.endActivityWithClearTask() {
        val intent = Intent(this, T::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        // set an exit transition



    }



}