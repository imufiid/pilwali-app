package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class TpsPresenter(private val tpsView: ITpsView) {
    fun getDataTps(id_tps: String) {
        tpsView.isLoadingTps()
        ApiClient.instance().getDataTps(id_tps)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    200 -> tpsView.getDataTps(it.message, it.data!!)
                    else -> {
                        val msg = "Data Tps tidak ditemukan!"
                        tpsView.failedGetDataTps(it.message)
                    }
                }
                tpsView.hideLoadingTps()
            }, {
                tpsView.failedGetDataTps(it.message)
                tpsView.hideLoadingTps()
            })
    }
}