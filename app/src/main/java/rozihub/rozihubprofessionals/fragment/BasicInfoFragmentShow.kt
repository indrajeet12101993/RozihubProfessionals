package rozihub.rozihubprofessionals.fragment


import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_basic_info_fragment_show.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import rozihub.rozihubprofessionals.ImageDetailActvity


import rozihub.rozihubprofessionals.R
import rozihub.rozihubprofessionals.adapter.ImageAdapter
import rozihub.rozihubprofessionals.base.BaseFragment
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.callBackInterface.ListnerForCustomDialog
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.imageList.ResponseFromGalleryList

import rozihub.rozihubprofessionals.pojo.newBooking.Result
import rozihub.rozihubprofessionals.pojo.profile.ResponseFromserverProfile
import rozihub.rozihubprofessionals.utils.FileHelper
import java.io.File
import java.util.ArrayList



class BasicInfoFragmentShow : BaseFragment(), ListnerForCustomDialog {
    override fun selectPosition(position: Int) {
        mIsRefreshTrue ="true"
        startintentforAddServices(position)
    }

    private lateinit var et_name: TextInputEditText
    private lateinit var et_number: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var et_adress: TextInputEditText
    private lateinit var et_city: TextInputEditText
    private lateinit var et_state: TextInputEditText
    private lateinit var et_pin_code: TextInputEditText
    private lateinit var rv_all_services: RecyclerView
    private lateinit var mFileUserPhoto: File
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var imageAdapter:ImageAdapter
    private var mList: ArrayList<rozihub.rozihubprofessionals.pojo.imageList.Result>?= null
    private lateinit var mIsRefreshTrue:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_basic_info_fragment_show, container, false)
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        et_name = view.et_name
        et_number = view.et_number
        et_email = view.et_email
        et_adress = view.et_adress
        et_city = view.et_city
        et_state = view.et_state
        et_pin_code = view.et_pin_code
        rv_all_services = view.rv_all_services
        mIsRefreshTrue ="false"

       // val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL ,false)
        view.rv_all_services.layoutManager = GridLayoutManager(activity, 2) as RecyclerView.LayoutManager?
        view.rv_all_services.isNestedScrollingEnabled=false
       // view. rv_all_services.layoutManager = layoutManager

        showuserservices()

        view.tv_add_image.setOnClickListener {
            startPic(4, 4)

        }



        return view
    }

    override fun onResume() {
        if(mIsRefreshTrue=="true"){
            getGalleryList()
        }
        super.onResume()
    }



    private fun startPic(aspectRatioX: Int, aspectRatioY: Int) {

        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setCropMenuCropButtonIcon(R.drawable.ic_green_right_rounded)
            .setAspectRatio(aspectRatioX, aspectRatioY)
            .start(context!!,this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                onImageResult(File(FileHelper.getPath(activity!!, resultUri)))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun onImageResult(file: File) {

       // Glide.with(this).load(file).into(profile_image)
        mFileUserPhoto = file
        uploadingUserPhoto()
    }

    //geeting  profile list
    private fun showuserservices() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServiceProfile(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromserverProfile) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(responseFromSerevr.Result.first.username!=null){
            et_name.setText(responseFromSerevr.Result.first.username)
        }

        if(responseFromSerevr.Result.first.shop_phone_no!=null){
            et_number.setText(responseFromSerevr.Result.first.shop_phone_no)
        }
        if(responseFromSerevr.Result.first.seller_email!=null) {
            et_email.setText(responseFromSerevr.Result.first.seller_email)
        }
        if(responseFromSerevr.Result.first.address!=null) {
            et_adress.setText(responseFromSerevr.Result.first.address)
        }
        if(responseFromSerevr.Result.first.city!=null) {
            et_city.setText(responseFromSerevr.Result.first.city)
        }
        if(responseFromSerevr.Result.first.state!=null) {
            et_state.setText(responseFromSerevr.Result.first.state)
        }
        if(responseFromSerevr.Result.first.pin_code!=null) {
            et_pin_code.setText(responseFromSerevr.Result.first.pin_code)
        }

        getGalleryList()








    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()
        val file = FileHelper.reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file!!.name.toString(), reqFile)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"), dataManager.getUserId()!!)



        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImageForGallery(body, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        getGalleryList()


    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    //geeting  profile list
    private fun getGalleryList() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getGallery(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_gallery, this::handleError_gallery)
        )
    }


    private fun handleResponse_gallery(responseFromSerevr: ResponseFromGalleryList) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if(!responseFromSerevr.result.isEmpty()){
            mList=responseFromSerevr.result
            imageAdapter= ImageAdapter(responseFromSerevr.result,this)
            rv_all_services.adapter=imageAdapter

        }



    }


    private fun handleError_gallery(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun startintentforAddServices(position:Int) {

        val intent = Intent(activity, ImageDetailActvity::class.java)
        intent.putExtra("id",position)
        intent.putExtra("list",mList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        } else {

            startActivity(intent)
        }
    }
}
