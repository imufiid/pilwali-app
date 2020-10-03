package com.mufiid.pilwali2020.models

import com.google.gson.annotations.SerializedName

data class Tps(
    @field:SerializedName("kecamatan")
    val kecamatan: String,

    @field:SerializedName("kelurahan")
    val kelurahan: String,

    @field:SerializedName("dpk_1")
    val dpk1: Any? = null,

    @field:SerializedName("no_tps")
    val noTps: String? = null,

    @field:SerializedName("dpk_2")
    val dpk2: String? = null,

    @field:SerializedName("dpktb_2")
    val dpktb2: String? = null,

    @field:SerializedName("dptb_2")
    val dptb2: String? = null,

    @field:SerializedName("id_wilayah")
    val idWilayah: String? = null,

    @field:SerializedName("dptb_1")
    val dptb1: Any? = null,

    @field:SerializedName("longi")
    val longi: Any? = null,

    @field:SerializedName("dpt_2")
    val dpt2: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("dpt_1")
    val dpt1: Any? = null,

    @field:SerializedName("suara_tidak_sah")
    val suara_tidak_sah: String? = null,

    @field:SerializedName("difabel_2")
    val difabel2: String? = null,

    @field:SerializedName("foto_blanko")
    val foto_blanko: String? = null,

    @field:SerializedName("foto_tps")
    val foto_tps: String? = null,

    @field:SerializedName("difabel_1")
    val difabel1: Any? = null,

    @field:SerializedName("lati")
    val lati: Any? = null,

    @field:SerializedName("dpktb_1")
    val dpktb1: Any? = null,

    @field:SerializedName("id")
    val id: String? = null
)