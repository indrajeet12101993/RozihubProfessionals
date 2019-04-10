package rozihub.rozihubprofessionals


import android.os.Bundle

import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_detail_actvity.*
import rozihub.rozihubprofessionals.adapter.ImageDeatilViewPagerAdapter


import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric

import rozihub.rozihubprofessionals.pojo.imageList.Result

import rozihub.rozihubprofessionals.view.ZoomOutPageTransformer
import java.util.ArrayList


class ImageDetailActvity : BaseActivity(), ListnerForCustomDialog {
    override fun selectPosition(position: Int) {
        deleteItem(mList!![position].id)
        mList!!.removeAt(position)
        mAdapterViewpager.notifyDataSetChanged()

    }

    private var mCompositeDisposable: CompositeDisposable? = null
    private var mList: ArrayList<Result>?= null
    private var mPosition: Int? = null
    private lateinit var dataManager: DataManager
    private lateinit var mAdapterViewpager: ImageDeatilViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail_actvity)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        mPosition = intent.getIntExtra("id", 0)
        mList = intent.getSerializableExtra("list") as ArrayList<Result>
        mAdapterViewpager=ImageDeatilViewPagerAdapter(mList!!,this)
        viewPager2.adapter = mAdapterViewpager
        viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager2.setPageTransformer( ZoomOutPageTransformer())
        viewPager2.currentItem = mPosition!!

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {


            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //geeting  profile list
    private fun deleteItem(id: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .deleteImages(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_gallery, this::handleError_gallery)
        )
    }


    private fun handleResponse_gallery(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()





    }


    private fun handleError_gallery(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


}
