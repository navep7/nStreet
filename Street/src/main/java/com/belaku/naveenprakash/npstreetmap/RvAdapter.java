package com.belaku.naveenprakash.npstreetmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private ArrayList<String> arrayListNames = new ArrayList<>();
    private ArrayList<Integer> arrayListDrawables = new ArrayList<>();
    private final OnPlaceClick onPlaceClick;

    public RvAdapter(Context context, ArrayList<String> arrayListNames, ArrayList<Integer> arrayListDrawables, OnPlaceClick onPlaceClick) {
        this.mInflater = LayoutInflater.from(context);
        this.arrayListNames = arrayListNames;
        this.arrayListDrawables = arrayListDrawables;
        this.onPlaceClick = onPlaceClick;
    }


    @NonNull
    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_gridview, parent, false);
        return new ViewHolder(view, onPlaceClick);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapter.ViewHolder holder, int position) {

        holder.textView.setText(arrayListNames.get(position));
        holder.imageView.setImageResource(arrayListDrawables.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayListNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;
        OnPlaceClick onPlaceClick;


        public ViewHolder(@NonNull View itemView, OnPlaceClick onPlaceClick) {
            super(itemView);
            textView = itemView.findViewById(R.id.tx);
            imageView = itemView.findViewById(R.id.imgv);
            this.onPlaceClick = onPlaceClick;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
        onPlaceClick.onClick(getAdapterPosition());
        }
    }

    public interface OnPlaceClick {
        void onClick(int pos);
    }

}
