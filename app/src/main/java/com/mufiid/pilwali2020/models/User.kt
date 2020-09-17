package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String? = null
)