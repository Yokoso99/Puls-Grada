package com.example.prototype2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterUpcAndPass extends RecyclerView.Adapter<UpcAndPassViewHolder> {

    private List<UpcPassDogadjajiItem> list;
    private Context context;

    public AdapterUpcAndPass(Context context,List<UpcPassDogadjajiItem> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public UpcAndPassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upandpass_item, parent, false);

        return new UpcAndPassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcAndPassViewHolder holder, int position) {

        UpcPassDogadjajiItem item = list.get(position);
        holder.recImage.setImageBitmap(list.get(position).getDataImage());
        holder.recTitle.setText(list.get(position).getDataTitle());
        holder.recDesc.setText(list.get(position).getDataDesc());
        holder.recLang.setText(list.get(position).getDataLang());
        holder.eventTip.setText(list.get(position).getEventTip());
        holder.eventDetail.setText(list.get(position).getEventDetail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
