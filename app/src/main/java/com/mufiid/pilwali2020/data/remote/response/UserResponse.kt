package com.mufiid.pilwali2020.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("password")
    var password: String? = null,

    @field:SerializedName("is_active")
    var isActive: String? = null,

    @field:SerializedName("nama")
    var nama: String? = null,

    @field:SerializedName("no_hp")
    var noHp: String? = null,

    @field:SerializedName("jabatan")
    var jabatan: String? = null,

    @field:SerializedName("id_tps")
    var idTps: String? = null,

    @field:SerializedName("id_wilayah")
    var idWilayah: String? = null,

    @field:SerializedName("id_role")
    var idRole: String? = null,

    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("date_registered")
    var dateRegistered: String? = null,

    @field:SerializedName("username")
    var username: String? = null,

    @field:SerializedName("api_key")
    var api_key: String? = null,

    @field:SerializedName("foto")
    var foto: String? = null
)
