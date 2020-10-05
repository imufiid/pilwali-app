package com.mufiid.pilwali2020.ui.fragments

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.SliderAdapter
import com.mufiid.pilwali2020.models.Config
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.ConfigPresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.ui.activities.LoginActivity
import com.mufiid.pilwali2020.ui.activities.MonitoringActivity
import com.mufiid.pilwali2020.ui.activities.PilwaliActivity
import com.mufiid.pilwali2020.ui.activities.TpsActivity
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.utils.helpers.ConnectivityReceiver
import com.mufiid.pilwali2020.views.IConfigView
import com.mufiid.pilwali2020.views.ITpsView
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_beranda.*
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATION")
class BerandaFragment : Fragment(), ITpsView, IConfigView,
    ConnectivityReceiver.ConnectivityReceiverListener {
    private var param1: String? = null
    private var param2: String? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var presenter: TpsPresenter? = null
    private var configPresenter: ConfigPresenter? = null
    private var viewPager: ViewPager? = null
    private var indicator: CirclePageIndicator? = null
    private var currentPage = 0
    private var numPages = 0

    // var jumlah pemilih
    private var dpt: TextView? = null
    private var dptb: TextView? = null
    private var dpk: TextView? = null
    private var dpktb: TextView? = null
    private var difabel: TextView? = null
    private var tps: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_beranda, container, false)
        val btn_pilwali = root.findViewById<ImageButton>(R.id.btn_pilwali) as ImageButton
        val btn_tps = root.findViewById<ImageButton>(R.id.btn_tps) as ImageButton
        val btn_monitoring = root.findViewById<ImageButton>(R.id.btn_monitor) as ImageButton
        val btn_blangko = root.findViewById<ImageButton>(R.id.btn_blangko) as ImageButton
        //val image_header = root.findViewById<ImageView>(R.id.image_header) as ImageView
        shimmer =
            root.findViewById<View>(R.id.mShimmerViewContainer) as com.facebook.shimmer.ShimmerFrameLayout
        val jumlah_pemilih = root.findViewById<View>(R.id.jumlah_pemilih)
        val layout_title = root.findViewById<LinearLayout>(R.id.layout_title) as LinearLayout
        viewPager = root.findViewById(R.id.banner_viewPager) as ViewPager
        indicator = root.findViewById(R.id.indicator) as CirclePageIndicator

        // init id jumlah_pemilih
        dpt = root.findViewById<TextView>(R.id.dpt) as TextView
        dptb = root.findViewById<TextView>(R.id.dptb) as TextView
        dpk = root.findViewById<TextView>(R.id.dpk) as TextView
        dpktb = root.findViewById<TextView>(R.id.dpktb) as TextView
        difabel = root.findViewById<TextView>(R.id.difabel) as TextView
        tps = root.findViewById(R.id.subTitle) as TextView

        // init presenter
        presenter = TpsPresenter(this)
        configPresenter = ConfigPresenter(this)

        // slider
        // createImageSlider(Constants.imageSlider)


        btn_pilwali.setOnClickListener {
            startActivity(Intent(context, PilwaliActivity::class.java))
        }
        btn_tps.setOnClickListener {
            startActivity(Intent(context, TpsActivity::class.java))
        }
        btn_monitoring.setOnClickListener {
            startActivity(Intent(context, MonitoringActivity::class.java))
        }
        btn_blangko.setOnClickListener {
            Toast.makeText(context, "Blangko", Toast.LENGTH_SHORT).show()
        }

        /**
         * register receiver untuk check internet
         *
         * @author imam mufiid
         */
        requireActivity().registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        return root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BerandaFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BerandaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        presenter?.getDataTps(Constants.getIDTps(context!!))
        configPresenter?.config()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * bind to UI from API
     *
     * @author imam mufiid
     * @param message => message from response (success or fail)
     * @param data => data from response API
     * */
    override fun getDataTps(message: String?, data: Tps) {
        tps?.text = "TPS ${data.noTps} - ${data.kelurahan} ${data.kecamatan}"
        dpt?.text = data.dpt2
        dptb?.text = data.dptb2
        dpk?.text = data.dpk2
        dpktb?.text = data.dpktb2
        difabel?.text = data.difabel2
    }

    /**
     * failed to request data from API
     *
     * @author imam mufiid
     * @param message => message failed
     * */
    override fun failedGetDataTps(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun messageSuccess(message: String?) {
        TODO("Not yet implemented")
    }

    override fun messageFailed(message: String?) {
        TODO("Not yet implemented")
    }

    /**
     * process loading when get data TPS
     *
     * @author imam mufiid
     * */
    override fun isLoadingTps(state: Int?) {
        shimmer?.startShimmer()
        shimmer?.visibility = View.VISIBLE
        jumlah_pemilih.visibility = View.GONE
        layout_title.visibility = View.GONE
    }

    /**
     * end process loading when get data TPS
     *
     * @author imam mufiid
     * */
    override fun hideLoadingTps(state: Int?) {
        shimmer?.stopShimmer()
        shimmer?.visibility = View.GONE
        jumlah_pemilih.visibility = View.VISIBLE
        layout_title.visibility = View.VISIBLE
    }

    /**
     * check ketika koneksi internet berubah
     *
     * @author imam mufiid
     * */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    /**
     * bind ke UI ketika koneksi internet berubah
     *
     * @author imam mufiid
     * */
    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            Toast.makeText(context, "You're offline", Toast.LENGTH_SHORT).show()
        } else {
            // code ..
            // Toast.makeText(context, "You're online", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageSlider(string: List<String?>?) {
        viewPager?.adapter = SliderAdapter(context!!, string)
        indicator?.setViewPager(viewPager)
        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator?.radius = 5 * density
        numPages = string?.size ?: 0

        // Auto get Data
        val update = Runnable {
            if (currentPage === numPages) {
                currentPage = 0
            }
            viewPager?.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(update)
            }
        }, 5000, 5000)

        // Pager listener over indicator
        indicator?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // code ...
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // code ...
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

        })
    }

    override fun isLoadingConfig() {
        // code ..
    }

    override fun hideLoadingConfig() {
        // code ..
    }


    override fun getSuccessConfig(message: String?, data: Config) {
        createImageSlider(data.banner!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateConfig = LocalDateTime.parse(data.dDay, formatter)
            val currentDate = LocalDateTime.now()

            if (data.status == "production" && currentDate < dateConfig) {
                OpenDialog(
                    "Mohon Maaf!",
                    "Aplikasi belum dapat diakses sampai waktu yang ditentukan.",
                    0
                )
            } else {
                OpenDialog(
                    "Pemberitahuan!",
                    "Waktu Percobaan aplikasi kurang ${Duration.between(currentDate, dateConfig).toDays()} hari lagi",
                    1)
            }
        } else {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateConfig = formatter.parse(data.dDay)
            val diff = ((((dateConfig.time - Date().time)/1000)/60)/60)/24
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            if (data.status == "production" && Date() < dateConfig) {
                OpenDialog(
                    "Mohon Maaf!",
                    "Aplikasi belum dapat diakses sampai waktu yang ditentukan.",
                    0
                )
            } else {
                OpenDialog(
                    "Pemberitahuan!",
                    "Waktu Percobaan aplikasi kurang $diff hari lagi",
                    1)
            }
        }

    }

    override fun getFailedConfig(message: String?) {
        // code ..
    }

    private fun OpenDialog(title: String, message: String?, state: Int?) {
        val mBundle = Bundle().apply {
            putString(OpenDialogFragment.TITLE_MESSAGE, title)
            putString(
                OpenDialogFragment.MESSAGE,
                message
            )
            putInt(OpenDialogFragment.STATE.toString(), state!!)
        }
        OpenDialogFragment().apply {
            arguments = mBundle
            isCancelable = false
        }.show(
            childFragmentManager, OpenDialogFragment::class.java.simpleName
        )
    }

    internal var buttonListener: OpenDialogFragment.ButtonListener =
        object : OpenDialogFragment.ButtonListener {
            override fun choose() {
                Constants.clear(context!!)
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }

        }
}