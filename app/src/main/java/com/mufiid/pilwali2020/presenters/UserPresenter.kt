package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IUserView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserPresenter(private val userView: IUserView) {

    /**
     * fungsi untuk mengambil data user by ID
     *
     * @author Imam Mufiid
     *
     * @param id_user => id user
     *
     * */
    fun getUserByID(id_user: String) {
        userView.isLoadingUser(1)
        CompositeDisposable().add(
            ApiClient.instance().getUserById(id_user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
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
        )

    }

    /**
     * fungsi untuk update data user by ID
     *
     * @author Imam Mufiid
     *
     * @param id_user => id user
     * @param username => username user
     * @param nama => nama user
     * @param passwordNew => password baru user
     *
     * */
    fun updateUser(
        id_user: RequestBody?,
        username: RequestBody?,
        nama: RequestBody?,
        foto: MultipartBody.Part?,
        passwordNew: RequestBody?,
        apiKey: RequestBody?
    ) {
        userView.isLoadingUser(2)
        CompositeDisposable().add(
            ApiClient.instance()
                .updateProfile(id_user, username, nama, foto, passwordNew, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> {
                            userView.successMessage(it.message)
                        }
                        400 -> {
                            userView.failedMessage(it.message)
                        }
                    }
                    userView.hideLoadingUser(2)
                }, {
                    userView.failedMessage(it.message)
                    userView.hideLoadingUser(2)
                })
        )

    }
}