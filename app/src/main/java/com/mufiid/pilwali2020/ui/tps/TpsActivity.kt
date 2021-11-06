package com.mufiid.pilwali2020.ui.tps

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.locationbasedservice.MySimpleLocation
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.data.entity.Tps
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.utils.helpers.CustomView
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_tps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class TpsActivity : AppCompatActivity(), MySimpleLocation.MySimpleLocationCallback, ITpsView,
    View.OnClickListener {
    private var alamat_tps = String
    private var loading: ProgressDialog? = null
    private var tpsPresenter: TpsPresenter? = null
    private var fotoTPS: Bitmap? = null
    private var part: MultipartBody.Part? = null
    private var currentPhotoPath: String? = ""
    private var fotoTpsApi: String? = null
    private val UPDATE_INTERVAL = 10 * 1000 /* 10 secs */.toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
        private val REQUEST_IMAGE_CAPTURE = 1
    }

    // pertama
    private lateinit var mySimpleLocation: MySimpleLocation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tps)

        setSupportActionBar()
        init()
        initPermission()
        Constants.getUserData(this)?.idTps?.let { tpsPresenter?.getDataTps(it) }

    }

    private fun setSupportActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.title_detail_tps)
    }

    private fun init() {
        loading = ProgressDialog(this)
        tpsPresenter = TpsPresenter(this)
        open_camera.setOnClickListener(this)
        btn_simpan.setOnClickListener(this)
        refresh_location.setOnClickListener(this)
    }

    private fun doSimpan() {
        val lat = RequestBody.create("text/plain".toMediaTypeOrNull(), latitude.text.toString())
        val long = RequestBody.create("text/plain".toMediaTypeOrNull(), longitude.text.toString())
        val username =
            Constants.getUserData(this)?.username?.let {
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    it
                )
            }
        val id_tps =
            Constants.getUserData(this)?.idTps?.let {
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    it
                )
            }
        val form_page = RequestBody.create("text/plain".toMediaTypeOrNull(), "2")
        val apiKey =
            Constants.getUserData(this)?.api_key?.let {
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    it
                )
            }

        if (currentPhotoPath != "") {
            // ketika user capture foto
            val pictFromBitmap = File(currentPhotoPath)
            val reqFile: RequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), pictFromBitmap)
            part = MultipartBody.Part.createFormData("foto_tps", pictFromBitmap.name, reqFile)
        } else {
            if (fotoTpsApi.isNullOrEmpty()) {
                CustomView.customToast(this, resources.getString(R.string.message_not_upload_image), true, isSuccess = false)
                return
            }
        }
        tpsPresenter?.postData(id_tps, form_page, part, lat, long, username, apiKey)


    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun rotate(bitmap: Bitmap) {
        val ei = ExifInterface(currentPhotoPath!!)
        val eiValue = ei.getAttribute(ExifInterface.TAG_ORIENTATION)?.toInt()
        if (Build.VERSION.SDK_INT >= 23) {
            when (eiValue) {
                6 -> rotateImg(bitmap, 90F)
                8 -> rotateImg(bitmap, 270F)
                3 -> rotateImg(bitmap, 180F)
                1 -> rotateImg(bitmap, 0F)
                0 -> rotateImg(bitmap, 90F)
            }
        } else {
            when (eiValue) {
                6 -> rotateImg(bitmap, 90F)
                8 -> rotateImg(bitmap, 270F)
                3 -> rotateImg(bitmap, 180F)
                1 -> rotateImg(bitmap, 0F)
                0 -> rotateImg(bitmap, 0F)
            }
        }
    }

    private fun rotateImg(bitmap: Bitmap, i: Float) {
        val matrix = Matrix()
        matrix.setRotate(i)
        val img = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        fotoTPS = img
        image_tps.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageBitmap(img)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == RESULT_OK) {
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
                rotate(imageBitmap)
            } else if(requestCode == IMAGE_PICK_CODE) {
                setImageFromGallery(data)
            }

        }
    }

    private fun startCaptureTPS() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.mufiid.pilwali2020.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun getPermissionGps() {
        // check permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            } else {
//                mySimpleLocation = MySimpleLocation(this, this)
//                mySimpleLocation.checkLocationSetting(this)
//                startLocationUpdates()
                requestLocationUpdate()

                mShimmerViewContainer.startShimmer()
                mShimmerViewContainer.visibility = View.VISIBLE
                layout_latitude.visibility = View.GONE
                layout_longitude.visibility = View.GONE
            }
        } else {
//            mySimpleLocation = MySimpleLocation(this, this)
//            mySimpleLocation.checkLocationSetting(this)
//            startLocationUpdates()
            requestLocationUpdate()

            // show shimmer
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.VISIBLE
            layout_latitude.visibility = View.GONE
            layout_longitude.visibility = View.GONE
        }
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
        }
    }

    override fun getLocation(location: Location) {
        val lat = location.latitude
        val long = location.longitude

        GlobalScope.launch(Dispatchers.IO) {

            launch(Dispatchers.Main) {
                // bind to UI
                try {
                    // stop shimmer
                    mShimmerViewContainer.stopShimmer()
                    mShimmerViewContainer.visibility = View.GONE
                    layout_latitude.visibility = View.VISIBLE
                    layout_longitude.visibility = View.VISIBLE

                    latitude.setText(lat.toString())
                    longitude.setText(long.toString())

                    //If you want to stop get your location on first result
                    mySimpleLocation.stopGetLocation()
                } catch (e: Exception) {
                    // code ...
                }
            }
        }
    }

    override fun isLoadingTps(state: Int?) {
        when (state) {
            1 -> {
                // get
                shimmer_container_detail_tps.visibility = View.VISIBLE
                shimmer_container_detail_tps.startShimmer()
                sub_layout_detail_tps.visibility = View.GONE
            }
            2 -> {
                // post
                loading?.let {
                    it.setMessage(resources.getString(R.string.please_wait))
                    it.setCanceledOnTouchOutside(false)
                    it.setCancelable(false)
                    it.show()
                }
            }
        }

    }

    override fun hideLoadingTps(state: Int?) {
        when (state) {
            1 -> {
                // get
                shimmer_container_detail_tps.stopShimmer()
                shimmer_container_detail_tps.visibility = View.GONE
                sub_layout_detail_tps.visibility = View.VISIBLE
            }
            2 -> {
                // post
                loading?.dismiss()
            }
        }
    }

    override fun getDataTps(message: String?, data: Tps) {
        nomor_tps.text = data.noTps
        kel_tps.text = data.kelurahan
        kec_tps.text = data.kecamatan
        et_alamat_tps.text = data.alamat
        fotoTpsApi = data.foto_tps

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.centerRadius = 100F
        circularProgressDrawable.start()

        if (!data.foto_tps.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.foto_tps)
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(image_tps)

            // open_camera.visibility = View.GONE
        } else {
            image_tps.setImageResource(R.drawable.ic_img_placeholder)
        }

        if (data.lati.isNullOrEmpty() && data.longi.isNullOrEmpty()) {
            getPermissionGps()
        } else {
            latitude.setText(data.lati)
            longitude.setText(data.longi)
        }

        if (data.isDefault == "0") {
            // tidak bisa update
            refresh_location.visibility = View.GONE
        }

        if(data.verified == "1") {
            btn_simpan.visibility = View.GONE
            open_camera.visibility = View.GONE
            CustomView.customToast(this, "Data TPS ${getString(R.string.verification)}", false, isSuccess = true)
        }
    }

    override fun failedGetDataTps(message: String?) {
        CustomView.customToast(this, message, false, isSuccess = false)
    }

    override fun messageSuccess(message: String?) {
        refresh_location.visibility = View.GONE
        CustomView.customToast(this, message, false, isSuccess = true)
    }

    override fun messageFailed(message: String?) {
        CustomView.customToast(this, message, false, isSuccess = false)
    }

    private fun requestLocationUpdate() {
        mFusedLocationProviderClient = getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onLocationChanged(locationResult?.lastLocation)
            }
        }
        mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
        mFusedLocationProviderClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun onLocationChanged(location: Location?) {
        mShimmerViewContainer.stopShimmer()
        mShimmerViewContainer.visibility = View.GONE
        layout_latitude.visibility = View.VISIBLE
        layout_longitude.visibility = View.VISIBLE

        latitude.setText(location?.latitude.toString())
        longitude.setText(location?.longitude.toString())
    }

    private fun startLocationUpdates() {
        mFusedLocationProviderClient = getFusedLocationProviderClient(this)
        // Create the location request to start receiving updates
        val mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        // Create LocationSettingsRequest object using location request
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()

        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
        mFusedLocationProviderClient?.requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }

    private fun getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        val locationClient = getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient.lastLocation
            .addOnSuccessListener { location -> // GPS location can be null if GPS is switched off
                location?.let { onLocationChanged(it) }
            }
            .addOnFailureListener { e ->
                Log.d("MapDemoActivity", "Error trying to get last GPS location")
                e.printStackTrace()
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.open_camera -> {
                val options = resources.getStringArray(R.array.choose_image)
                AlertDialog.Builder(this).apply {
                    setItems(options) { _, item ->
                        when {
                            options[item] == getString(R.string.take_photo) -> {
                                startCaptureTPS()
                            }
                            options[item] == getString(R.string.choose_gallery) -> {
                                startPickImage()
                            }
                        }
                    }
                }.show()
            }
            R.id.btn_simpan -> doSimpan()
            R.id.refresh_location -> getPermissionGps()
        }
    }

    private fun startPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    1
                )
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun setImageFromGallery(data: Intent?) {
        val uri = data?.data
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val c = contentResolver.query(uri!!, filePath, null, null, null)
        c?.moveToFirst()
        val columnIndex = c?.getColumnIndex(filePath[0])
        val picturePath = c?.getString(columnIndex!!)
        c?.close()
        // var thumbnail = BitmapFactory.decodeFile(picturePath)
        currentPhotoPath = picturePath
        image_tps.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageURI(uri)
        }
    }

}