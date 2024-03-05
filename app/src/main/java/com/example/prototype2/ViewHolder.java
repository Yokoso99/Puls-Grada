package com.example.prototype2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;

import org.w3c.dom.Text;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    public ImageView itemImage;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.item_image);
        name = itemView.findViewById(R.id.name);
    }
}
