package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.User


interface IUserView {
    fun isLoadingUser()
    fun hideLoadingUser()

    fun getDataUser(message: String?, data: User)
    fun failedGetDataUser(message: String?)
}