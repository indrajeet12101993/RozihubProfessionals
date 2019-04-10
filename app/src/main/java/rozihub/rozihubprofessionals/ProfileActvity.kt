package rozihub.rozihubprofessionals

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile_actvity.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rozihub.rozihubprofessionals.adapter.MyProfilePagerAdapter
import rozihub.rozihubprofessionals.base.BaseActivity
import rozihub.rozihubprofessionals.base.RozihubApplicaion
import rozihub.rozihubprofessionals.dataprefence.DataManager
import rozihub.rozihubprofessionals.networking.ApiRequestClient
import rozihub.rozihubprofessionals.pojo.ResponseFromServerGeneric
import rozihub.rozihubprofessionals.pojo.getImage.ResponseFromServerGetImage
import rozihub.rozihubprofessionals.utils.FileHelper
import rozihub.rozihubprofessionals.utils.FileHelper.reduceFileSize
import java.io.File


class ProfileActvity : BaseActivity() {
    private var mSectionsPagerAdapter: MyProfilePagerAdapter? = null
    private lateinit var mFileUserPhoto: File
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "My Profile"
        mSectionsPagerAdapter = MyProfilePagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        dataManager = RozihubApplicaion.baseApplicationInstance.getdatamanger()
        tabLayout.setupWithViewPager(container)
        getImage()
        tv_logout.setOnClickListener {
            dataManager.setLoggedIn(false)
            endActivityWithClearTask<Main2Activity>()
        }
        profile_image.setOnClickListener {
            startPic(4, 4)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.action_edit -> {
                launchActivity<EditProfileActvity>()
                true
            }

            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startPic(aspectRatioX: Int, aspectRatioY: Int) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setCropMenuCropButtonTitle("Done")
            .setCropMenuCropButtonIcon(R.drawable.ic_green_right_rounded)
            .setAspectRatio(aspectRatioX, aspectRatioY)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode,data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                onImageResult(File(FileHelper.getPath(this, resultUri)))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun onImageResult(file: File) {

        Glide.with(this).load(file).into(profile_image)
        mFileUserPhoto = file
        uploadingUserPhoto()
    }

    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()
        val file = reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file!!.name.toString(), reqFile)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"), dataManager.getUserId()!!)



        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImage(body, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        showDialogWithDismiss(response.response_message)


    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    //geeting
    private fun getImage() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getIamge(dataManager.getUserId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_numbersend, this::handleError_numbersend)
        )
    }


    private fun handleResponse_numbersend(responseFromSerevr: ResponseFromServerGetImage) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        if (!responseFromSerevr.Result.profile_photo.isEmpty()) {
            Glide.with(this)
                .load("https://rozihub.com/local/images/subservicephoto/" + responseFromSerevr.Result.profile_photo)
                .into(profile_image)
        }

    }


    private fun handleError_numbersend(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

}
