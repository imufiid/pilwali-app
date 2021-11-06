package com.mufiid.pilwali2020.data.remote.response

import com.google.gson.annotations.SerializedName

data class PaslonResponse(
    @field:SerializedName("nm_peserta")
    var nmPeserta: String? = null,

    @field:SerializedName("foto")
    var foto: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("no_peserta")
    var noPeserta: String? = null,

    @field:SerializedName("jumlah_suara")
    var jumlah_suara: String? = null,

    @field:SerializedName("jumlah_suara_di_tps")
    var jumlah_suara_di_tps: String? = null
)
