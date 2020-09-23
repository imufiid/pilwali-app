package com.mufiid.pilwali2020.fragments

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.activities.MonitoringActivity
import com.mufiid.pilwali2020.activities.PilwaliActivity
import com.mufiid.pilwali2020.activities.TpsActivity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BerandaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BerandaFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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

        Glide.with(this)
            .load("https://cdn2.tstatic.net/wartakota/foto/bank/images/pilkada-serentak-2020a.jpg")
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







        return root
    }

    private fun openActivity(activity: Activity) {
        startActivity(Intent(context, activity::class.java))
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BerandaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}