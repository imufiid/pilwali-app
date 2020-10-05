package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class Config(

	@field:SerializedName("banner")
	val banner: List<String?>? = null,

	@field:SerializedName("d_day")
	val dDay: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
