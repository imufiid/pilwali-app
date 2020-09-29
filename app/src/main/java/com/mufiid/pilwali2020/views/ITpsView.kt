package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.Tps

interface ITpsView {
    fun isLoadingTps(state: Int?)
    fun hideLoadingTps(state: Int?)

    fun getDataTps(message: String?, data: Tps)
    fun failedGetDataTps(message: String?)

    fun messageSuccess(message: String?)
    fun messageFailed(message: String?)
}