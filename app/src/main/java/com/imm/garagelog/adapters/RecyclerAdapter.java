package com.imm.garagelog.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imm.garagelog.R;
import com.imm.garagelog.repository.Repository;
import com.squareup.picasso.Picasso;

/**
 * Created by Ionut on 30/10/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    Repository repository;
    OnItemClickListener listener;
    OnItemLongClickListener longListener;
    Context context;

    public RecyclerAdapter(Repository repository, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.repository = repository;
        this.listener = listener;
        this.longListener = longListener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        context = parent.getContext();
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.brandName.setText(repository.getCarList().get(position).getBrand());
        holder.modelName.setText(repository.getCarList().get(position).getModel());
        Picasso.with(context).load(repository.getCarList().get(position).getBrandLogoUrl()).into(holder.brandLogo);
    }

    @Override
    public int getItemCount() {
        return repository.getCarList().size();
    }

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(final int id);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView brandName;
        TextView modelName;
        ImageView brandLogo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            brandName = (TextView) itemView.findViewById(R.id.brandName);
            modelName = (TextView) itemView.findViewById(R.id.modelName);
            brandLogo = (ImageView) itemView.findViewById(R.id.brandLogo);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longListener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }
}