package com.mufiid.pilwali2020.ui.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.presenters.UserPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IUserView
import kotlinx.android.synthetic.main.activity_edit_profile.*

@Suppress("DEPRECATION")
class EditProfileActivity : AppCompatActivity(), IUserView {
    private var userPresenter: UserPresenter? = null
    private var loading: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.title = "Edit Profil"
        userPresenter = UserPresenter(this)
        loading = ProgressDialog(this)

        btn_update_user.setOnClickListener {
            doUpdate()
        }
    }

    private fun doUpdate() {
        val nama = et_nama.text.toString()
        val passwdNew = et_password_baru.text.toString()
        val passwdOld = et_password_lama.text.toString()
        userPresenter?.updateUser(
            Constants.getIDUser(this),
            Constants.getUsername(this),
            nama,
            passwdNew
        )
    }

    override fun onResume() {
        super.onResume()
        userPresenter?.getUserByID(Constants.getIDUser(this))
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
    }

    override fun failedMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun successMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}