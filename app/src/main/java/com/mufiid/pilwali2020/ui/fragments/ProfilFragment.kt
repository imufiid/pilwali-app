package com.mufiid.pilwali2020.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mufiid.pilwali2020.BuildConfig
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.ui.activities.EditProfileActivity
import com.mufiid.pilwali2020.ui.activities.LoginActivity
import com.mufiid.pilwali2020.utils.Constants
import org.w3c.dom.Text

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProfile = view.findViewById<LinearLayout>(R.id.editProfile) as LinearLayout
        val logout = view.findViewById<LinearLayout>(R.id.logout) as LinearLayout
        val tvUsername = view.findViewById<TextView>(R.id.username_user) as TextView
        val appVersion = view.findViewById<TextView>(R.id.app_version)

        appVersion.text = "App Version - ${BuildConfig.VERSION_NAME}"

        editProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        tvUsername.text = Constants.getUsername(context!!)

        logout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Konfirmasi keluar")
                setMessage("Anda yakin keluar?")
                    .setPositiveButton("Iya") { dialogInterface, i ->
                        Constants.clear(context!!)
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                    .setNegativeButton("Tidak") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
            }.show()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}