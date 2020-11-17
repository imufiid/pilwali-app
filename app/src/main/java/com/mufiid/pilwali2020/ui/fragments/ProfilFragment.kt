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

class ProfilFragment : Fragment() {

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

        appVersion.text = "${getString(R.string.app_versions, BuildConfig.VERSION_NAME)} \n ${getString(R.string.copyright)}"

        editProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        tvUsername.text = Constants.getUserData(context!!)?.username

        logout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(getString(R.string.confirm_logout))
                setMessage(getString(R.string.question_logout))
                    .setPositiveButton(getString(R.string.yes)) { _, _->
                        Constants.clear(context!!)
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                    .setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
            }.show()

        }
    }
}