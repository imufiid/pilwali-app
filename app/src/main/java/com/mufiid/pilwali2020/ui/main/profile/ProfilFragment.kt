package com.mufiid.pilwali2020.ui.main.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.BuildConfig
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.data.entity.User
import com.mufiid.pilwali2020.presenters.UserPresenter
import com.mufiid.pilwali2020.ui.editprofile.EditProfileActivity
import com.mufiid.pilwali2020.ui.login.LoginActivity
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IUserView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_tps.*
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment : Fragment(), IUserView {
    private var userPresenter: UserPresenter? = null
    private var tvUsername: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPresenter = UserPresenter(this)

        val editProfile = view.findViewById<LinearLayout>(R.id.editProfile) as LinearLayout
        val logout = view.findViewById<LinearLayout>(R.id.logout) as LinearLayout
        tvUsername = view.findViewById(R.id.username_user) as TextView
        val appVersion = view.findViewById<TextView>(R.id.app_version)

        appVersion.text = getString(R.string.app_versions, BuildConfig.VERSION_NAME)

        editProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        tvUsername?.text = Constants.getUserData(requireContext())?.nama

        logout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(getString(R.string.confirm_logout))
                setMessage(getString(R.string.question_logout))
                    .setPositiveButton(getString(R.string.yes)) { _, _->
                        Constants.clear(requireContext())
                        startActivity(Intent(context, LoginActivity::class.java))
                        activity?.finish()
                    }
                    .setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
            }.show()

        }
    }

    override fun onResume() {
        super.onResume()
        Constants.getUserData(requireContext())?.id?.let {
            userPresenter?.getUserByID(it)
        }
    }

    override fun isLoadingUser(state: Int?) {
        // code...
    }

    override fun hideLoadingUser(state: Int?) {
        // code ...
    }

    override fun getDataUser(message: String?, data: User) {

        tvUsername?.text = data.nama
        if (!data.foto.isNullOrEmpty()) {
            Glide.with(this)
                .load(data.foto)
                .placeholder(R.drawable.ic_profile_picture)
                .centerCrop()
                .into(profile_picture)
        }else {
            image_tps.setImageResource(R.drawable.ic_profile_picture)
        }
    }

    override fun failedMessage(message: String?) {
        // code ..
    }

    override fun successMessage(message: String?) {
        // code ...
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }
}