package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class Paslon(

	@field:SerializedName("nm_peserta")
	val nmPeserta: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("no_peserta")
	val noPeserta: String? = null,

	@field:SerializedName("jumlah_suara")
    var jumlah_suara: String? = null
)
