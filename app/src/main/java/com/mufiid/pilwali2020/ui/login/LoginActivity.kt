package com.mufiid.pilwali2020.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mufiid.pilwali2020.MyApplication
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.data.entity.User
import com.mufiid.pilwali2020.databinding.ActivityLoginBinding
import com.mufiid.pilwali2020.presenters.AuthPresenter
import com.mufiid.pilwali2020.ui.main.BerandaActivity
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.utils.helpers.CustomView
import com.mufiid.pilwali2020.viewmodel.ViewModelFactory
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), IAuthView, ILoadingView, View.OnClickListener {
    private lateinit var _bind: ActivityLoginBinding
    private var presenter: AuthPresenter? = null
    private var progressDialog: ProgressDialog? = null
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: AuthViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        _bind = DataBindingUtil.setContentView(this, R.layout.activity_login)
        _bind.viewmodel = viewModel
        _bind.lifecycleOwner = this

        supportActionBar?.hide()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        // CompositeDisposable().clear()
    }

    override fun successLogin(message: String?, user: User) {
        Constants.setUserData(this, user)
        Constants.setISLOGGEDIN(this, true)

        showToast(resources.getString(R.string.success_login), true)

        Handler().postDelayed({
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }, 1000)
    }

    override fun failedLogin(message: String?) {
        showToast(message!!, false)
    }

    override fun isLoading() {
        progressDialog?.let {
            it.setMessage(getString(R.string.please_wait))
            it.setCanceledOnTouchOutside(false)
            it.show()
        }
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

    private fun showToast(message: String, isSuccess: Boolean?) {
        CustomView.customToast(this, message, true, isSuccess)
    }

    private fun init() {
        progressDialog = ProgressDialog(this)
        presenter = AuthPresenter(this, this)
        checkLogin()
        btn_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val username = et_username.text.toString()
                val password = et_password.text.toString()
                if (username == "" || password == "") {
                    showToast(getString(R.string.username_password_must_not_be_null), false)
                } else {
                    presenter?.login(username, password)
                }
            }
        }
    }
}