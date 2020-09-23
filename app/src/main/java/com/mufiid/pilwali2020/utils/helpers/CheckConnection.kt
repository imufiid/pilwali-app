@file:Suppress("DEPRECATION")

package com.mufiid.pilwali2020.utils.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object CheckConnection {
    fun check(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}