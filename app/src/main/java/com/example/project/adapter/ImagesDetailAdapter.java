package com.example.project.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;

import java.util.ArrayList;

public class ImagesDetailAdapter extends RecyclerView.Adapter<ImagesDetailAdapter.ViewHolder> {
    private ArrayList<String> listImage;

    public ImagesDetailAdapter(ArrayList<String> listImage) {
        this.listImage = listImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.images_details, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageDetail);
        }

        void bindData(String url) {
            Glide.with(itemView.getContext())
                    .load(url)
                    .into(image);
        }
    }


}
