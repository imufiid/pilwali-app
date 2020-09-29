package com.mufiid.pilwali2020.ui.activities

import android.app.ProgressDialog
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
import com.mufiid.pilwali2020.views.ITpsView
import kotlinx.android.synthetic.main.activity_pilwali.*

@Suppress("DEPRECATION")
class PilwaliActivity : AppCompatActivity(), ITpsView {
    private var presenter: TpsPresenter? = null
    private var loading : ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilwali)

        supportActionBar?.title = "Pilwali"

        presenter = TpsPresenter(this)
        loading = ProgressDialog(this)

        btn_add.setOnClickListener {
            startActivity(Intent(this, AddVoteActivity::class.java))
        }

        btn_simpan.setOnClickListener {
            postDaftarPemilih()
        }
    }

    private fun postDaftarPemilih() {
        val dpt = jumlah_dpt.text.toString()
        val dptb = jumlah_dptb.text.toString()
        val dpk = jumlah_dpk.text.toString()
        val dpktb = jumlah_dpktb.text.toString()
        val difabel = jumlah_difabel.text.toString()
        if (dpt.isEmpty() || dptb.isEmpty() || dpk.isEmpty() || dpktb.isEmpty() || difabel.isEmpty()) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            presenter?.postJumlahPemilih(
                Constants.getIDTps(this),
                "1",
                dpt.toInt(),
                dptb.toInt(),
                dpk.toInt(),
                dpktb.toInt(),
                difabel.toInt()
            )
        }

    }

    override fun onResume() {
        super.onResume()
        presenter?.getDataTps(Constants.getIDTps(this))
    }

    override fun isLoadingTps(state: Int?) {
        when(state) {
            1 -> {
                mShimmerViewContainer.startShimmer()
                mShimmerViewContainer.visibility = View.VISIBLE
                layout_daftar_pemilih.visibility = View.GONE
                divider.visibility = View.GONE
                card_status.visibility = View.GONE
            }
            2 -> {
                // code ...
                loading?.setMessage("Mohon tunggu sebentar...")
                loading?.show()
            }
        }


    }

    override fun hideLoadingTps(state: Int?) {
        when(state) {
            1 -> {
                mShimmerViewContainer.stopShimmer()
                mShimmerViewContainer.visibility = View.GONE
                layout_daftar_pemilih.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
                card_status.visibility = View.VISIBLE
            }
            2 -> {
                // ...
                loading?.dismiss()
            }
        }

    }

    override fun getDataTps(message: String?, data: Tps) {
        Log.d("MESSAGE", message!!)
        jumlah_dpt.setText(data.dpt2)
        jumlah_dptb.setText(data.dptb2)
        jumlah_dpk.setText(data.dpk2)
        jumlah_dpktb.setText(data.dpktb2)
        jumlah_difabel.setText(data.difabel2)
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}