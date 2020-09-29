package com.mufiid.pilwali2020.ui.fragments

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.ui.activities.MonitoringActivity
import com.mufiid.pilwali2020.ui.activities.PilwaliActivity
import com.mufiid.pilwali2020.ui.activities.TpsActivity
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.presenters.TpsPresenter
import com.mufiid.pilwali2020.utils.Constants
import com.mufiid.pilwali2020.utils.helpers.ConnectivityReceiver
import com.mufiid.pilwali2020.views.ITpsView
import kotlinx.android.synthetic.main.fragment_beranda.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BerandaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class BerandaFragment : Fragment(), ITpsView, ConnectivityReceiver.ConnectivityReceiverListener {
    private var param1: String? = null
    private var param2: String? = null
    private var shimmer : ShimmerFrameLayout? = null
    private var presenter: TpsPresenter? =null

    // var jumlah pemilih
    private var dpt : TextView? = null
    private var dptb : TextView? = null
    private var dpk : TextView? = null
    private var dpktb : TextView? = null
    private var difabel : TextView? = null
    private var tps : TextView? = null

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
        val image_header = root.findViewById<ImageView>(R.id.image_header) as ImageView
        shimmer = root.findViewById<View>(R.id.mShimmerViewContainer) as com.facebook.shimmer.ShimmerFrameLayout
        val jumlah_pemilih = root.findViewById<View>(R.id.jumlah_pemilih)
        val layout_title = root.findViewById<LinearLayout>(R.id.layout_title) as LinearLayout

        // init id jumlah_pemilih
        dpt = root.findViewById<TextView>(R.id.dpt) as TextView
        dptb = root.findViewById<TextView>(R.id.dptb) as TextView
        dpk = root.findViewById<TextView>(R.id.dpk) as TextView
        dpktb = root.findViewById<TextView>(R.id.dpktb) as TextView
        difabel = root.findViewById<TextView>(R.id.difabel) as TextView
        tps = root.findViewById(R.id.subTitle) as TextView

        // init presenter
        presenter = TpsPresenter(this)


        // event listener
        Glide.with(this)
            .load("https://cdn2.tstatic.net/wartakota/foto/bank/images/pilkada-serentak-2020a.jpg")
            .placeholder(R.drawable.ic_img_placeholder)
            .into(image_header)

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
        requireActivity().registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

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
        tps?.text = "TPS ${data.noTps} - Kel. ${data.kelurahan} Kec. ${data.kecamatan}"
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
}