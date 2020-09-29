package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TpsPresenter(private val tpsView: ITpsView) {
    fun getDataTps(id_tps: String) {
        tpsView.isLoadingTps(1)
        ApiClient.instance().getDataTps(id_tps)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    200 -> tpsView.getDataTps(it.message, it.data!!)
                    else -> {
                        val msg = "Data Tps tidak ditemukan!"
                        tpsView.failedGetDataTps(it.message)
                    }
                }
                tpsView.hideLoadingTps(1)
            }, {
                tpsView.failedGetDataTps(it.message)
                tpsView.hideLoadingTps(1)
            })
    }

    fun postData(
        id_tps: RequestBody?,
        form_page: RequestBody?,
        fotoTPS: MultipartBody.Part,
        latitude: RequestBody?,
        longitude: RequestBody?,
        username: RequestBody?
    ) {
        tpsView.isLoadingTps(2)
        ApiClient.instance().inputDataTPS(id_tps, form_page, fotoTPS, latitude, longitude, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // code ...
                when (it.status) {
                    201 -> tpsView.messageSuccess(it.message)
                    else -> tpsView.messageFailed("Ada masalah")
                }

                tpsView.hideLoadingTps(2)
            }, {
                // code ...
                tpsView.messageFailed(it.message)
                tpsView.hideLoadingTps(2)
            })

    }

    fun postJumlahPemilih(
        id_tps: String?,
        form_page: String?,
        dpt: Int?,
        dptb: Int?,
        dpk: Int?,
        dpktb: Int?,
        difabel: Int?
    ) {
        tpsView.isLoadingTps(2)
        ApiClient.instance().inputDaftarPemilih(id_tps, form_page, dpt, dptb, dpk, dpktb, difabel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    201 -> tpsView.messageSuccess(it.message)
                    400 -> tpsView.messageFailed(it.message)
                }
                tpsView.hideLoadingTps(2)
            }, {
                tpsView.messageFailed(it.message)
                tpsView.hideLoadingTps(2)
            })

    }
}