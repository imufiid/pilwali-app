package com.mufiid.pilwali2020.presenters

import android.util.Log
import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.IPaslonView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AddVotePresenter(private val paslonView: IPaslonView) {
    fun getPaslon() {
        paslonView.isLoadingPaslon()
        ApiClient.instance().getPaslon()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    200 -> {
                        paslonView.getDataPaslon(it.message, it.data!!)
                    }
                    400 -> {
                        val msg = "Password anda salah!"
                        paslonView.failedGetDataPaslon(it.message)
                    }
                    else -> {
                        val msg = "Username anda tidak ditemukan!"
                        paslonView.failedGetDataPaslon(it.message)
                    }
                }
                paslonView.hideLoadingPaslon()
            },{
                // code ...
                Log.d("AUTH ERROR", it.message!!)
                paslonView.hideLoadingPaslon()
            })
    }
}