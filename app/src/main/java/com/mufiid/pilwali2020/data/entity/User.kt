package com.mufiid.pilwali2020.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var password: String? = null,
    var isActive: String? = null,
    var nama: String? = null,
    var noHp: String? = null,
    var jabatan: String? = null,
    var idTps: String? = null,
    var idWilayah: String? = null,
    var idRole: String? = null,
    var id: String? = null,
    var dateRegistered: String? = null,
    var username: String? = null,
    var api_key: String? = null,
    var foto: String? = null
): Parcelable