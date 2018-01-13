package com.example.nghiatruong.musicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Bitmap;

/**
 * Created by Nghia Truong on 12/24/2017.
 */

public class AudioPlayerActivity extends AppCompatActivity{
    Button btnBack;
    static Button btnPause;
    Button btnNext;
    static Button btnPlay;
    static Button btnShuffle;
    static Button btnUnShuffle;
    static Button btnRepeat;
    static TextView textViewNowPlaying;
    static TextView textViewAlbumArtist;
    static TextView textViewComposer;
    TextView textViewBufferDuration,textViewDuration;
    static LinearLayout linearLayoutPlayer;
    static Context context;
    SeekBar seekBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.audio_player);
        context=this;
        init();

    }

    @SuppressLint("HandlerLeak")
    private void init() {
        getView();
        setListeners();
        PlayerConstants.PROGRESSBAR_HANDLER=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Integer[] integers=(Integer[])msg.obj;
                textViewBufferDuration.setText(UltilFunctions.getDuration(integers[0]));
                textViewDuration.setText(UltilFunctions.getDuration(integers[1]));
                seekBar.setMax(integers[1]);
                if(!PlayerConstants.IS_PROGRESSBAR_SEEK)
                    seekBar.setProgress(integers[0]);
            }
        };
    }
    public void getView() {
        btnBack=(Button)findViewById(R.id.btnBack);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnPause =(Button)findViewById(R.id.btnPause);
        btnShuffle=(Button)findViewById(R.id.btnShuffle);
        btnUnShuffle=(Button)findViewById(R.id.btnUnShuffle);
        btnRepeat=(Button)findViewById(R.id.repeat);

        textViewAlbumArtist=(TextView)findViewById(R.id.textAlbumArtist);
        textViewBufferDuration=(TextView)findViewById(R.id.textBufferDuration);
        textViewDuration=(TextView)findViewById(R.id.textDuration);
        textViewComposer=(TextView)findViewById(R.id.textComposer);
        textViewNowPlaying=(TextView)findViewById(R.id.textNowPlaying);

        linearLayoutPlayer=(LinearLayout)findViewById(R.id.linerLayoutPlayer);

        seekBar=(SeekBar)findViewById(R.id.seekBar);

    }

    private void setListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(getApplicationContext());
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.playControl(getApplicationContext());
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.pauseControl(getApplicationContext());
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.nextControl(getApplicationContext());
            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.shuffleControl(getApplicationContext());
            }
        });
        btnUnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.shuffleControl(getApplicationContext());
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.repeatControl(getApplicationContext());
                changeRepeat();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                PlayerConstants.CURRENT_POSISION=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerConstants.IS_PROGRESSBAR_SEEK=true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.isFinish){
            this.finish();
        }
        boolean isServiceRunning=UltilFunctions.isServiceRunning(SongService.class.getName(),getApplicationContext());
        if(isServiceRunning){
            updateUI();
        }
        changeButton();
        changeShuffle();
    }



    private static void updateUI() {
        try{
            String songTitle= PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
            String artist=PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER).getArtist();
            String album=PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER).getAlbum();
            String composer=PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER).getComposer();
            textViewNowPlaying.setText(songTitle);
            textViewAlbumArtist.setText(artist+"-"+album);
            textViewAlbumArtist.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            if(composer!=null&&composer.length()>0){
                textViewComposer.setVisibility(View.VISIBLE);
                textViewComposer.setText(composer);
            }else {
                textViewComposer.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            long albumID=PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumID();
            Bitmap albumArt=UltilFunctions.getAlbumImg(context,albumID);
            if(albumArt!=null){
                linearLayoutPlayer.setBackgroundDrawable(new BitmapDrawable(albumArt));
            }else
            {
                linearLayoutPlayer.setBackgroundDrawable(new BitmapDrawable(UltilFunctions.getDefaultAlbumImg(context)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void changeButton() {
        if(PlayerConstants.SONG_PAUSE){
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
        }
        else{
            btnPause.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
        }
    }
    public static void changeUI(){
        updateUI();
        changeButton();
    }
    public static void changeShuffle(){
     if(PlayerConstants.SONG_SHUFFLE){
         btnUnShuffle.setVisibility(View.GONE);
         btnShuffle.setVisibility(View.VISIBLE);
     }else{
         btnShuffle.setVisibility(View.GONE);
         btnUnShuffle.setVisibility(View.VISIBLE);
     }
   }
    private void changeRepeat(){
        if(PlayerConstants.SONG_REPEAT==0){
            btnRepeat.setBackgroundResource(R.drawable.repeat);
        }else if(PlayerConstants.SONG_REPEAT==1){
            btnRepeat.setBackgroundResource(R.drawable.repeat_1);
        }else if(PlayerConstants.SONG_REPEAT==2){
            btnRepeat.setBackgroundResource(R.drawable.repeat_all);
        }
    }
}

