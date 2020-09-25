package com.mufiid.pilwali2020.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.PaslonAdapter
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.AddVotePresenter
import com.mufiid.pilwali2020.views.ILoadingView
import com.mufiid.pilwali2020.views.IPaslonView
import kotlinx.android.synthetic.main.activity_add_vote.*
import kotlinx.android.synthetic.main.activity_add_vote.open_camera
import kotlinx.android.synthetic.main.activity_tps.*

class AddVoteActivity : AppCompatActivity(), ILoadingView, IPaslonView {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var presenter: AddVotePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vote)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewManager = LinearLayoutManager(this)
        presenter = AddVotePresenter(this, this)


        open_camera.setOnClickListener {
            captureBlangko()
        }

        getPermission()
    }

    override fun onResume() {
        super.onResume()
        presenter?.getPaslon()
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

    override fun isLoading() {
        Log.i("LOADING", "Loading...")
    }

    override fun hideLoading() {
        Log.i("LOADING", "Selesai...")
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
}