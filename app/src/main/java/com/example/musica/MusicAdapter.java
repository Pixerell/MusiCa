package com.example.musica;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles)
    {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
         return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {

        holder.file_name.setText(mFiles.get(position).getTitle());

        try {
            byte[] image = getAlbumArt(mFiles.get(position).getPath());


            if (image != null) {

                Glide.with(mContext)
                        .asBitmap()
                        .load(image)
                        .into(holder.album_art);
            }
            else {
                Glide.with(mContext)
                        .load(R.drawable.sample_music_icon)
                        .into(holder.album_art);
            }

        }
        catch (Exception IllegalArgumentException){
            Glide.with(mContext)
                    .load(R.drawable.sample_music_icon)
                    .into(holder.album_art);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, trackScreenMain.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        // Popup general
        holder.more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            Toast.makeText(mContext, "Delete ON", Toast.LENGTH_SHORT).show();
                            deleteFile(position, v);
                            break;

                    }
                    return true;
                }));
            }
        });
    }

    // Popup deletion
    private void deleteFile(int position, View v) {

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId()));

        mFiles.remove(position);

        File file = new File(mFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            mContext.getContentResolver().delete(contentUri, null, null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());


            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "Audiofile trashed : ", Snackbar.LENGTH_LONG)
                    .show();
        }

        else{
            // API =< 19 or is in SD CARD
            Snackbar.make(v, "I will not delete that : ", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyVieHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        ImageView album_art, more_menu;

     public MyVieHolder(@NonNull View itemView) {
         super(itemView);
         file_name = itemView.findViewById(R.id.music_file_name);
         album_art = itemView.findViewById(R.id.music_img);
         more_menu = itemView.findViewById(R.id.more_menu);
     }
 }

 private byte[] getAlbumArt(String uri){
     MediaMetadataRetriever retriever = new MediaMetadataRetriever();
     retriever.setDataSource(uri);
     byte[] art = retriever.getEmbeddedPicture();
     retriever.release();
     return art;
 }

}