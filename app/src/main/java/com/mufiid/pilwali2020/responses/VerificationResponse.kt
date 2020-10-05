package com.mufiid.pilwali2020.responses

import com.google.gson.annotations.SerializedName

data class VerificationResponse(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("verifikasi") var verification: Int? = null
)