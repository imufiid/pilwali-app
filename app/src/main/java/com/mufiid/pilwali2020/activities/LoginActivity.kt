package com.mufiid.pilwali2020.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.openBerandaActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        btn_login.setOnClickListener {
//            openBerandaActivity(this)
            startActivity(Intent(this, BerandaActivity::class.java).also { finish() })
        }
    }
}