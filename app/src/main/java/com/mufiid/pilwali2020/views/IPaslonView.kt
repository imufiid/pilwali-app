package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.data.entity.Paslon
import com.mufiid.pilwali2020.data.entity.Tps

interface IPaslonView {
    fun isLoadingPaslon(state: Int?)
    fun hideLoadingPaslon(state: Int?)
    fun getDataPaslon(message: String?, data: List<Paslon>?)
    fun failedGetDataPaslon(message: String?)
    fun successPostData(message: String?, data: Tps?)
}