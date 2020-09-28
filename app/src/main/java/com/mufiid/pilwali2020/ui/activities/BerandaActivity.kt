package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.ui.fragments.BerandaFragment
import com.mufiid.pilwali2020.ui.fragments.ProfilFragment
import com.mufiid.pilwali2020.utils.Constants
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var doubleBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)
        supportActionBar?.hide()
        loadFragment(BerandaFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // check
        checkPermission()
        checkLogin()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(BerandaFragment())
                return true
            }
            R.id.nav_profile -> {
                loadFragment(ProfilFragment())
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (doubleBack) {
            super.onBackPressed()
            return
        }

        this.doubleBack = true
        Toast.makeText(this, "Tekan kembali lagi untuk keluar!", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBack = false
        }, 2000)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            // .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    private fun checkLogin() {
        if (!Constants.ISLOGGEDIN(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}