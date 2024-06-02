package com.example.prototype2;// UpcAndPassViewHolder.java

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UpcAndPassViewHolder extends RecyclerView.ViewHolder {

    public ImageView recImage;
    public TextView recTitle;
    public TextView recDesc;
    public TextView recLang;
    public TextView eventTip;
    public TextView eventDetail;

    public UpcAndPassViewHolder(@NonNull View itemView, final AdapterUpcAndPass.OnItemClickListener listener) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recLang);
        eventTip = itemView.findViewById(R.id.eventTip);
        eventDetail = itemView.findViewById(R.id.eventDetail);


    }
}
