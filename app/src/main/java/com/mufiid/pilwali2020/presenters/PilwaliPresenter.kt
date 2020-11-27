package com.mufiid.pilwali2020.presenters

import com.mufiid.pilwali2020.api.ApiClient
import com.mufiid.pilwali2020.views.IPilwaliView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PilwaliPresenter(private val pilwaliView: IPilwaliView) {
    private val message = "Ada Gangguan di Server Kami"
    /**
     * fungsi mengambil data tps sudah terverifikasi
     * atau belum
     *
     * @author Imam Mufiid
     * @param id_tps => id tps
     *
     * */
    fun getVerification(verif_tps: String?) {
        pilwaliView.isLoadingPilwali()
        CompositeDisposable().add(ApiClient.instance().getVerifikasi(verif_tps)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.status) {
                    200 -> pilwaliView.success(it.message, it.verification)
                    else -> pilwaliView.failed(it.message)
                }
                pilwaliView.hideLoadingPilwali()
            }, {
                pilwaliView.failed(it.message)
                pilwaliView.hideLoadingPilwali()
            }))
    }

}