package com.mufiid.pilwali2020.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.utils.Constants
import kotlinx.android.synthetic.main.activity_monitoring.*

@Suppress("DEPRECATION")
class MonitoringActivity : AppCompatActivity() {
    companion object {
        var progressBar: ProgressBar? = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        supportActionBar?.title = resources.getString(R.string.title_monitoring_perhitungan)

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
}