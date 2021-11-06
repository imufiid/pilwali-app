package com.mufiid.pilwali2020.data.remote.response

import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @field:SerializedName("banner")
    val banner: List<String?>? = null,

    @field:SerializedName("d_day")
    val dDay: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("blanko")
    val blanko: String? = null
)
