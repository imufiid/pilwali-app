package com.mufiid.pilwali2020.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    override fun onResume() {
        super.onResume()
        userPresenter?.getUserByID(Constants.getIDUser(this))
    }

    override fun isLoadingUser() {
        loading?.setMessage("Tunggu sebentar...")
        loading?.show()
    }

    override fun hideLoadingUser() {
        loading?.dismiss()
    }

    override fun getDataUser(message: String?, data: User) {
        et_username.setText(data.username.toString())
        et_nama.setText(data.nama.toString())
    }

    override fun failedGetDataUser(message: String?) {
        TODO("Not yet implemented")
    }
}