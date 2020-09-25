package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.Tps

interface ITpsView {
    fun getDataTps(message: String?, data: Tps)
    fun failedGetDataTps(message: String?)
}