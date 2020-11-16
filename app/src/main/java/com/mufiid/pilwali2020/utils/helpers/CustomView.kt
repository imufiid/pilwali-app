package com.mufiid.pilwali2020.utils.helpers

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mufiid.pilwali2020.R

object CustomView {

    fun customToast(context: Context?, message: String?, duration: Boolean?) {
        val layoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.custom_toast, null)
        val text = view.findViewById<View>(R.id.text) as TextView

        text.text = message
        if (duration != null) {
            if (duration) {
                Toast(context).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    setDuration(Toast.LENGTH_SHORT)
                    setView(view)
                }.show()
            } else {
                Toast(context).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    setDuration(Toast.LENGTH_LONG)
                    setView(view)
                }.show()
            }
        }
    }
}