package com.mufiid.pilwali2020.presenters

import android.util.Log
import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(private val authView: IAuthView, private val loading: ILoadingView) {
    fun login(username: String?, password: String?) {
        loading.isLoading()
        ApiClient.instance().login(username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.status == 200) {
                    // code...
                    authView.successLogin(it.message, it.data!!)
                } else {
                    // code ...
                    authView.failedLogin(it.message)
                }
                loading.hideLoading()
            },{
                // code ...
                Log.d("AUTH ERROR", it.message!!)
                loading.hideLoading()
            })
    }
}