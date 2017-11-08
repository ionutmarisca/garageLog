package com.imm.garagelog.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imm.garagelog.R;
import com.imm.garagelog.domain.Car;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ionut on 30/10/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    List<Car> carList;
    OnItemClickListener listener;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public RecyclerAdapter(List<Car> carList, OnItemClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        context = parent.getContext();
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.brandName.setText(carList.get(position).getBrand());
        holder.modelName.setText(carList.get(position).getModel());
        Picasso.with(context).load(carList.get(position).getBrandLogoUrl()).into(holder.brandLogo);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView brandName;
        TextView modelName;
        ImageView brandLogo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            brandName = (TextView)itemView.findViewById(R.id.brandName);
            modelName = (TextView)itemView.findViewById(R.id.modelName);
            brandLogo = (ImageView)itemView.findViewById(R.id.brandLogo);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }
    }
}