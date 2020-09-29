package com.mufiid.pilwali2020.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.PaslonAdapter
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.AddVotePresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IPaslonView
import com.mufiid.pilwali2020.views.ITpsView
import kotlinx.android.synthetic.main.activity_add_vote.*
import kotlinx.android.synthetic.main.activity_add_vote.open_camera

class AddVoteActivity : AppCompatActivity(), IPaslonView, ITpsView {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var presenter: AddVotePresenter? = null
    private var tpsPresenter: TpsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perolehan Suara"
        viewManager = LinearLayoutManager(this)
        presenter = AddVotePresenter(this)
        tpsPresenter = TpsPresenter(this)


        open_camera.setOnClickListener {
            captureBlangko()
        }

        getPermission()
    }

    override fun onResume() {
        super.onResume()
        presenter?.getPaslon()
        tpsPresenter?.getDataTps(Constants.getIDTps(this))
    }

    private fun captureBlangko() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            image_blangko.setImageBitmap(imageBitmap)
        }
    }

    private fun getPermission() {
        // check permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(
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
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    override fun isLoadingPaslon() {
        shimmer_container.visibility = View.VISIBLE
        shimmer_container.startShimmer()
        title_upload_blangko.visibility = View.GONE
        div2.visibility = View.GONE
        layout_img.visibility = View.GONE
        btn_save.visibility = View.GONE
    }

    override fun hideLoadingPaslon() {
        shimmer_container.visibility = View.GONE
        shimmer_container.stopShimmer()
        title_upload_blangko.visibility = View.VISIBLE
        div2.visibility = View.VISIBLE
        layout_img.visibility = View.VISIBLE
        btn_save.visibility = View.VISIBLE
    }

    override fun getDataPaslon(message: String?, data: List<Paslon>?) {
        rv_paslon.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = PaslonAdapter(data!!)

        }
    }

    override fun failedGetDataPaslon(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun isLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.VISIBLE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.GONE
    }

    override fun hideLoadingTps(state: Int?) {
        shimmer_container_daftar_pemilih.visibility = View.GONE
        shimmer_container_daftar_pemilih.startShimmer()
        layout_daftar_pemilih.visibility = View.VISIBLE
    }

    override fun getDataTps(message: String?, data: Tps) {
        jumlah_dpt.text = data.dpt2
        jumlah_dptb.text = data.dptb2
        jumlah_dpk.text = data.dpk2
        jumlah_dpktb.text = data.dpktb2
        jumlah_difabel.text = data.difabel2
    }

    override fun failedGetDataTps(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        TODO("Not yet implemented")
    }

    override fun messageFailed(message: String?) {
        TODO("Not yet implemented")
    }
}