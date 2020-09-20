package com.mufiid.pilwali2020.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufiid.pilwali2020.R
import kotlinx.android.synthetic.main.activity_add_vote.*

class AddVoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)

        rv_paslon.isNestedScrollingEnabled = false
    }
}