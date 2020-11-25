package com.mufiid.pimpinan.response

import com.mufiid.pimpinan.api.ApiClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(private val authView: IAuthView) {

    /**
     * fungsi untuk login user
     *
     * @author Imam Mufiid
     * @param username => username user
     * @param password => password user
     *
     * */
    fun login(username: String?, password: String?) {
        authView.isLoading()
        CompositeDisposable().add(
            ApiClient.instance().login(username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    200 -> authView.successLogin(it.message, it.data!!)
                    400 -> authView.failedLogin(it.message)
                    else -> authView.failedLogin(it.message)
                }
                authView.hideLoading()
            },{
                authView.failedLogin(it.message)
                authView.hideLoading()
            }))
    }
}