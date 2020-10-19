package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.locationbasedservice.MySimpleLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_tps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class TpsActivity : AppCompatActivity(), MySimpleLocation.MySimpleLocationCallback, ITpsView {
    private val REQUEST_IMAGE_CAPTURE = 1
    private var alamat_tps = String
    private var loading: ProgressDialog? = null
    private var tpsPresenter: TpsPresenter? = null
    private var fotoTPS: Bitmap? = null
    private var part: MultipartBody.Part? = null
    private var currentPhotoPath: String? = ""

    // pertama
    private lateinit var mySimpleLocation: MySimpleLocation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail TPS"

        // init progressbar
        loading = ProgressDialog(this)

        tpsPresenter = TpsPresenter(this)
        tpsPresenter?.getDataTps(Constants.getIDTps(this))

        open_camera.setOnClickListener {
            captureTPS()
        }

        btn_simpan.setOnClickListener {
            doSimpan()
        }

        getLatLong()
        getPermissionTakePhoto()
    }

    private fun doSimpan() {
        //val pictFromBitmap = createFile(fotoTPS)
        if (currentPhotoPath != "") {
            val pictFromBitmap = File(currentPhotoPath)
            val reqFile: RequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), pictFromBitmap)
            part = MultipartBody.Part.createFormData("foto_tps", pictFromBitmap.name, reqFile)
        }

        val lat = RequestBody.create("text/plain".toMediaTypeOrNull(), latitude.text.toString())
        val long = RequestBody.create("text/plain".toMediaTypeOrNull(), longitude.text.toString())
        val username =
            RequestBody.create("text/plain".toMediaTypeOrNull(), Constants.getUsername(this))
        val id_tps = RequestBody.create("text/plain".toMediaTypeOrNull(), Constants.getIDTps(this))
        val form_page = RequestBody.create("text/plain".toMediaTypeOrNull(), "2")


        tpsPresenter?.postData(id_tps, form_page, part, lat, long, username)
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
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        var rotateBitmap: Bitmap? = null

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
        image_tps.setImageBitmap(img)
    }

    /**
     * function to create file
     * @author imam mufiid
     * @param bitmap => foto TPS
     *
     * */
    private fun createFile(bitmap: Bitmap?): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , System.currentTimeMillis().toString() + "_image.jpg"
        )

        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData: ByteArray = bos.toByteArray()

        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_refresh_tps, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
        onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                getLatLong()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            rotate(imageBitmap)
        }
    }

    private fun captureTPS() {
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

    private fun getPermission() {
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
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            } else {
//                mySimpleLocation = MySimpleLocation(this, this)
//                mySimpleLocation.checkLocationSetting(this)
                getLatLong()

                // show shimmer
                mShimmerViewContainer.startShimmer()
                mShimmerViewContainer.visibility = View.VISIBLE
                layout_latitude.visibility = View.GONE
                layout_longitude.visibility = View.GONE
            }
        } else {
//            mySimpleLocation = MySimpleLocation(this, this)
//            mySimpleLocation.checkLocationSetting(this)
            getLatLong()

            // show shimmer
            mShimmerViewContainer.startShimmer()
            mShimmerViewContainer.visibility = View.VISIBLE
            layout_latitude.visibility = View.GONE
            layout_longitude.visibility = View.GONE
        }
    }

    private fun getPermissionTakePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                loading?.setMessage("Mohon tunggu sebentar...")
                loading?.setCanceledOnTouchOutside(false)
                loading?.show()
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

        if (!data.foto_tps.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.foto_tps)
                .placeholder(R.drawable.ic_spinner_imagepx)
                .centerCrop()
                .into(image_tps)
        } else {
            image_tps.setImageResource(R.drawable.ic_img_placeholder)
        }
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getLatLong() {

        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
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
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            mFusedLocation.lastLocation.addOnSuccessListener(
                this
            ) { location ->
                // Display in Toast
                mShimmerViewContainer.stopShimmer()
                mShimmerViewContainer.visibility = View.GONE
                layout_latitude.visibility = View.VISIBLE
                layout_longitude.visibility = View.VISIBLE

                latitude.setText(location?.latitude.toString())
                longitude.setText(location?.longitude.toString())
            }
        }


    }


}