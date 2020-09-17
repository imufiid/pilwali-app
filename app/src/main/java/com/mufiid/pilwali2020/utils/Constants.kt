package com.mufiid.pilwali2020.utils

import android.content.Context

class Constants {
    companion object {
        const val API_ENDPOINT = ""

        fun getUsername(context: Context) : String {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("USERNAME", "undefined")!!
        }

        fun setUsername(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("USERNAME", username)
                apply()
            }
        }

        fun clear(context: Context) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidPhone(phone: String) = phone.length >= 12
    }
}