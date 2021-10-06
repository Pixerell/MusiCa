package com.example.musica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.musica.librariesMain.musicFiles;
import static com.example.musica.librariesMain.shuffleOn;
import static com.example.musica.librariesMain.repeatOn;

public class trackScreenMain extends AppCompatActivity implements MediaPlayer.OnCompletionListener {


    TextView song_name, artist_name, duration_played, duration_total, now_playing_text;
    ImageView cover_art, nextBtn, previousBtn, backBtn, shuffleBtn, repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int position = -1;
   // boolean shuffleOn = false, repeatOn = false;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private  Thread playThread, previousThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_screen_main);

        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                  mediaPlayer.seekTo(progress * 1000);



                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        trackScreenMain.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {

                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));

                    /*
                    if (artist_name.getText() != null) {
                        artist_name.setVisibility(View.VISIBLE);
                        artist_name.setText(listSongs.get(position).getArtist());
                    }
                    else {
                        artist_name.setVisibility(View.INVISIBLE);
                    }

                     */

                }

                handler.postDelayed(this, 1000);

            }
        });

    }

    @Override
    protected void onResume() {
        // Spawning multiple threads is retarded
        playThreadBtn();
        //nextThreadBtn();
       // previousThreadBtn();
        super.onResume();
    }


    // Main Thread. All button clicks are registered here
    private void playThreadBtn() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });

                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnBtnClicked();
                    }
                });

                previousBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousBtnClicked();
                    }
                });

                shuffleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shuffleOn){
                            shuffleBtn.setColorFilter(shuffleBtn.getContext().getResources().getColor(R.color.buttonTintColor), PorterDuff.Mode.SRC_ATOP);

                            shuffleOn = false;
                        }
                        else {
                            shuffleBtn.setColorFilter(shuffleBtn.getContext().getResources().getColor(R.color.buttonTintColorBackground), PorterDuff.Mode.SRC_ATOP);

                            shuffleOn = true;
                        }
                    }
                });

                repeatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (repeatOn){
                            repeatBtn.setColorFilter(repeatBtn.getContext().getResources().getColor(R.color.buttonTintColor), PorterDuff.Mode.SRC_ATOP);

                            repeatOn = false;


                        }
                        else {
                            repeatBtn.setColorFilter(repeatBtn.getContext().getResources().getColor(R.color.buttonTintColorBackground), PorterDuff.Mode.SRC_ATOP);

                            repeatOn = true;
                        }


                    }
                });
            }
        };

        playThread.start();
    }

    private void repeatBtnClicked() {

        repeatOn = !repeatOn;

        if (repeatOn){
            repeatBtn.setImageResource(R.drawable.repeat_on);
            repeatBtn.setColorFilter(R.color.buttonTintColorBackground);
        }
        else {
            repeatBtn.setImageResource(R.drawable.repeat_off);
            repeatBtn.setColorFilter(null);
        }

    }

    private void shuffleBtnClicked() {


        shuffleOn = !shuffleOn;

        if (shuffleOn){
            shuffleBtn.setColorFilter(R.color.buttonTintColorBackground);

        }
        else {
            shuffleBtn.setColorFilter(null);

        }

    }

    private void playPauseBtnClicked() {

        if (mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            trackScreenMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {

                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }

                    handler.postDelayed(this, 1000);

                }
            });
        }
        else
        {
            playPauseBtn.setImageResource(R.drawable.pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            trackScreenMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {

                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                    }

                    handler.postDelayed(this, 1000);

                }
            });
        }
    }

    private void nextBtnBtnClicked() {

       if (mediaPlayer.isPlaying()){
           mediaPlayer.stop();
           mediaPlayer.release();

           if (shuffleOn && !repeatOn)
           {
               position = getRandom(listSongs.size() -1);
           }

           else if (!shuffleOn && !repeatOn) {
               position = ((position + 1) % listSongs.size());
           }

           uri = Uri.parse(listSongs.get(position).getPath());
           mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
           metaData(uri);
           song_name.setText(listSongs.get(position).getTitle());
           artist_name.setText(listSongs.get(position).getArtist());
           seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   if (mediaPlayer != null && fromUser){
                       mediaPlayer.seekTo(progress * 1000);



                   }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {

               }
           });

           trackScreenMain.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (mediaPlayer != null) {

                       int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                       seekBar.setProgress(mCurrentPosition);
                       duration_played.setText(formattedTime(mCurrentPosition));
                   }

                   handler.postDelayed(this, 1000);

               }
           });
           mediaPlayer.setOnCompletionListener(this);
           playPauseBtn.setBackgroundResource(R.drawable.pause);
           mediaPlayer.start();
       }

       else {
           mediaPlayer.stop();
           mediaPlayer.release();

           if (shuffleOn && !repeatOn)
           {
               position = getRandom(listSongs.size() -1);
           }

           else if (!shuffleOn && !repeatOn) {
               position = ((position + 1) % listSongs.size());
           }

           //position = ((position + 1) % listSongs.size());
           uri = Uri.parse(listSongs.get(position).getPath());
           mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
           metaData(uri);
           song_name.setText(listSongs.get(position).getTitle());
           artist_name.setText(listSongs.get(position).getArtist());
           seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   if (mediaPlayer != null && fromUser){
                       mediaPlayer.seekTo(progress * 1000);



                   }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {

               }
           });

           trackScreenMain.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (mediaPlayer != null) {

                       int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                       seekBar.setProgress(mCurrentPosition);
                       duration_played.setText(formattedTime(mCurrentPosition));
                   }

                   handler.postDelayed(this, 1000);

               }
           });
           mediaPlayer.setOnCompletionListener(this);
           playPauseBtn.setBackgroundResource(R.drawable.play);
       }

    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);

    }

    private void previousBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffleOn && !repeatOn)
            {
                position = getRandom(listSongs.size() -1);
            }

            else if (!shuffleOn && !repeatOn) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaPlayer != null && fromUser){
                        mediaPlayer.seekTo(progress * 1000);



                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            trackScreenMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {

                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                    }

                    handler.postDelayed(this, 1000);

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.pause);
            mediaPlayer.start();
        }

        else {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (shuffleOn && !repeatOn)
            {
                position = getRandom(listSongs.size() -1);
            }

            else if (!shuffleOn && !repeatOn) {
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaPlayer != null && fromUser){
                        mediaPlayer.seekTo(progress * 1000);



                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            trackScreenMain.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {

                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                    }

                    handler.postDelayed(this, 1000);

                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.play);
        }

    }

    private String formattedTime(int mCurrentPosition) {

        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1){
            return totalNew;
        }

        else {
            return totalOut;
        }


    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();

        }

        else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }

        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }

    private void initViews() {
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationPlayed);
        duration_total = findViewById(R.id.durationTotal);
        now_playing_text = findViewById(R.id.now_playingText);

        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.id_next);
        previousBtn = findViewById(R.id.id_previous);
        backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.id_shuffle);
        repeatBtn = findViewById(R.id.id_repeat);

        playPauseBtn = findViewById(R.id.play_pause);

        seekBar = findViewById(R.id.seekBar);
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration())/ 1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null){
            
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);

            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this, cover_art, bitmap);


        }

        else {

            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.sample_music_icon)
                    .into(cover_art);
        }
    }

    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap) {

        Animation animaOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animaIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animaOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animaIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animaIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animaOut);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnBtnClicked();
        if (mediaPlayer != null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
