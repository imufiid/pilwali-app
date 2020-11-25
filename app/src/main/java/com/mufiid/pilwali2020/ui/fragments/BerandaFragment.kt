package com.mufiid.pilwali2020.ui.fragments

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.adapters.PaslonAdapter
import com.mufiid.pilwali2020.adapters.SliderAdapter
import com.mufiid.pilwali2020.models.Config
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.AddVotePresenter
import com.mufiid.pilwali2020.presenters.ConfigPresenter
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.ui.activities.LoginActivity
import com.mufiid.pilwali2020.ui.activities.MonitoringActivity
import com.mufiid.pilwali2020.ui.activities.PilwaliActivity
import com.mufiid.pilwali2020.ui.activities.TpsActivity
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.views.IConfigView
import com.mufiid.pilwali2020.views.IPaslonView
import com.mufiid.pilwali2020.views.ITpsView
import com.viewpagerindicator.CirclePageIndicator
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlinx.android.synthetic.main.suara_paslon.*
import java.net.URI
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Suppress("DEPRECATION")
class BerandaFragment : Fragment(), ITpsView, IConfigView, IPaslonView, View.OnClickListener {
    private var shimmer: ShimmerFrameLayout? = null
    private var shimmerImageSlider: ShimmerFrameLayout? = null
    private var shimmerSuaraPaslon: ShimmerFrameLayout? = null
    private var shimmerSuaraTidakSahHadir: ShimmerFrameLayout? = null
    private var presenter: TpsPresenter? = null
    private var configPresenter: ConfigPresenter? = null
    private var paslonPresenter: AddVotePresenter? = null
    private var viewPager: ViewPager? = null
    private var indicator: CirclePageIndicator? = null
    private var layoutBtnMonitoring: LinearLayout? = null
    private var btnPilwali : ImageButton? = null
    private var btnTps : ImageButton? = null
    private var btnMonitoring : ImageButton? = null
    private var btnBlangko : ImageButton? = null
    private var currentPage = 0
    private var numPages = 0
    private var pathFileBlanko: String? = null

    // var jumlah pemilih
    private var dpt: TextView? = null
    private var dptb: TextView? = null
    private var dpk: TextView? = null
    private var dph: TextView? = null
    private var tps: TextView? = null
    private var tidakSah: TextView? = null
    private var tidakHadir: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        btnPilwali = view.findViewById(R.id.btn_pilwali) as ImageButton
        btnTps = view.findViewById(R.id.btn_tps) as ImageButton
        btnMonitoring = view.findViewById(R.id.btn_monitor) as ImageButton
        btnBlangko = view.findViewById(R.id.btn_blangko) as ImageButton
        layoutBtnMonitoring = view.findViewById(R.id.layout_button_monitoring) as LinearLayout
        shimmer = view.findViewById<View>(R.id.mShimmerViewContainer) as ShimmerFrameLayout
        shimmerSuaraPaslon = view.findViewById<View>(R.id.shimmer_suara_paslon) as ShimmerFrameLayout
        shimmerSuaraTidakSahHadir = view.findViewById<View>(R.id.shimmer_suara_tidak_sah_hadir) as ShimmerFrameLayout
        shimmerImageSlider =
            view.findViewById<View>(R.id.shimmer_image_slider_container) as ShimmerFrameLayout
        viewPager = view.findViewById(R.id.banner_viewPager) as ViewPager
        indicator = view.findViewById(R.id.indicator) as CirclePageIndicator

