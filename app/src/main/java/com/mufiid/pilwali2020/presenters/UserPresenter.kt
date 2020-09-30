package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IUserView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPresenter(private val userView: IUserView) {
    fun getUserByID(id_user: String) {
        userView.isLoadingUser(1)
        ApiClient.instance().getUserById(id_user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    200 -> {
                        userView.getDataUser(it.message, it.data!!)
                    }
                    else -> {
                         userView.failedMessage(it.message)
                    }
                }
                userView.hideLoadingUser(1)
            }, {
                userView.failedMessage(it.message)
                userView.hideLoadingUser(1)
            })

    }

    fun updateUser(id_user: String?, username: String?, nama: String?, passwordNew: String?) {
        userView.isLoadingUser(2)
        ApiClient.instance().updateProfile(id_user, username, nama, passwordNew)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    201 -> {
                        userView.successMessage(it.message)
                    }
                    400 -> {
                        userView.failedMessage(it.message)
                    }
                }
                userView.hideLoadingUser(2)
            },{
                userView.failedMessage(it.message)
                userView.hideLoadingUser(2)
            })

    }
}