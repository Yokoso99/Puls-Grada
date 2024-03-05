package com.example.prototype2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.Item;
import com.example.prototype2.R;
import com.example.prototype2.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private Context context;
    private List<Item> itemList;
    private List<Item> filteredList;
    private OnItemClickListener onItemClickListener; // Use your custom interface

    public Adapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.filteredList = new ArrayList<>(itemList);
    }

    // Declare your custom interface
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = filteredList.get(position);


        holder.itemImage.setImageResource(currentItem.getImageResourceId());

        holder.name.setText(currentItem.getName());

        holder.itemView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(currentItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterText = charSequence.toString().toLowerCase().trim();

                List<Item> tempList = new ArrayList<>();

                if (filterText.isEmpty()) {
                    tempList.addAll(itemList);
                } else {
                    for (Item item : itemList) {
                        if (item.getName().toLowerCase().contains(filterText)) {
                            tempList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = tempList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList.clear();
                filteredList.addAll((List<Item>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}
