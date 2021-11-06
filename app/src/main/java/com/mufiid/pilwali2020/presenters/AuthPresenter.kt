package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.data.remote.api.ApiClient
import com.mufiid.pilwali2020.views.IAuthView
import com.mufiid.pilwali2020.views.ILoadingView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthPresenter(private val authView: IAuthView, private val loading: ILoadingView) {
    private val message = "Ada Gangguan di Server Kami"
    /**
     * fungsi untuk login user
     *
     * @author Imam Mufiid
     * @param username => username user
     * @param password => password user
     *
     * */
    fun login(username: String?, password: String?) {
        loading.isLoading()
        CompositeDisposable().add(ApiClient.instance().login(username!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    200 -> authView.successLogin(it.message, it.data!!)
                    400 -> authView.failedLogin(it.message)
                    else -> authView.failedLogin(it.message)
                }
                loading.hideLoading()
            },{
                authView.failedLogin(it.message)
                loading.hideLoading()
            }))
    }
}