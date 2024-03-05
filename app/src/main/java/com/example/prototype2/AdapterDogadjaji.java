package com.example.prototype2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Result;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

public class AdapterDogadjaji extends RecyclerView.Adapter<DogadjajiViewHolder> implements Filterable {


    private Context context;
    private List<DogadjajiItem> list;




    public AdapterDogadjaji(Context context, List<DogadjajiItem> list){

        this.context = context;
        this.list = list;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }




    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public DogadjajiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogadjaji_item_layout, parent, false);

        return new DogadjajiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogadjajiViewHolder holder, int position) {



        DogadjajiItem item = list.get(position);
        holder.recImage.setImageBitmap(list.get(position).getDataImage());
        holder.recTitle.setText(list.get(position).getDataTitle());
        holder.recDesc.setText(list.get(position).getDataDesc());
        holder.recLang.setText(list.get(position).getDataLang());
        holder.eventTip.setText(list.get(position).getEventTip());
        holder.eventDetail.setText(list.get(position).getEventDetail());



        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent intent = new Intent(context, DogadjajiDetalji.class);


                intent.putExtra("eventTitle", item.getDataTitle());
                intent.putExtra("eventDate", item.getDataDesc());
                intent.putExtra("eventTime", item.getDataLang());
                intent.putExtra("eventTip", item.getEventTip());
                intent.putExtra("eventDetail", item.getEventDetail());

                Bitmap bitmap = item.getDataImage();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("encodedImage", byteArray);
                ((Activity) context).startActivityForResult(intent, 1);



            }
        });
    }



}
