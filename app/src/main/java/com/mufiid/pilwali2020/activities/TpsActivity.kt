package com.mufiid.pilwali2020.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.pilwali2020.R

class TpsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "TPS"
    }
}