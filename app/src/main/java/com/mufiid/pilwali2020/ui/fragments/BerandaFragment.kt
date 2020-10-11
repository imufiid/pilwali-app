package com.mufiid.pilwali2020.ui.fragments

import android.app.AlertDialog
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
    private var shimmerImageSlider: ShimmerFrameLayout? = null
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
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        val btnPilwali = view.findViewById<ImageButton>(R.id.btn_pilwali) as ImageButton
        val btnTps = view.findViewById<ImageButton>(R.id.btn_tps) as ImageButton
        val btnMonitoring = view.findViewById<ImageButton>(R.id.btn_monitor) as ImageButton
        val btnBlangko = view.findViewById<ImageButton>(R.id.btn_blangko) as ImageButton
        shimmer = view.findViewById<View>(R.id.mShimmerViewContainer) as com.facebook.shimmer.ShimmerFrameLayout
        shimmerImageSlider = view.findViewById<View>(R.id.shimmer_image_slider_container) as com.facebook.shimmer.ShimmerFrameLayout
        val jumlahPemilih = view.findViewById<View>(R.id.jumlah_pemilih)
        val layoutTitle = view.findViewById<LinearLayout>(R.id.layout_title) as LinearLayout
        viewPager = view.findViewById(R.id.banner_viewPager) as ViewPager
        indicator = view.findViewById(R.id.indicator) as CirclePageIndicator

        // init id jumlah_pemilih
        dpt = view.findViewById<TextView>(R.id.dpt) as TextView
        dptb = view.findViewById<TextView>(R.id.dptb) as TextView
        dpk = view.findViewById<TextView>(R.id.dpk) as TextView
        dpktb = view.findViewById<TextView>(R.id.dpktb) as TextView
        difabel = view.findViewById<TextView>(R.id.difabel) as TextView
        tps = view.findViewById(R.id.subTitle) as TextView

        // init presenter
        presenter = TpsPresenter(this)
        configPresenter = ConfigPresenter(this)

        // event listener
        btnPilwali.setOnClickListener {
            startActivity(Intent(context, PilwaliActivity::class.java))
        }
        btnTps.setOnClickListener {
            startActivity(Intent(context, TpsActivity::class.java))
        }
        btnMonitoring.setOnClickListener {
            startActivity(Intent(context, MonitoringActivity::class.java))
        }
        btnBlangko.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(resources.getString(R.string.sorry))
                setMessage(resources.getString(R.string.message_developement))
                    .setPositiveButton(resources.getString(R.string.close)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
            }
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
    }


    companion object {
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

    /**
     * fungsi untuk membuat image slider
     *
     * @author Imam Mufiid
     * @param string untuk menampung List image dgn tipe data string
     *
     * */
    private fun createImageSlider(string: List<String?>?) {
        viewPager?.adapter = SliderAdapter(context!!, string)
        indicator?.setViewPager(viewPager)
        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator?.radius = 5 * density
        numPages = string?.size ?: 0

        // Auto get Data
        val update = Runnable {
            if (currentPage == numPages) {
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

    /**
     * fungsi untuk melakukan proses loading get data config
     *
     * @author Imam Mufiid
     *
     * */
    override fun isLoadingConfig() {
        shimmerImageSlider?.visibility = View.VISIBLE
        shimmerImageSlider?.startShimmer()
    }

    /**
     * fungsi ketika proses loading get data config selesai
     *
     * @author Imam Mufiid
     *
     * */
    override fun hideLoadingConfig() {
        // code ..
        shimmerImageSlider?.stopShimmer()
        shimmerImageSlider?.visibility = View.GONE
    }

    /**
     * fungsi untuk mengambil data config dan bind ke UI
     *
     * @author Imam Mufiid
     * @param message berisi pesan success/failed dari API
     * @param data berisi data config dari database
     *
     * */
    override fun getSuccessConfig(message: String?, data: Config) {
        // memanggil fungsi untuk membuat image slider
        createImageSlider(data.banner!!)

        // checking android SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateConfig = LocalDateTime.parse(data.dDay, formatter)
            val currentDate = LocalDateTime.now()

            // checking status and date
            if (data.status == "production" && currentDate < dateConfig) {

                // memanggil fungsi untuk menampilkan dialog fragment
                OpenDialog(
                    "Mohon Maaf!",
                    "Aplikasi belum dapat diakses sampai waktu yang ditentukan.",
                    0
                )
            } else {

                // memanggil fungsi untuk menampilkan dialog fragment
//                OpenDialog(
//                    "Pemberitahuan!",
//                    "Waktu Percobaan aplikasi kurang ${Duration.between(currentDate, dateConfig).toDays()} hari lagi",
//                    1)
            }
        } else {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateConfig = formatter.parse(data.dDay)
            val diff = ((((dateConfig.time - Date().time)/1000)/60)/60)/24

            // checking status and date
            if (data.status == "production" && Date() < dateConfig) {

                // memanggil fungsi untuk menampilkan dialog fragment
                OpenDialog(
                    "Mohon Maaf!",
                    "Aplikasi belum dapat diakses sampai waktu yang ditentukan.",
                    0
                )
            } else {

                // memanggil fungsi untuk menampilkan dialog fragment
                OpenDialog(
                    "Pemberitahuan!",
                    "Waktu Percobaan aplikasi kurang $diff hari lagi",
                    1)
            }
        }

    }

    /**
     * fungsi ketika gagal mengambil data config dari database
     *
     * @author Imam Mufiid
     * @param message berisi pesan gagal
     *
     * */
    override fun getFailedConfig(message: String?) {
        // code ..
    }

    /**
     * fungsi untuk mengimplementasi dialog fragment
     *
     * @author Imam Mufiid
     * @param title untuk title dialog
     * @param message untuk pesan dialog
     * @param state state untuk menyimpan nilai state
     * */
    private fun OpenDialog(title: String, message: String?, state: Int?) {

        // menyimpan data untuk di passing ke Dialog Fragment
        val mBundle = Bundle().apply {
            putString(OpenDialogFragment.TITLE_MESSAGE, title)
            putString(
                OpenDialogFragment.MESSAGE,
                message
            )
            putInt(OpenDialogFragment.STATE.toString(), state!!)
        }

        // menampilkan dialog fragment
        OpenDialogFragment().apply {
            arguments = mBundle
            isCancelable = false
        }.show(
            childFragmentManager, OpenDialogFragment::class.java.simpleName
        )
    }

    // mengimplementasi interface button listener di dialog fragment
    internal var buttonListener: OpenDialogFragment.ButtonListener =
        object : OpenDialogFragment.ButtonListener {
            override fun choose() {
                Constants.clear(context!!)
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }

        }
}