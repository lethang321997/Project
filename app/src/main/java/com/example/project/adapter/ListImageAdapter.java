package com.example.project.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ViewHolder> {
    private ArrayList<Bitmap> listImage;

    public ListImageAdapter(ArrayList<Bitmap> listImage) {
        this.listImage = listImage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_image, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindData(listImage.get(position));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listImage.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.btnDeleteImage);
        }

        void bindData(Bitmap imageBitmap) {
            image.setImageBitmap(imageBitmap);
        }
    }
}
