package com.example.shopsaverandroidapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter {

    String[] list;

    @NonNull
    @Override
    public CustomisedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomisedViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    /*@Override
    public void onBindViewHolder(@NonNull ListAdapter.CustomisedViewHolder holder, int position) {
        holder.textView.setText(list[position]);
    }*/

    @Override
    public int getItemCount() {
        return list.length;
    }

    static class CustomisedViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public CustomisedViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewList);
        }
    }
}
