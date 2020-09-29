package com.mufiid.pilwali2020.views

interface IPilwaliView {
    fun isLoadingPilwali()
    fun hideLoadingPilwali()

    fun successAdd(message: String?)
    fun failedAdd(message: String?)
}