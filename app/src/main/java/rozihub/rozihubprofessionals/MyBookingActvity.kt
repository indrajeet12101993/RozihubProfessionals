package rozihub.rozihubprofessionals


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_booking_actvity.*
import rozihub.rozihubprofessionals.adapter.SimpleFragPagerAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.callBackInterface.ApiResultDeatiledbooking
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.detailedBooking.ResponseFromServerDetailedBooking
import androidx.viewpager.widget.ViewPager
import rozihub.rozihubprofessionals.base.RozihubApplicaion


class MyBookingActvity : BaseActivity() {
    private var mSectionsPagerAdapter: SimpleFragPagerAdapter? = null
   // private lateinit var mListNewBooking: List<Result>
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var mResult: ResponseFromServerDetailedBooking

    private lateinit var mInterface: ApiResultDeatiledbooking
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        mSectionsPagerAdapter = SimpleFragPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(container)

        newDetailedbookingList(object :ApiResultDeatiledbooking{
            override fun resultNewBooking(result: ResponseFromServerDetailedBooking) {
                mResult=result
                tv_price.text="Rs"+" "+mResult.Result.todayrevenue.toString()
            }

        })


        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                if(position==0){
                    tv_price.text="Rs"+" "+mResult.Result.todayrevenue.toString()
                }
                if(position==1){
                    tv_price.text="Rs"+" "+mResult.Result.tommorwrevenue.toString()
                }
                if(position==2){
                    tv_price.text="Rs"+" "+mResult.Result.Allrevenue.toString()
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  menuInflater.inflate(R.menu.menu_my_booking_actvity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //geeting  detailedBookingList
    private fun newDetailedbookingList(interfacedetailbooking: ApiResultDeatiledbooking) {
        mInterface = interfacedetailbooking
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postDetailBookingList(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerDetailedBooking) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mInterface.resultNewBooking(responseFromSerevr)


    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


}
