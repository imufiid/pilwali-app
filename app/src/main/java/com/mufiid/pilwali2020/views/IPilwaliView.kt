package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.Perhitungan

interface IPilwaliView {
    fun isLoadingPilwali()
    fun hideLoadingPilwali()

    fun success(message: String?, data: Perhitungan?)
    fun failed(message: String?)
}