        // init id jumlah_pemilih
        dpt = view.findViewById(R.id.dpt) as TextView
        dptb = view.findViewById(R.id.dptb) as TextView
        dpk = view.findViewById(R.id.dpk) as TextView
        dph = view.findViewById(R.id.dph) as TextView
        tps = view.findViewById(R.id.subTitle) as TextView
        tidakSah = view.findViewById(R.id.tidak_sah) as TextView
        tidakHadir = view.findViewById(R.id.tidak_hadir) as TextView

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable().clear()
    }

    private fun init() {
        presenter = TpsPresenter(this)
        configPresenter = ConfigPresenter(this)
        paslonPresenter = AddVotePresenter(this)
        configPresenter?.config()

        layoutBtnMonitoring?.visibility = View.GONE

        // event listener
        btnPilwali?.setOnClickListener(this)
        btnTps?.setOnClickListener(this)
        btnMonitoring?.setOnClickListener(this)
        btnBlangko?.setOnClickListener(this)
    }

    private fun doDownloadBlangko() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            } else {
                // do  download
                startDownload()
            }
        } else {
            // do download sdk <= 23
            startDownload()
        }
    }

    private fun startDownload() {
        Toast.makeText(context, resources.getString(R.string.title_download), Toast.LENGTH_SHORT)
            .show()
        val path = URI(pathFileBlanko).path
        val nameFile = path.substring(path.lastIndexOf('/') + 1)

        val request = DownloadManager.Request(Uri.parse(pathFileBlanko)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setTitle(resources.getString(R.string.title_download))
            setDescription(resources.getString(R.string.download_description))
            allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}_${nameFile}"
            )
        }

        // get download service and enqueue file
        val manager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted, perform download
                    startDownload()
                } else {
                    // permission from popup was denied, show error message
                    Toast.makeText(
                        context,
                        resources.getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            Constants.getUserData(it)?.idTps?.let { idTps ->
                presenter?.getDataTps(idTps)
                paslonPresenter?.getPaslon(idTps)
            }
        }

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
        dph?.text = data.dpph2
        tidakSah?.text = data.suara_tidak_sah
        tidakHadir?.text = data.tidakHadir
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
        // code...
    }

    override fun messageFailed(message: String?) {
        // code ..
    }

    /**
     * process loading when get data TPS
     *
     * @author imam mufiid
     * */
    override fun isLoadingTps(state: Int?) {
        shimmer?.startShimmer()
        shimmerSuaraTidakSahHadir?.startShimmer()
        shimmer?.visibility = View.VISIBLE
        shimmerSuaraTidakSahHadir?.visibility = View.VISIBLE

        jumlah_pemilih.visibility = View.GONE
        layout_title.visibility = View.GONE
        suara_tidak_sah_hadir?.visibility = View.GONE
    }

    /**
     * end process loading when get data TPS
     *
     * @author imam mufiid
     * */
    override fun hideLoadingTps(state: Int?) {
        shimmer?.stopShimmer()
        shimmerSuaraTidakSahHadir?.stopShimmer()
        shimmer?.visibility = View.GONE
        shimmerSuaraTidakSahHadir?.visibility = View.GONE

        jumlah_pemilih?.visibility = View.VISIBLE
        layout_title?.visibility = View.VISIBLE
        suara_tidak_sah_hadir?.visibility = View.VISIBLE
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

        pathFileBlanko = data.blanko

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
                // code ...
            }
        } else {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateConfig = formatter.parse(data.dDay)
            // val diff = ((((dateConfig.time - Date().time) / 1000) / 60) / 60) / 24

            // checking status and date
            if (data.status == "production" && Date() < dateConfig) {

                // memanggil fungsi untuk menampilkan dialog fragment
                OpenDialog(
                    "Mohon Maaf!",
                    "Aplikasi belum dapat diakses sampai waktu yang ditentukan.",
                    0
                )
            } else {
                // code...
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_pilwali -> startActivity(Intent(context, PilwaliActivity::class.java))
            R.id.btn_tps -> startActivity(Intent(context, TpsActivity::class.java))
            R.id.btn_monitor -> startActivity(Intent(context, MonitoringActivity::class.java))
            R.id.btn_blangko -> doDownloadBlangko()
        }
    }

    override fun isLoadingPaslon(state: Int?) {
        when(state) {
            1 -> {
                shimmerSuaraPaslon?.visibility = View.VISIBLE
                shimmerSuaraPaslon?.startShimmer()
                suara_paslon?.visibility = View.GONE
            }
        }

    }

    override fun hideLoadingPaslon(state: Int?) {
        when(state) {
            1 -> {
                shimmerSuaraPaslon?.stopShimmer()
                shimmerSuaraPaslon?.visibility = View.GONE
                suara_paslon?.visibility = View.VISIBLE
            }
        }
    }

    override fun getDataPaslon(message: String?, data: List<Paslon>?) {

        data?.let {
            // paslon 1
            Glide.with(this)
                .load(data[0].foto)
                .placeholder(R.drawable.ic_profile_picture)
                .centerCrop()
                .into(img_paslon1)

            suara_paslon1.text = data[0].jumlah_suara_di_tps
            nama_paslon1.text = data[0].nmPeserta

            // paslon 2
            Glide.with(this)
                .load(data[1].foto)
                .placeholder(R.drawable.ic_profile_picture)
                .centerCrop()
                .into(img_paslon2)
            suara_paslon2.text = data[1].jumlah_suara_di_tps
            nama_paslon2.text = data[1].nmPeserta
        }
    }

    override fun failedGetDataPaslon(message: String?) {
        // code ...
    }

    override fun successPostData(message: String?, data: Tps?) {
        // code ...
    }
}