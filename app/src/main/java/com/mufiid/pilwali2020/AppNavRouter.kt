package com.mufiid.pilwali2020

import android.content.Context
import android.content.Intent
import android.util.Log

const val PARENT_PACKAGE = "com.mufiid"
const val PACKAGE_BERANDA = "$PARENT_PACKAGE.beranda"
const val BUNDLE_KEY = "bundlekey"

fun openBerandaActivity(context: Context) {
    try {
        // create intent
        val intent =
            Intent(context, Class.forName("$PACKAGE_BERANDA.BerandaActivity"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.d("navigation", "Activity not Found!")
    }
}