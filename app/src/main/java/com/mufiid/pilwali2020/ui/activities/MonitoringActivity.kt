package com.mufiid.pilwali2020.ui.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.utils.Constants
import kotlinx.android.synthetic.main.activity_monitoring.*

@Suppress("DEPRECATION")
class MonitoringActivity : AppCompatActivity() {
    companion object {
        private var loading: ProgressDialog? = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        supportActionBar?.title = "Monitoring Perhitungan"
        loading = ProgressDialog(this)
        // set web view
        wv_monitoring.loadUrl(Constants.URL_WEBVIEW)
        wv_monitoring.settings.apply {
            javaScriptEnabled = true
        }
        wv_monitoring.webViewClient = WebViewClient()

        // ....
        loading?.setMessage("Tunggu sebentar...")
        loading?.setCancelable(false)
        loading?.show()
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
            //loading?.dismiss()
            loading?.dismiss()
        }
    }
}