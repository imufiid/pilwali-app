package com.mufiid.pilwali2020.adapters;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mufiid.pilwali2020.R;
import com.mufiid.pilwali2020.models.Paslon;

import java.util.ArrayList;
import java.util.List;

public class PaslonAdptr extends RecyclerView.Adapter<PaslonAdptr.MyViewHolder> {
    private LayoutInflater inflater;
    public static List<Paslon> editModelArrayList;

    public PaslonAdptr(Context ctx, ArrayList<Paslon> editModelArrayList){
        inflater = LayoutInflater.from(ctx);
        PaslonAdptr.editModelArrayList = editModelArrayList;
    }

    @NonNull
    @Override
    public PaslonAdptr.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_paslon, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaslonAdptr.MyViewHolder holder, int position) {
        holder.editText.setText(editModelArrayList.get(position).getJumlah_suara());
        holder.namaPaslon.setText(editModelArrayList.get(position).getNoPeserta() + ". " + editModelArrayList.get(position).getNmPeserta());
        Glide.with(holder.itemView)
                .load(editModelArrayList.get(position).getFoto())
                .into(holder.gambar);
        if(position == 1) {
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected EditText editText;
        protected TextView namaPaslon;
        protected ImageView gambar;
        protected View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (View) itemView.findViewById(R.id.divider);
            editText = (EditText) itemView.findViewById(R.id.et_suara_paslon);
            namaPaslon = (TextView) itemView.findViewById(R.id.nama_paslon);
            gambar = (ImageView) itemView.findViewById(R.id.img_paslon);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editModelArrayList.get(getAdapterPosition()).setJumlah_suara(Integer.valueOf(editText.getText().toString()));
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }
    }
}
