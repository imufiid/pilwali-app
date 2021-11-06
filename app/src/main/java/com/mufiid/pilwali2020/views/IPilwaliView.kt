package com.mufiid.pilwali2020.views

interface IPilwaliView {
    fun isLoadingPilwali()
    fun hideLoadingPilwali()

    fun success(message: String?, verification: Int?)
    fun failed(message: String?)
}