package com.mufiid.pilwali2020.responses

import com.google.gson.annotations.SerializedName

data class WrappedListResponses<T>(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var data: List<T>? = null
)