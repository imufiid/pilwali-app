package com.mufiid.pilwali2020.utils

import android.content.Context

class Constants {
    companion object {
        const val API_ENDPOINT = "http://192.168.1.3/pilwali-2020/index.php/api/"
        val imageSlider = listOf(
            "https://cdn2.tstatic.net/wartakota/foto/bank/images/pilkada-serentak-2020a.jpg",
            "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/03/19/3779749248.jpg",
            "data:image/png;base64,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"
        )

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

        fun getIDUser(context: Context) : String {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("ID_USER", "undefined")!!
        }

        fun setIDTps(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("ID_TPS", username)
                apply()
            }
        }

        fun getIDTps(context: Context) : String {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("ID_TPS", "undefined")!!
        }

        fun setIDUser(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("ID_USER", username)
                apply()
            }
        }

        fun ISLOGGEDIN(context: Context) : Boolean {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getBoolean("ISLOGGEDIN", false)!!
        }

        fun setISLOGGEDIN(context: Context, state: Boolean) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putBoolean("ISLOGGEDIN", state)
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