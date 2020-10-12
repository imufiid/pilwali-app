package com.mufiid.pilwali2020.utils

import android.content.Context

class Constants {
    companion object {
        const val API_ENDPOINT = "http://pilwali.tobiaditia.my.id/Api/"
        const val URL_WEBVIEW = "http://pilwali.tobiaditia.my.id/publics/chart_perhitungan"
        const val URL_DOWNLOAD_BLANGKO = "http://pilwali.tobiaditia.my.id/assets/img/blanko/20201006_120640_JPEG_20201006_120618_3463935527490257966.jpg"

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

        fun getVerification(context: Context?) : Int {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getInt("VERIF", 0)!!
        }

        fun setVerification(context: Context, verification: Int) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putInt("VERIF", verification)
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