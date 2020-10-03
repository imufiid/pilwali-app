package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.PaslonAdapter
import com.mufiid.pilwali2020.adapters.PaslonAdptr
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.AddVotePresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IPaslonView
import com.mufiid.pilwali2020.views.ITpsView
import kotlinx.android.synthetic.main.activity_add_vote.*
import kotlinx.android.synthetic.main.activity_add_vote.open_camera
import kotlinx.android.synthetic.main.activity_tps.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddVoteActivity : AppCompatActivity(), IPaslonView, ITpsView {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var loading: ProgressDialog? = null
    private var presenter: AddVotePresenter? = null
    private var tpsPresenter: TpsPresenter? = null
    private var fotoBlangko: Bitmap? = null
    private var pictPart: MultipartBody.Part? = null
    private var currentPhotoPath: String? = ""
    private val suaraPaslon = mutableListOf<Int>()
    private val idPaslon = mutableListOf<Int>()
    val idJsonObject: JSONObject? = null
    val suaraJsonObject: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perolehan Suara"
        viewManager = LinearLayoutManager(this)
        presenter = AddVotePresenter(this)
        tpsPresenter = TpsPresenter(this)
        loading = ProgressDialog(this)
        presenter?.getPaslon()
        tpsPresenter?.getDataTps(Constants.getIDTps(this))

        open_camera.setOnClickListener {
            captureBlangko()
        }

        getPermission()



        btn_save.setOnClickListener {
            doUpload()
        }
    }

    private fun doUpload() {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        // JAVA
        // for (i in 0 until PaslonAdptr.editModelArrayList.size) {
        //     array.add(PaslonAdptr.editModelArrayList[i].jumlah_suara.toString())
        // }

        // KOTLIN
        idPaslon.clear()
        suaraPaslon.clear()
        for (i in 0 until (PaslonAdapter.dataPaslon?.size ?: 0)) {
            suaraPaslon.add(PaslonAdapter.dataPaslon!![i].jumlah_suara!!.toInt())
            idPaslon.add(PaslonAdapter.dataPaslon!![i].id!!.toInt())
            idJsonObject?.put("id[$i]", PaslonAdapter.dataPaslon!![i].id.toString())
            suaraJsonObject?.put("suara[$i]", PaslonAdapter.dataPaslon!![i].jumlah_suara.toString())
        }

        val idJson = gson.toJson(idPaslon)

        // POST Data
        /**
         * check if user not take picture
         *
         * */
        if (currentPhotoPath != "") {
            val pictBlangko = File(currentPhotoPath)
            val reqFile = pictBlangko.asRequestBody("image/*".toMediaTypeOrNull())
            pictPart = MultipartBody.Part.createFormData("foto_blanko", pictBlangko.name, reqFile)
        }

        val suaraTidakSah = jumlah_suara_tidak_sah.text.toString()

        val idTps = Constants.getIDTps(this).toRequestBody("text/plain".toMediaTypeOrNull())
        val username = Constants.getUsername(this).toRequestBody("text/plain".toMediaTypeOrNull())
        val suara_tidak_sah = suaraTidakSah.toRequestBody("text/plain".toMediaTypeOrNull())
        presenter?.postDataPerolehanSuara(idTps, suara_tidak_sah, idPaslon, suaraPaslon, pictPart, username)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun captureBlangko() {
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
        Log.d("EXIF value", ei.getAttribute(ExifInterface.TAG_ORIENTATION).toString());
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
        }

    }

    private fun rotateImg(bitmap: Bitmap, i: Float) {
        val matrix = Matrix()
        matrix.setRotate(i)
        val img = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        fotoBlangko = img
        image_blangko.setImageBitmap(img)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            rotate(imageBitmap)
        }
    }

    private fun getPermission() {
        // check permission
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

    override fun isLoadingPaslon(state: Int?) {
        when (state) {
            1 -> {
                shimmer_container.visibility = View.VISIBLE
                shimmer_container.startShimmer()
                rv_paslon.visibility = View.GONE
                title_upload_blangko.visibility = View.GONE
                div2.visibility = View.GONE
                layout_img.visibility = View.GONE
                btn_save.visibility = View.GONE
                layout_suara_tdk_sah.visibility = View.GONE
                div.visibility = View.GONE
            }
            2 -> {
                loading?.setMessage("Tunggu sebentar..")
                loading?.setCanceledOnTouchOutside(false)
                loading?.show()
            }
        }

    }

    override fun hideLoadingPaslon(state: Int?) {
        when (state) {
            1 -> {
                shimmer_container.visibility = View.GONE
                shimmer_container.stopShimmer()
                title_upload_blangko.visibility = View.VISIBLE
                rv_paslon.visibility = View.VISIBLE

                div2.visibility = View.VISIBLE
                layout_img.visibility = View.VISIBLE
                btn_save.visibility = View.VISIBLE

                layout_suara_tdk_sah.visibility = View.VISIBLE
                div.visibility = View.VISIBLE
            }
            2 -> {
                loading?.dismiss()
            }
        }

    }

    override fun getDataPaslon(message: String?, data: List<Paslon>?) {
        rv_paslon.apply {
            setHasFixedSize(true)
            layoutManager = viewManager

            // KOTLIN
            adapter = PaslonAdapter(data!!)

            // JAVA
            // adapter = PaslonAdptr(this@AddVoteActivity, data as ArrayList<Paslon>?)
        }
    }

    override fun failedGetDataPaslon(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun isLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.VISIBLE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.GONE
    }

    override fun hideLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.GONE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.VISIBLE
    }

    override fun getDataTps(message: String?, data: Tps) {
        jumlah_dpt.text = data.dpt2
        jumlah_dptb.text = data.dptb2
        jumlah_dpk.text = data.dpk2
        jumlah_dpktb.text = data.dpktb2
        jumlah_difabel.text = data.difabel2
        jumlah_suara_tidak_sah.setText(data.suara_tidak_sah)

        /**
         * check image if not null
         * pasang ke imageView
         * */
        if (!data.foto_blanko.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.foto_blanko)
                .placeholder(R.drawable.ic_img_placeholder)
                .centerCrop()
                .into(image_blangko)
        }else {
            image_blangko.setImageResource(R.drawable.ic_img_placeholder)
        }
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        TODO("Not yet implemented")
    }

    override fun messageFailed(message: String?) {
        TODO("Not yet implemented")
    }
}