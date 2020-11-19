package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.presenters.UserPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IUserView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_tps.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity(), IUserView {
    private var userPresenter: UserPresenter? = null
    private var loading: ProgressDialog? = null
    private var currentPhotoPath: String? = ""
    private var part: MultipartBody.Part? = null

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.title = "Edit Profil"
        userPresenter = UserPresenter(this)
        loading = ProgressDialog(this)

        Constants.getUserData(this)?.id?.let {
            userPresenter?.getUserByID(it)
        }

        btn_update_user.setOnClickListener {
            doUpdate()
        }

        open_galery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val path = data?.data
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(path!!, filePath, null, null, null)
            c?.moveToFirst()
            val columnIndex = c?.getColumnIndex(filePath[0])
            val picturePath = c?.getString(columnIndex!!)
            c?.close()
            // var thumbnail = BitmapFactory.decodeFile(picturePath)
            currentPhotoPath = picturePath
            image_profile.setImageURI(path)
        }
    }


    private fun doUpdate() {
        val userData = Constants.getUserData(this)
        val id = userData?.id?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val username = userData?.username?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val apiKey = userData?.api_key?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val nama = RequestBody.create("text/plain".toMediaTypeOrNull(),et_nama.text.toString())
        val passwdNew = RequestBody.create("text/plain".toMediaTypeOrNull(), et_password_baru.text.toString())
        val passwdOld = RequestBody.create("text/plain".toMediaTypeOrNull(),et_password_lama.text.toString())
        if (currentPhotoPath != "") {
            // ketika user capture foto
            val pictFromBitmap = File(currentPhotoPath)
            val reqFile: RequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), pictFromBitmap)
            part = MultipartBody.Part.createFormData("foto", pictFromBitmap.name, reqFile)
        }
        userPresenter?.updateUser(
            id,
            username,
            nama,
            part,
            passwdNew,
            apiKey
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }


    override fun isLoadingUser(state: Int?) {
        when (state) {
            1 -> {
                // get data
                loading?.setMessage("Tunggu sebentar...")
                loading?.show()
            }
            2 -> {
                // post data
                loading?.setMessage("Tunggu sebentar...")
                loading?.setCanceledOnTouchOutside(false)
                loading?.setCancelable(false)
                loading?.show()
            }
        }

    }

    override fun hideLoadingUser(state: Int?) {
        when (state) {
            1 -> loading?.dismiss()
            2 -> {
                // post data
                loading?.dismiss()
            }
        }

    }

    override fun getDataUser(message: String?, data: User) {
        et_username.setText(data.username.toString())
        et_username.isEnabled = false
        et_nama.setText(data.nama.toString())

        if (!data.foto.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.foto)
                .placeholder(R.drawable.ic_profile_picture)
                .centerCrop()
                .into(image_profile)
        } else {
            image_tps.setImageResource(R.drawable.ic_profile_picture)
        }
    }

    override fun failedMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun successMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}