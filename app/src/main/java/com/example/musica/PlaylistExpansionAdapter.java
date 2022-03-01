package com.example.musica;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;

public class PlaylistExpansionAdapter extends RecyclerView.Adapter<PlaylistExpansionAdapter.MyHolder> {
    private Context mContext;
    static ArrayList<MusicFiles> playlistFiles;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();


    View view;

    public PlaylistExpansionAdapter(Context mContext, ArrayList<MusicFiles> playlistFiles) {
        this.mContext = mContext;
        this.playlistFiles = playlistFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.playlist_name.setText(playlistFiles.get(position).getTitle());
        // byte[] image = getAlbumArt(playlistFiles.get(position).getPath());
        // byte[] image = getAlbumArt(albumSongs.get(0).getPath());


        byte[] image = null;


        if (image != null) {

            GlideApp.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.playlist_image);

        }
        else {
            GlideApp.with(mContext)
                    .load(R.drawable.sample_music_icon)
                    .into(holder.playlist_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, trackScreenMain.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return playlistFiles.size();
    }

    public class MyHolder  extends RecyclerView.ViewHolder {

        ImageView playlist_image;
        TextView playlist_name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            playlist_image = itemView.findViewById(R.id.music_img);
            playlist_name = itemView.findViewById(R.id.music_file_name);

        }
    }

    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

}
