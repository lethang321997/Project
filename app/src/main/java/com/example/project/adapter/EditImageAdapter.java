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

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.fragment.EditProductFragment;

import java.util.ArrayList;

public class EditImageAdapter extends RecyclerView.Adapter<EditImageAdapter.ViewHolder> {

    ArrayList<Bitmap> listImagesBitmap;
    public static boolean deletedMainImage = false;

    public EditImageAdapter(ArrayList<Bitmap> listImagesBitmap) {
        this.listImagesBitmap = listImagesBitmap;
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
        holder.bindDataUrl(listImagesBitmap.get(position));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listImagesBitmap.remove(position);
                EditProductFragment.firstSize--;
                EditProductFragment.addDeletedImage(position);
                if (position == 0) {
                    deletedMainImage = true;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImagesBitmap.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.btnDeleteImage);
        }


        void bindDataUrl(Bitmap imageBitmap) {
            image.setImageBitmap(imageBitmap);
        }
    }
}
