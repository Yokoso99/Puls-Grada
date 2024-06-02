package com.example.prototype2;// AdapterUpcAndPass.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUpcAndPass extends RecyclerView.Adapter<UpcAndPassViewHolder> {

    private List<UpcPassDogadjajiItem> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AdapterUpcAndPass(Context context, List<UpcPassDogadjajiItem> list) {
        this.list = list;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(UpcPassDogadjajiItem position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public UpcAndPassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upandpass_item, parent, false);
        return new UpcAndPassViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcAndPassViewHolder holder, int position) {
        UpcPassDogadjajiItem item = list.get(position);
        holder.recImage.setImageBitmap(item.getDataImage());
        holder.recTitle.setText(item.getDataTitle());
        holder.recDesc.setText(item.getDataDesc());
        holder.recLang.setText(item.getDataLang());
        holder.eventTip.setText(item.getEventTip());
        holder.eventDetail.setText(item.getEventDetail());

        holder.itemView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
