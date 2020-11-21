package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
class EditProfileActivity : AppCompatActivity(), IUserView, View.OnClickListener {
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

        supportActionBar?.title = getString(R.string.edit_account)
        init()

    }

    private fun init() {
        userPresenter = UserPresenter(this)
        loading = ProgressDialog(this)
        btn_update_user.setOnClickListener(this)
        open_galery.setOnClickListener(this)

        Constants.getUserData(this)?.id?.let {
            userPresenter?.getUserByID(it)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c = contentResolver.query(uri!!, filePath, null, null, null)
            c?.moveToFirst()
            val columnIndex = c?.getColumnIndex(filePath[0])
            val picturePath = c?.getString(columnIndex!!)
            c?.close()
            // var thumbnail = BitmapFactory.decodeFile(picturePath)
            currentPhotoPath = picturePath
            image_profile.setImageURI(uri)
        }
    }

    private fun doUpdate() {
        val userData = Constants.getUserData(this)
        val id = userData?.id?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val username =
            userData?.username?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val apiKey =
            userData?.api_key?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val nama = RequestBody.create("text/plain".toMediaTypeOrNull(), et_nama.text.toString())
        val passwdNew =
            RequestBody.create("text/plain".toMediaTypeOrNull(), et_password_baru.text.toString())
        val passwdOld =
            RequestBody.create("text/plain".toMediaTypeOrNull(), et_password_lama.text.toString())
        if (currentPhotoPath != "") {
            // ketika user capture foto
            val pictFromBitmap = File(currentPhotoPath)
            val reqFile: RequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), pictFromBitmap)
            part = MultipartBody.Part.createFormData("foto", pictFromBitmap.name, reqFile)
        }
        userPresenter?.updateUser(id, username, nama, part, passwdNew, apiKey)
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }

    override fun isLoadingUser(state: Int?) {
        loading?.setMessage(getString(R.string.please_wait))
        if (state == 2) {
            // post data
            loading?.setCanceledOnTouchOutside(false)
            loading?.setCancelable(false)

        }
        loading?.show()

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_update_user -> doUpdate()
            R.id.open_galery -> {
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
        }
    }
}