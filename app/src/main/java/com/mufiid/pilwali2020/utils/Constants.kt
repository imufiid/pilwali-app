package com.mufiid.pilwali2020.utils

import android.content.Context
import androidx.core.content.edit

class Constants {
    companion object {
        const val API_ENDPOINT = "http://pilkada2020.blitarkota.go.id/Api/"
        const val URL_WEBVIEW = "http://pilkada2020.blitarkota.go.id/publics/chart_perhitungan"

        fun getUsername(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("USERNAME", "undefined")
        }

        fun setUsername(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("USERNAME", username)
                apply()
            }
        }

        fun getApiKey(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("APIKEY", "undefined")
        }

        fun setApiKey(context: Context, apiKey: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putString("APIKEY", apiKey)
            }
        }

        fun getIDUser(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("ID_USER", "undefined")
        }

        fun setIDTps(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putString("ID_TPS", username)
            }
        }

        fun getVerification(context: Context?): Int? {
            val pref = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref?.getInt("VERIF", 0)
        }

        fun setVerification(context: Context, verification: Int) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putInt("VERIF", verification)
            }
        }

        fun getIDTps(context: Context): String? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getString("ID_TPS", "undefined")
        }

        fun setIDUser(context: Context, username: String) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putString("ID_USER", username)
            }
        }

        fun ISLOGGEDIN(context: Context): Boolean {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return pref.getBoolean("ISLOGGEDIN", false)
        }

        fun setISLOGGEDIN(context: Context, state: Boolean) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putBoolean("ISLOGGEDIN", state)
            }
        }

        fun clear(context: Context) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit{
                clear()
            }
        }

        fun isValidPhone(phone: String) = phone.length >= 12
    }
}