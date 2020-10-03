package com.mufiid.pilwali2020.adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Paslon
import kotlinx.android.synthetic.main.item_paslon.view.*


class PaslonAdapter(val data: List<Paslon>) : RecyclerView.Adapter<PaslonAdapter.Holder>() {
    companion object {
        var dataPaslon : List<Paslon>? = null
    }

    init {
        dataPaslon = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paslon, parent, false)
//        suaraPaslon = ArrayList<SuaraPaslon>(data.size)

        return Holder(view)
    }

    override fun getItemCount(): Int = data.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.et_suara_paslon
    }

    @Suppress("DEPRECATION")
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(get: Paslon?) {
            Log.d("GET", get.toString())
            itemView.nama_paslon.text = "${get?.noPeserta}. ${get?.nmPeserta}"
            itemView.et_suara_paslon.setText(get?.jumlah_suara.toString())

            // get last item
            if(position == data.size-1) {
                itemView.divider.visibility = View.GONE
            }

            Glide.with(itemView)
                .load(get?.foto)
                .into(itemView.img_paslon)

            itemView.et_suara_paslon.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // code
                     val suara_paslon = s.toString()
                    data[adapterPosition].jumlah_suara = itemView.et_suara_paslon.text.toString()
                }

            })

        }

    }

}