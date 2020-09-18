package com.mufiid.pilwali2020.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.fragments.BerandaFragment
import com.mufiid.pilwali2020.fragments.ProfilFragment
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var doubleBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)
        supportActionBar?.hide()
        loadFragment(BerandaFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home ->{
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
}