package com.mufiid.pilwali2020.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.ITpsView
import kotlinx.android.synthetic.main.activity_pilwali.*

class PilwaliActivity : AppCompatActivity(), ILoadingView, ITpsView {
    private var presenter: TpsPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilwali)

        supportActionBar?.title = "Pilwali"

        presenter = TpsPresenter(this, this)

        btn_add.setOnClickListener {
            startActivity(Intent(this, AddVoteActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter?.getDataTps(Constants.getIDTps(this))
    }

    override fun isLoading() {
        mShimmerViewContainer.startShimmer()
        mShimmerViewContainer.visibility = View.VISIBLE
        layout_daftar_pemilih.visibility = View.GONE
        divider.visibility = View.GONE
        card_status.visibility = View.GONE

    }

    override fun hideLoading() {
        mShimmerViewContainer.stopShimmer()
        mShimmerViewContainer.visibility = View.GONE
        layout_daftar_pemilih.visibility = View.VISIBLE
        divider.visibility = View.VISIBLE
        card_status.visibility = View.VISIBLE
    }

    override fun getDataTps(message: String?, data: Tps) {
        Log.d("MESSAGE", message!!)
        jumlah_dpt.setText(data.dpt2)
        jumlah_dptb.setText(data.dptb2)
        jumlah_dpk.setText(data.dpk2)
        jumlah_dpktb.setText(data.dpk2)
        jumlah_difabel.setText(data.difabel2)
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}