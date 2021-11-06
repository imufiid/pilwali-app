package com.mufiid.pilwali2020.utils

import android.content.Context
import androidx.core.content.edit
import com.mufiid.pilwali2020.data.entity.User

class Constants {
    companion object {
        const val URL_WEBVIEW = "http://pilkada2020.blitarkota.go.id/publics/chart_perhitungan"
        const val API_ENDPOINT = "http://192.168.1.3/projects/pilwali-2020/Api/"

        fun getUserData(context: Context): User? {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            return User().apply {
                nama = pref.getString("NAMA", "undefined")
                id = pref.getString("ID_USER", "undefined")
                username = pref.getString("USERNAME", "undefined")
                api_key = pref.getString("APIKEY", "undefined")
                idTps = pref.getString("ID_TPS", "undefined")
            }
        }

        fun setUserData(context: Context, user: User) {
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit {
                putString("NAMA", user.nama)
                putString("USERNAME", user.username)
                putString("APIKEY", user.api_key)
                putString("ID_USER", user.id)
                putString("ID_TPS", user.idTps)
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