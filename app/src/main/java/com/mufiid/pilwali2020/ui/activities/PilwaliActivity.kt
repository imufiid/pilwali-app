package com.mufiid.pilwali2020.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.PilwaliPresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IPilwaliView
import com.mufiid.pilwali2020.views.ITpsView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_pilwali.*

@Suppress("DEPRECATION")
class PilwaliActivity : AppCompatActivity(), ITpsView, IPilwaliView {
    private var presenter: TpsPresenter? = null
    private var pilwaliPresenter: PilwaliPresenter? = null
    private var loading : ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilwali)

        supportActionBar?.title = resources.getString(R.string.title_pilwali)

        presenter = TpsPresenter(this)
        pilwaliPresenter = PilwaliPresenter(this)
        loading = ProgressDialog(this)

        btn_add.visibility = View.GONE
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
        val dph = jumlah_dph.text.toString()
        val userData = Constants.getUserData(this)
        if (dpt.isEmpty() || dptb.isEmpty() || dpk.isEmpty() || dph.isEmpty()) {
            Toast.makeText(this, getString(R.string.form_is_required), Toast.LENGTH_SHORT).show()
        } else {
            presenter?.postJumlahPemilih(
                userData?.idTps,
                "1",
                dpt.toInt(),
                dptb.toInt(),
                dpk.toInt(),
                dph.toInt(),
                userData?.username,
                userData?.api_key
            )
        }

    }

    override fun onResume() {
        super.onResume()
        Constants.getUserData(this)?.idTps?.let { presenter?.getDataTps(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
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
                loading?.setMessage(getString(R.string.please_wait))
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
                loading?.dismiss()
            }
        }

    }

    override fun getDataTps(message: String?, data: Tps) {
        jumlah_dpt.setText(data.dpt2)
        jumlah_dptb.setText(data.dptb2)
        jumlah_dpk.setText(data.dpk2)
        jumlah_dph.setText(data.dpph2)

        if(data.dpt2?.toInt()!! > 0) {
            btn_add.visibility = View.VISIBLE
            pilwaliPresenter?.getVerification(Constants.getUserData(this)?.idTps)
        }
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        btn_add.visibility = View.VISIBLE
    }

    override fun messageFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun isLoadingPilwali() {
        // code ..
    }

    override fun hideLoadingPilwali() {
        // code ..
    }

    override fun success(message: String?, verification: Int?) {
        Constants.setVerification(this, verification!!)
        when(verification) {
            1 -> {
                ic_verifikasi.setImageResource(R.drawable.ic_verification)
                tv_verifikasi.text = getString(R.string.verification)
                btn_simpan.visibility = View.GONE

                jumlah_dpt.isEnabled = false
                jumlah_dptb.isEnabled = false
                jumlah_dpk.isEnabled = false
                jumlah_dph.isEnabled = false
            }
            else -> {
                ic_verifikasi.setImageResource(R.drawable.ic_unverification)
                tv_verifikasi.text = getString(R.string.not_verification)
            }
        }
    }

    override fun failed(message: String?) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}