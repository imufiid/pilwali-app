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
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.PaslonAdapter
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.AddVotePresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IPaslonView
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_add_vote.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddVoteActivity : AppCompatActivity(), IPaslonView, ITpsView, View.OnClickListener {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var loading: ProgressDialog? = null
    private var presenter: AddVotePresenter? = null
    private var tpsPresenter: TpsPresenter? = null
    private var fotoBlangko: Bitmap? = null
    private var pictPart: MultipartBody.Part? = null
    private var fotoTpsApi: String? = null
    private var currentPhotoPath: String? = ""
    private val suaraPaslon = mutableListOf<Int>()
    private val idPaslon = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)

        setSupportActionBar()
        init()
    }

    private fun setSupportActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.perolehan_suara)
        }
    }

    private fun init() {
        open_camera.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        viewManager = LinearLayoutManager(this)
        presenter = AddVotePresenter(this)
        tpsPresenter = TpsPresenter(this)
        loading = ProgressDialog(this)

        checkVerification()
        getPermission()

        presenter?.getPaslon(Constants.getUserData(this)?.idTps)
        Constants.getUserData(this)?.idTps?.let {
            tpsPresenter?.getDataTps(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }

    private fun checkVerification() {
        when (Constants.getVerification(this)) {
            1 -> {
                jumlah_suara_tidak_sah.isEnabled = false
                layout_btn_save.visibility = View.GONE
                open_camera.visibility = View.GONE
            }
        }
    }

    /**
     * function untuk upload data
     *
     * @author Imam Mufiid
     *
     * */
    private fun doUpload() {
        // get value dari editText recycler view
        idPaslon.clear()
        suaraPaslon.clear()
        for (i in 0 until (PaslonAdapter.dataPaslon?.size ?: 0)) {
            suaraPaslon.add(PaslonAdapter.dataPaslon!![i].jumlah_suara_di_tps!!.toInt())
            idPaslon.add(PaslonAdapter.dataPaslon!![i].id!!.toInt())
        }

        /**
         * check if user not take picture
         *
         * */

        // convert value to Request Body
        val suaraTidakSah = jumlah_suara_tidak_sah.text.toString()
        val idTps =
            Constants.getUserData(this)?.idTps?.toRequestBody("text/plain".toMediaTypeOrNull())
        val username =
            Constants.getUserData(this)?.username?.toRequestBody("text/plain".toMediaTypeOrNull())
        val suara_tidak_sah = suaraTidakSah.toRequestBody("text/plain".toMediaTypeOrNull())
        val apiKey =
            Constants.getUserData(this)?.api_key?.toRequestBody("text/plain".toMediaTypeOrNull())

        if (currentPhotoPath != "") {
            val pictBlangko = File(currentPhotoPath)
            val reqFile = pictBlangko.asRequestBody("image/*".toMediaTypeOrNull())
            pictPart = MultipartBody.Part.createFormData("foto_blanko", pictBlangko.name, reqFile)
        } else {
            if (fotoTpsApi.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.message_not_upload_image),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        // post data
        presenter?.postDataPerolehanSuara(
            idTps,
            suara_tidak_sah,
            idPaslon,
            suaraPaslon,
            pictPart,
            username,
            apiKey
        )
    }

    /**
     * function untuk menghidupkan kamera
     *
     * @author Imam Mufiid
     *
     * */
    private fun startCaptureBlangko() {
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

    /**
     * function untuk convert hasil foto kedalam file
     *
     * @author Imam Mufiid
     *
     * */
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

    /**
     * function untuk merotate bitmap
     *
     * @author Imam Mufiid
     * @param bitmap berisi bitmap image
     *
     * */
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

    /**
     * function untuk merotate image dan bind ke UI
     *
     * @author Imam Mufiid
     * @param bitmap berisi bitmap gambar
     * @param i berisi value rotasi
     * */
    private fun rotateImg(bitmap: Bitmap, i: Float) {
        val matrix = Matrix()
        matrix.setRotate(i)
        val img = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        fotoBlangko = img
        image_blangko.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageBitmap(img)
        }
    }

    /**
     * function activity result setelah take picture
     *
     * @author Imam Mufiid
     *
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            rotate(imageBitmap)
        }
    }

    /**
     * function untuk mengecek permission secara runtime
     *
     * @author Imam Mufiid
     *
     * */
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
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    /**
     * function melakukan proses ketika loading get data paslon
     *
     * @author Imam Mufiid
     * @param state berisi state loading
     *
     * */
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
                loading?.let {
                    it.setMessage("Tunggu sebentar..")
                    it.setCanceledOnTouchOutside(false)
                    it.setCancelable(false)
                    it.show()
                }
            }
        }

    }

    /**
     * function melaukan proses ketika proses loading selesai
     *
     * @author Imam Mufiid
     * @param state berisi state loading
     *
     * */
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

    /**
     * function untuk bind ke UI dari get data paslon
     *
     * @author Imam Mufiid
     * @param message berisi pesan success dari API
     * @param data berisi list data paslon
     *
     * */
    override fun getDataPaslon(message: String?, data: List<Paslon>?) {
        rv_paslon.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = PaslonAdapter(data!!)
        }
    }

    /**
     * function ketika proses get data paslon gagal
     *
     * @author Imam Mufiid
     * @param message pesan gagal
     *
     * */
    override fun failedGetDataPaslon(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * function ketika proses loading berjalan ketika get data tps
     *
     * @author Imam Mufiid
     * @param state berisi value state loading
     *
     * */
    override fun isLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.VISIBLE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.GONE
    }

    /**
     * function ketika proses loading selesai ketika get data tps
     *
     * @author Imam Mufiid
     * @param state berisi value state loading
     *
     * */
    override fun hideLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.GONE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.VISIBLE
    }

    /**
     * function untuk bind data TPS ke UI
     *
     * @author Imam Mufiid
     * @param message pesan success
     * @param data berisi data TPS
     *
     * */
    override fun getDataTps(message: String?, data: Tps) {
        jumlah_dpt.text = data.dpt2
        jumlah_dptb.text = data.dptb2
        jumlah_dpk.text = data.dpk2
        jumlah_dph.text = data.dpph2
        jumlah_suara_tidak_sah.setText(data.suara_tidak_sah)

        /**
         * check image if not null
         * pasang ke imageView
         * */
        if (!data.fotoBlangkoResize.isNullOrEmpty()) {
            fotoTpsApi = data.fotoBlangkoResize
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.centerRadius = 100F
            circularProgressDrawable.start()

            Glide.with(this)
                .load(data.fotoBlangkoResize)
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(image_blangko)

            // open_camera.visibility = View.GONE
        } else {
            image_blangko.setImageResource(R.drawable.ic_img_placeholder)
        }
    }

    /**
     * function ketika proses get data tps gagal
     *
     * @author Imam Mufiid
     * @param message pesan gagal
     *
     * */
    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * function untuk menampilkan pesan berhasil
     *
     * @author Imam Mufiid
     * @param message pesan berhasil
     *
     * */
    override fun messageSuccess(message: String?) {
        TODO("Not yet implemented")
    }

    /**
     * function untuk menampilkan pesan gagal
     *
     * @author Imam Mufiid
     * @param message pesan gagal
     *
     * */
    override fun messageFailed(message: String?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.open_camera -> startCaptureBlangko()
            R.id.btn_save -> doUpload()
        }
    }
}