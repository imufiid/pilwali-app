package com.mufiid.pilwali2020.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.openBerandaActivity
import com.mufiid.pilwali2020.presenters.AuthPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
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
            if(username == "" || password == "") {
                var snackBar = Snackbar.make(
                    it, "Username dan Password tidak boleh kosong!",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null)
                    .setActionTextColor(Color.BLUE)
                val snackBarView = snackBar.view
                snackBarView.setBackgroundColor(Color.CYAN)
                val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLUE)
                snackBar.show()
            } else {
                // presenter?.login(username, password)
            }

        }
    }

    override fun successLogin(message: String?, user: User) {
        Constants.setIDUser(this, user.id.toString())
        Constants.setUsername(this, user.username.toString())
        Constants.setISLOGGEDIN(this, true)
        startActivity(Intent(this, BerandaActivity::class.java))
        finish()
    }

    override fun failedLogin(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun isLoading() {
        progressDialog?.setMessage("Tunggu sebentar")
        progressDialog?.show()
    }

    override fun hideLoading() {
        progressDialog?.dismiss()
    }

    private fun checkLogin() {
        if(Constants.ISLOGGEDIN(this)) {
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }
    }
}