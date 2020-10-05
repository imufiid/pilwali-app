package com.mufiid.pilwali2020.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.utils.Constants

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OpenDialogFragment : DialogFragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var title: TextView? = null
    private var message: TextView? = null
    private var button: Button? = null
    private var button_cancel: Button? = null
    private var buttonListener: ButtonListener? = null
    private var message_dialog: String? = null
    private var title_dialog: String? = null
    private var state: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.title_message)
        message = view.findViewById(R.id.message)
        button = view.findViewById(R.id.btn_keluar)
        button_cancel = view.findViewById(R.id.btn_cancel)

        button?.setOnClickListener(this)
        button_cancel?.setOnClickListener(this)

        dialog?.setCanceledOnTouchOutside(false)
//        dialog?.setCancelable(false)
//        dialog?.setOnKeyListener { dialogInterface, i, keyEvent ->
//            if(i == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
//                // code
//                return@setOnKeyListener
//            }
//
//        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // get param
        if (savedInstanceState != null) {
            title_dialog = savedInstanceState.getString(TITLE_MESSAGE)
            message_dialog = savedInstanceState.getString(MESSAGE)
            state = savedInstanceState.getInt(STATE.toString(), 0)
        }

        if (arguments != null) {
            title_dialog = arguments?.getString(TITLE_MESSAGE)
            message_dialog = arguments?.getString(MESSAGE)
            state = arguments?.getInt(STATE.toString(), 0)

            title?.text = title_dialog
            message?.text = message_dialog
            if (state == 0) {
                button_cancel?.visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        var TITLE_MESSAGE = "title"
        var MESSAGE = "message"
        var STATE = 0

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_keluar -> {
                if (buttonListener != null) {
                    buttonListener?.choose()
                }
                dialog?.dismiss()
            }
            R.id.btn_cancel -> {
                dialog?.dismiss()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = parentFragment
        if (fragment is BerandaFragment) {
            val berandaFragment = fragment
            this.buttonListener = berandaFragment.buttonListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.buttonListener = null
    }

    interface ButtonListener {
        fun choose()
    }


}