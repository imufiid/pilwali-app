package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.Paslon

interface IPaslonView {
    fun getDataPaslon(message: String?, data: List<Paslon>?)
    fun failedGetDataPaslon(message: String?)
}