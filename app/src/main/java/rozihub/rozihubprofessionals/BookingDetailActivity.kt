package rozihub.rozihubprofessionals

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_booking_detail.*
import kotlinx.android.synthetic.main.custom_dialog_payment.*
import kotlinx.android.synthetic.main.custom_dialog_recycler.*
import rozihub.rozihubprofessionals.adapter.CustomDialogRecyclerAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.callBackInterface.ListnerForDialog
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.ResponsePrice
import rozihub.rozihubprofessionals.pojo.newBooking.Result
import rozihub.rozihubprofessionals.utils.UtiliyMethods
import java.util.ArrayList

class BookingDetailActivity : BaseActivity(), ListnerForCustomDialog, ListnerForDialog {
    override fun selctok() {
        mCustomDialogPayment!!.dismiss()
    }

    override fun selectPosition(position: Int) {
        mCustomDialog!!.dismiss()
        // 1-- for featured dialog
        if(mCustomDialogType=="1"){
            mCustomDialogPayment!!. et_featured.setText( mListFeatured!![position])

        }
        if(mCustomDialogType=="2"){
            mCustomDialogPayment!!. et_service.setText( mListServices!![position])
            getPrice( mListServices!![position])
        }
        if(mCustomDialogType=="3"){
            mCustomDialogPayment!!. et_payment_mode.setText( mListFeatured!![position])
        }
    }
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mListServuceidintent: Result? = null
    private var mCustomDialogPayment: Dialog? = null
    private lateinit var mListServices: ArrayList<String>
    private lateinit var mListFeatured: ArrayList<String>
    private var mCustomDialogType: String?= null
    private var mViewType: String?= null
    private var mAmount: Int?= null
    private var mCustomDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Booking Details"
        mListServuceidintent = intent.getSerializableExtra("id") as Result

        tv_bookingid.text = mListServuceidintent!!.book_id
        tv_bookingdate.text = mListServuceidintent!!.booking_date
        tv_booker_user_name.text =mListServuceidintent!!.name
        tv_booker_user_mobile.text = mListServuceidintent!!.phone
        tv_servicename.text = mListServuceidintent!!.servicename
        tv_adress.text = mListServuceidintent!!.booking_address

        tv_cancel.setOnClickListener {
            finish()
        }

        tv_payment.setOnClickListener {
            mViewType="1"
            showCustomDialogForPayment()
        }


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

    private fun showCustomDialogForPayment() {

        mCustomDialogPayment = Dialog(this)
        val window: Window = mCustomDialogPayment!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialogPayment!!.setCanceledOnTouchOutside(false)
        mCustomDialogPayment!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogPayment!!.setContentView(R.layout.custom_dialog_payment)
        // Get radio group selected item using on checked change listener
        mCustomDialogPayment!!. rb_paid_hour.isChecked=true
        mCustomDialogPayment!!.et_amount.isEnabled=false
        mCustomDialogPayment!!.radio_group.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId==R.id.rb_paid_hour){
                mViewType="1"

                mCustomDialogPayment!!. linear_paid_by_service.visibility= View.GONE
                mCustomDialogPayment!!. linear_paid_by_hour.visibility= View.VISIBLE
                mCustomDialogPayment!!.et_amount.isEnabled=false
            }
            if(checkedId==R.id.rb_paid_services){
                mViewType="2"

                mCustomDialogPayment!!.linear_paid_by_hour.visibility= View.GONE
                mCustomDialogPayment!!.linear_paid_by_service.visibility= View.VISIBLE
            }

        }

        mCustomDialogPayment!!.tv_close.setOnClickListener {
            mCustomDialogPayment!!.dismiss()
        }
        mCustomDialogPayment!!. btn_submit.setOnClickListener {
          if(mViewType=="1"){
             val str= mCustomDialogPayment!!.et_amount.text.toString()


              if(!str.isEmpty()){
                  mAmount=  str.toInt()
                  newbookingList()

              }else{
                  showDialogWithDismiss("please Input Amount")
              }

          }else{
              val str1= mCustomDialogPayment!!.et_total_amount.text.toString()
              if(!str1.isEmpty()){
                  mAmount=  str1.toInt()
                  newbookingList()

              }else{
                  showDialogWithDismiss("please Input Amount")
              }
          }








        }
        mCustomDialogPayment!!.btclose.setOnClickListener {
            mCustomDialogPayment!!.dismiss()
        }
        mCustomDialogPayment!!. et_service.setOnClickListener {
            mListServices= ArrayList<String>()

            for(i in 1..50){
                mListServices.add(i.toString())
            }
            mCustomDialogType="2"
            showCustomDialog(mListServices!!,mCustomDialogType!!)

        }
        mCustomDialogPayment!!. et_featured.setOnClickListener {

            mListFeatured=ArrayList<String>()
            mListFeatured.add("Online Pay")
            mListFeatured.add("Cash")
            mCustomDialogType="1"
            showCustomDialog(mListFeatured!!,mCustomDialogType!!)

        }
        mCustomDialogPayment!!. et_payment_mode.setOnClickListener {

            mListFeatured=ArrayList<String>()
            mListFeatured.add("Online Pay")
            mListFeatured.add("Cash")
            mCustomDialogType="3"
            showCustomDialog(mListFeatured!!,mCustomDialogType!!)

        }


        mCustomDialogPayment!!.show()

    }

    private fun showCustomDialog(list: ArrayList<String>,type:String) {

        mCustomDialog = Dialog(this)
        val window: Window = mCustomDialog!!.getWindow()
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        mCustomDialog!!.setCanceledOnTouchOutside(false)
        mCustomDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialog!!.setContentView(R.layout.custom_dialog_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mCustomDialog!!.custom_recycler.layoutManager = layoutManager
        val veneuadapter: CustomDialogRecyclerAdapter = CustomDialogRecyclerAdapter(list,this)
        mCustomDialog!!.custom_recycler.adapter=veneuadapter
        mCustomDialog!!.show()

    }

    //geeting  servicelist
    private fun newbookingList() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postCreatePayment(mListServuceidintent!!.book_id!!,mAmount.toString()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        UtiliyMethods.showDialogwithMessage(this,"Payment Creation SuccesFull",this)




    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    //geeting  servicelist
    private fun getPrice(workhour: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .priceCalculation(mListServuceidintent!!.shop_id,workhour)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_price, this::handleError_price)
        )
    }


    private fun handleResponse_price(responseFromSerevr: ResponsePrice) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mCustomDialogPayment!!.et_amount.setText(responseFromSerevr.Price)




    }


    private fun handleError_price(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
}
