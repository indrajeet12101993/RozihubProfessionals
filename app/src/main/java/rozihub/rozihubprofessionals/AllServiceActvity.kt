package rozihub.rozihubprofessionals

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_all_service_actvity.*
import rozihub.rozihubprofessionals.adapter.AllServiceAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.servicelist.ResponseFromServerServicelist
import rozihub.rozihubprofessionals.pojo.servicelist.Result


class AllServiceActvity : BaseActivity(), ListnerForCustomDialog {
    override fun selectPosition(position: Int) {
        mIsRefreshTrue ="true"
        startintentforAddServices(mListServices[position])
    }

    private lateinit var mListServices: List<Result>
    private lateinit var mIsRefreshTrue:String
    lateinit var dataManager: DataManager
    private var mCompositeDisposable: CompositeDisposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_service_actvity)
        setSupportActionBar(toolbar)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Services List"
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_all_services.layoutManager = layoutManager
        mIsRefreshTrue ="false"
        servicelist()
        rv_all_services.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && floating_action_button.getVisibility() == View.VISIBLE) {
                    floating_action_button.hide()
                } else if (dy < 0 && floating_action_button.getVisibility() != View.VISIBLE) {
                    floating_action_button.show()
                }
            }
        })

        floating_action_button.setOnClickListener {
            mIsRefreshTrue ="true"
            launchActivity<AddServiceActvity>()

        }
    }

    private fun startintentforAddServices(id: Result) {

        val intent = Intent(this, EditServiceActvity::class.java)
         intent.putExtra("id",id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else {

            startActivity(intent)
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

    override fun onResume() {
        if(mIsRefreshTrue=="true"){
            servicelist()
        }
        super.onResume()
    }


    //servicelist
    private fun servicelist() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postservicelist(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerServicelist) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        if(!responseFromSerevr.Result.isEmpty()){
            mListServices=responseFromSerevr.Result
            val veneuadapter= AllServiceAdapter(mListServices,this)
            rv_all_services.adapter=veneuadapter

        }



    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
