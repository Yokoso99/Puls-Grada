package com.example.prototype2;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class DogadjajiViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recLang,eventTip,eventDetail;
    CardView recCard;


    public DogadjajiViewHolder(@NonNull View itemView) {
        super(itemView);

        eventDetail = itemView.findViewById(R.id.eventDetail);
        eventTip = itemView.findViewById(R.id.eventTip);
        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recDesc = itemView.findViewById(R.id.recDesc);
        recLang = itemView.findViewById(R.id.recLang);
        recCard = itemView.findViewById(R.id.recCard);

    }
}