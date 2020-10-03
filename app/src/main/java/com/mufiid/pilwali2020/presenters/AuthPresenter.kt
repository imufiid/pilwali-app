package com.mufiid.pilwali2020.presenters

import android.util.Log
import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(private val authView: IAuthView, private val loading: ILoadingView) {

    /**
     * fungsi untuk login user
     *
     * @author Imam Mufiid
     *
     * @param username => username user
     * @param password => password user
     *
     * */
    fun login(username: String?, password: String?) {
        loading.isLoading()
        ApiClient.instance().login(username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    200 -> {
                        authView.successLogin(it.message, it.data!!)
                    }
                    400 -> {
                        val msg = "Password anda salah!"
                        authView.failedLogin(msg)
                    }
                    else -> {
                        val msg = "Username anda tidak ditemukan!"
                        authView.failedLogin(msg)
                    }
                }
                loading.hideLoading()
            },{
                // code ...
                Log.d("AUTH ERROR", it.message!!)
                loading.hideLoading()
            })
    }
}