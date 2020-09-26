package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IUserView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPresenter(private val userView: IUserView) {
    fun getUserByID(id_user: String) {
        userView.isLoadingUser()
        ApiClient.instance().getUserById(id_user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    200 -> {
                        userView.getDataUser(it.message, it.data!!)
                    }
                    else -> {
                         userView.failedGetDataUser(it.message)
                    }
                }
                userView.hideLoadingUser()
            }, {
                userView.failedGetDataUser(it.message)
                userView.hideLoadingUser()
            })

    }
}