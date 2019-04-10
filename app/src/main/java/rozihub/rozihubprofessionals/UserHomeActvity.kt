package rozihub.rozihubprofessionals

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_user_home_actvity.*
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.fragment.NewBookingFragment

class UserHomeActvity : BaseActivity() {

    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_actvity)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        dataManager.setLoggedIn(true)
        replaceFragment(NewBookingFragment(),"New Booking")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun replaceFragment(fragment: Fragment,title:String) {

        supportActionBar!!.title = title
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //  message.setText("No Enquiry")
                replaceFragment(NewBookingFragment(),"New Booking")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                // message.setText(R.string.comingsoon)
                launchActivity<MyBookingActvity>()

                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {
                launchActivity<ProfileActvity>()
                //  message.setText("Upadte profile")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_services -> {

                val mainIntent = Intent(this@UserHomeActvity, AllServiceActvity::class.java)
                startActivity(mainIntent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
