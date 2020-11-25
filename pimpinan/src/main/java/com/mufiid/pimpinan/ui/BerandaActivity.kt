package com.mufiid.pimpinan.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import com.mufiid.pimpinan.R
import com.mufiid.pimpinan.utils.Constants
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity() {
    companion object {
        var progressBar: ProgressBar? = null
        private var doubleBack = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        checkLogin()
        init()
    }

    private fun init() {
        wv_monitoring?.let {
            it.loadUrl(Constants.URL_WEBVIEW)
            it.settings.apply {
                javaScriptEnabled = true
            }
            it.webViewClient = WebViewClient()
        }

        progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        progress_bar?.visibility = View.VISIBLE
    }

    private fun checkLogin() {
        if (!Constants.ISLOGGEDIN(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> false
        }
    }

    private fun logout() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.confirm_logout))
            setMessage(getString(R.string.question_logout))
                .setPositiveButton(getString(R.string.yes)) { _, _->
                    Constants.clear(context!!)
                    Toast.makeText(this@BerandaActivity, getString(R.string.success_logout), Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        startActivity(Intent(this@BerandaActivity, LoginActivity::class.java))
                        finish()
                    }, 1000)
                }
                .setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
        }.show()
    }

    open class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar?.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (doubleBack) {
            super.onBackPressed()
            return
        }

        doubleBack = true
        Toast.makeText(this, getString(R.string.double_back), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBack = false
        }, 2000)
    }
}