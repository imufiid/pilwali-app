package com.mufiid.pilwali2020.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mufiid.pilwali2020.R
import com.mufiid.pilwali2020.models.Paslon
import kotlinx.android.synthetic.main.item_paslon.view.*

class PaslonAdapter(private val data: List<Paslon>?): RecyclerView.Adapter<PaslonAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paslon, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(data?.get(position))
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(get: Paslon?) {
        }

    }

}