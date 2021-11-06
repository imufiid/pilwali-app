package com.mufiid.pilwali2020.data.remote.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    var message: String? = null,

    @SerializedName("status")
    var status: Int? = null
)