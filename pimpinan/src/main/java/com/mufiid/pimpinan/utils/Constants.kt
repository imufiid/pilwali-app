package com.mufiid.pimpinan.utils

import android.content.Context
import androidx.core.content.edit
import com.mufiid.pimpinan.models.User

class Constants {
    companion object {
        const val API_ENDPOINT = "http://pilkada2020.blitarkota.go.id/Api/"
        const val URL_WEBVIEW = "http://192.168.1.2/pilwali-2020/publics/isc_mobile"

        fun getUserData(context: Context): User? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return User().apply {
                id = pref.getString("ID_USER", "undefined")
                username = pref.getString("USERNAME", "undefined")
                api_key = pref.getString("APIKEY", "undefined")
                idTps = pref.getString("ID_TPS", "undefined")
            }
        }

        fun setUserData(context: Context, user: User) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putString("USERNAME", user.username)
                putString("APIKEY", user.api_key)
                putString("ID_USER", user.id)
                putString("ID_TPS", user.idTps)
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
            pref.edit {
                clear()
            }
        }
    }
}