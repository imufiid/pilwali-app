package com.mufiid.pilwali2020.utils.helpers

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.mufiid.pilwali2020.R

object CustomView {

    fun customToast(context: Context?, message: String?, isDurationShort: Boolean?, isSuccess: Boolean?) {
        val layoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.custom_toast, null)
        val text = view.findViewById<View>(R.id.text) as TextView
        val icon = view.findViewById<View>(R.id.icon) as ImageView

        isSuccess?.let {
            if(!it) {
                icon.setImageResource(R.drawable.ic_warning)
            }
        }


        text.text = message
        if (isDurationShort != null) {
            if (isDurationShort) {
                Toast(context).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    duration = Toast.LENGTH_SHORT
                    setView(view)
                }.show()
            } else {
                Toast(context).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    duration = Toast.LENGTH_LONG
                    setView(view)
                }.show()
            }
        }
    }
}