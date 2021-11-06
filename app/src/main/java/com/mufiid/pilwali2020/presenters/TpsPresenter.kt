package com.mufiid.pilwali2020.presenters

import android.util.Log
import com.mufiid.pilwali2020.data.remote.api.ApiClient
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TpsPresenter(private val tpsView: ITpsView) {
    private val message = "Ada Gangguan di Server Kami"
    /**
     * fungsi untuk mengambil data detail tps by ID
     *
     * @author Imam Mufiid
     * @param id_tps => id tps
     * */
    fun getDataTps(id_tps: String) {
        tpsView.isLoadingTps(1)
        CompositeDisposable().add(
            ApiClient.instance().getDataTps(id_tps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        200 -> tpsView.getDataTps(it.message, it.data!!)
                        else -> tpsView.failedGetDataTps(it.message)
                    }
                    tpsView.hideLoadingTps(1)
                }, {
                    tpsView.failedGetDataTps(it.message)
                    tpsView.hideLoadingTps(1)
                })
        )
    }

    /**
     * fungsi untuk post data lat long dan foto TPS
     *
     * @author Imam Mufiid
     * @param id_tps => id TPS
     * @param form_page => jenis form page
     * @param fotoTPS => foto TPS
     * @param latitude => latitude TPS
     * @param longitude => longitude TPS
     * @param username => username user login
     *
     * */
    fun postData(
        id_tps: RequestBody?,
        form_page: RequestBody?,
        fotoTPS: MultipartBody.Part?,
        latitude: RequestBody?,
        longitude: RequestBody?,
        username: RequestBody?,
        apiKey: RequestBody?
    ) {
        tpsView.isLoadingTps(2)
        CompositeDisposable().add(
            ApiClient.instance()
                .inputDataTPS(id_tps, form_page, fotoTPS, latitude, longitude, username, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        201 -> tpsView.messageSuccess(it.message)
                        else -> tpsView.messageFailed(it.message)
                    }

                    tpsView.hideLoadingTps(2)
                }, {
                    tpsView.messageFailed(it.message)
                    tpsView.hideLoadingTps(2)
                })
        )

    }

    /**
     * fungsi untuk post jumlah pemilih by TPS
     *
     * @author Imam Mufiid
     * @param id_tps => id TPS
     * @param form_page => jenis form page
     * @param dpt => jumlah dpt
     * @param dptb => jumlah dptb
     * @param dpk => jumlah dpk
     * @param dpktb => jumlah dpktb
     * @param difabel => jumlah difabel
     * */
    fun postJumlahPemilih(
        id_tps: String?,
        form_page: String?,
        dpt: Int?,
        dptb: Int?,
        dpk: Int?,
        dph: Int?,
        username: String?,
        apiKey: String?
    ) {
        tpsView.isLoadingTps(2)
        CompositeDisposable().add(
            ApiClient.instance()
                .inputDaftarPemilih(id_tps, form_page, dpt, dptb, dpk, dph, username, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("HALOO", it.toString())
                    when (it.status) {
                        201 -> tpsView.messageSuccess(it.message)
                        400 -> tpsView.messageFailed(it.message)
                    }
                    tpsView.hideLoadingTps(2)
                }, {
                    tpsView.messageFailed(it.message)
                    tpsView.hideLoadingTps(2)
                })
        )
    }
}