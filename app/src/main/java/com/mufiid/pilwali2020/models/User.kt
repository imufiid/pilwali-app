package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("is_active")
    val isActive: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("no_hp")
    val noHp: String? = null,

    @field:SerializedName("jabatan")
    val jabatan: String? = null,

    @field:SerializedName("id_tps")
    val idTps: String? = null,

    @field:SerializedName("id_wilayah")
    val idWilayah: String? = null,

    @field:SerializedName("id_role")
    val idRole: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("date_registered")
    val dateRegistered: Any? = null,

    @field:SerializedName("username")
    val username: String? = null
)