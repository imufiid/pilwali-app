package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.User


interface IUserView {
    fun isLoadingUser(state: Int?)
    fun hideLoadingUser(state: Int?)

    fun getDataUser(message: String?, data: User)
    fun failedMessage(message: String?)
    fun successMessage(message: String?)
}