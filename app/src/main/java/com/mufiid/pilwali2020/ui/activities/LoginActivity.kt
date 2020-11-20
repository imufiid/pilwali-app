package com.mufiid.pilwali2020.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.presenters.AuthPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.utils.helpers.CustomView
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), IAuthView, ILoadingView {
    private var presenter: AuthPresenter? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        progressDialog = ProgressDialog(this)
        presenter = AuthPresenter(this, this)
        checkLogin()

        btn_login.setOnClickListener {
            val username = et_username.text.toString()
            val password = et_password.text.toString()
            if (username == "" || password == "") {
                showToast(getString(R.string.username_password_must_not_be_null))
            } else {
                presenter?.login(username, password)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }

    override fun successLogin(message: String?, user: User) {
        Constants.setUserData(this, user)
        Constants.setISLOGGEDIN(this, true)

        showToast(resources.getString(R.string.success_login))

        Handler().postDelayed({
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }, 1000)
    }

    override fun failedLogin(message: String?) {
        showToast(message!!)
    }

    override fun isLoading() {
        progressDialog?.setMessage(getString(R.string.please_wait))
        progressDialog?.show()
    }

    override fun hideLoading() {
        progressDialog?.dismiss()
    }

    private fun checkLogin() {
        if (Constants.ISLOGGEDIN(this)) {
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }
    }

    private fun showToast(message: String) {
        CustomView.customToast(this, message, true)
    }
}