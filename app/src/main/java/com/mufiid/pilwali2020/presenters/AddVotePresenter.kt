package com.mufiid.pilwali2020.presenters

import android.util.Log
import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.IPaslonView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddVotePresenter(private val paslonView: IPaslonView) {

    /**
     * mengambil data paslon
     *
     * @author Imam Mufiid
     * */
    fun getPaslon() {
        paslonView.isLoadingPaslon(1)
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
                paslonView.hideLoadingPaslon(1)
            }, {
                // code ...
                paslonView.hideLoadingPaslon(1)
            })
    }

    /**
     * post data perolehan suara
     *
     * @author Imam Mufiid
     *
     * @param id_tps => id tps berdasarkan user
     * @param suara_tidak_sah => jumlah suara tidak sah di tps bersangkutan
     * @param id_paslon => id masing-masing paslon
     * @param suara_sah => jumlah suara sah masing-masing paslon
     * @param foto => upload foto blangko
     * @param username => username user yg sedang login
     *
     * */
    fun postDataPerolehanSuara(
        id_tps: RequestBody?,
        suara_tidak_sah: RequestBody?,
        id_paslon: List<Int>?,
        suara_sah: List<Int>,
        foto: MultipartBody.Part?,
        username: RequestBody?
    ) {
        paslonView.isLoadingPaslon(2)
        ApiClient.instance().postSuaraPaslon(id_tps, suara_tidak_sah, id_paslon, suara_sah, foto, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.status) {
                    201 -> {
                        paslonView.failedGetDataPaslon(it.message)
                    }
                    400 -> {
                        paslonView.failedGetDataPaslon(it.message)
                    }
                }
                paslonView.hideLoadingPaslon(2)
            }, {
                paslonView.failedGetDataPaslon(it.message)
                paslonView.hideLoadingPaslon(2)
            })

    }
}