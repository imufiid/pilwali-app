package com.mufiid.pilwali2020.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.pilwali2020.R
import kotlinx.android.synthetic.main.activity_pilwali.*

class PilwaliActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilwali)

        supportActionBar?.title = "Pilwali"

        btn_add.setOnClickListener {
            startActivity(Intent(this, AddVoteActivity::class.java))
        }
    }

    private fun captureBlangko() {

    }
}