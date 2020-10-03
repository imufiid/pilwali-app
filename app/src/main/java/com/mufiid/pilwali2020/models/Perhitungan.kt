package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class Perhitungan(

	@field:SerializedName("verified_by")
	val verifiedBy: Any? = null,

	@field:SerializedName("verifikasi")
	val verifikasi: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("verified_at")
	val verifiedAt: Any? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Any? = null,

	@field:SerializedName("id_tps")
	val idTps: String? = null,

	@field:SerializedName("suara_sah")
	val suaraSah: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_by")
	val createdBy: Any? = null,

	@field:SerializedName("id_paslon")
	val idPaslon: String? = null
)
