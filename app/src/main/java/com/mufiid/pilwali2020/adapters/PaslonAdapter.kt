package com.mufiid.pilwali2020.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.utils.Constants
import kotlinx.android.synthetic.main.item_paslon.view.*


class PaslonAdapter(val data: List<Paslon>) : RecyclerView.Adapter<PaslonAdapter.Holder>() {
    companion object {
        var dataPaslon : List<Paslon>? = null
        var context: Context? = null
    }

    init {
        dataPaslon = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paslon, parent, false)
        context = parent.context
        return Holder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(data[position])
        when(Constants.getVerification(context)) {
            1 -> {
                holder.itemView.et_suara_paslon.isEnabled = false
            }
        }
    }

    @Suppress("DEPRECATION")
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(get: Paslon?) {
            itemView.nama_paslon.text = "${get?.noPeserta}. ${get?.nmPeserta}"
            itemView.et_suara_paslon.setText(get?.jumlah_suara_di_tps.toString())

            // get last item
            if(position == data.size-1) {
                itemView.divider.visibility = View.GONE
            }

            val circularProgressDrawable = CircularProgressDrawable(context!!)
            circularProgressDrawable.centerRadius = 100F
            circularProgressDrawable.start()

            Glide.with(itemView)
                .load(get?.foto)
                .placeholder(circularProgressDrawable)
                .into(itemView.img_paslon)

            itemView.et_suara_paslon.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // code
                    data[adapterPosition].jumlah_suara_di_tps = itemView.et_suara_paslon.text.toString()
                }

            })

        }

    }

}