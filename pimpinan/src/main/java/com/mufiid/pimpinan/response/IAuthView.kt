package com.mufiid.pimpinan.response

import com.mufiid.pimpinan.models.User

interface IAuthView {
    fun isLoading()
    fun hideLoading()
    fun successLogin(message: String?, user: User)
    fun failedLogin(message: String?)

}