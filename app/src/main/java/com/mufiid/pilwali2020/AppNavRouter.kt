package com.mufiid.pilwali2020

import android.content.Context
import android.content.Intent
import android.util.Log

const val PARENT_PACKAGE = "com.mufiid"
const val PACKAGE_LOGIN = "$PARENT_PACKAGE.login"
const val PACKAGE_REGISTER = "$PARENT_PACKAGE.register"
const val BUNDLE_KEY = "bundlekey"

fun openLoginActivity(context: Context, text: String) {
    try {
        // create intent
        val intent =
            Intent(context, Class.forName("$PACKAGE_LOGIN.LoginActivity"))
        // add data to activity
        intent.putExtra(BUNDLE_KEY, text)
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.d("navigation", "Activity not Found!")
    }
}