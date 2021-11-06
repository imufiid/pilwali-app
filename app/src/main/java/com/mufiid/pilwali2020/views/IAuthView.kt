package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.data.entity.User

interface IAuthView {
    fun successLogin(message: String?, user: User)
    fun failedLogin(message: String?)

}