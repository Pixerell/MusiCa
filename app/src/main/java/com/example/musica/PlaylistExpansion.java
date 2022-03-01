package com.example.musica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.musica.librariesMain.musicFiles;

public class PlaylistExpansion extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumName;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();
    PlaylistExpansionAdapter playlistExpansionAdapter;

    public byte[] expandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_expansion);
        recyclerView = findViewById(R.id.recyclerView);
        albumPhoto = findViewById(R.id.albumPhoto);
        albumName = getIntent().getStringExtra("albumName");
        int j = 0;
        for (int i = 0; i < musicFiles.size(); i++){
            if (albumName.equals(musicFiles.get(i).getAlbum())){
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }

        expandedImage = getAlbumArt(albumSongs.get(0).getPath());
        if (expandedImage != null){

            GlideApp.with(this)
                    .load(expandedImage)
                    .into(albumPhoto);

        }
        else {
            GlideApp.with(this)
                    .load(R.drawable.sample_music_icon)
                    .into(albumPhoto);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)){
            playlistExpansionAdapter = new PlaylistExpansionAdapter(this, albumSongs);
            recyclerView.setAdapter(playlistExpansionAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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

    public byte[] getExpandedImage(){
        return expandedImage;
    }
}