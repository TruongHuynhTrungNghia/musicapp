package com.example.nghiatruong.musicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import android.graphics.Bitmap;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    SongAdapter songAdapter = null;
    static TextView playingSong;
    static Button btnPause, btnPlay, btnNext, btnPrevious, btnShuffle, btnUnShuffle;
    Button btnStop;
    Button btnBack;
    LinearLayout songLayout;
    static LinearLayout linearLayoutPlayingSong;
    ListView songListView;
    ProgressBar progressBar;
    TextView textBufferDuration, textDuration;
    static ImageView imageViewAlbumArt;
    static Context context;
    private Context closeContext;
    EditText editTextSearch;
    private static final int MY_PERMISSTION_REQUEST = 1;
    public static boolean isFinish;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        closeContext = MainActivity.this;
        //give permission to access memory(Init() inside this method)
        Permission();
        //Stop the keyboard appear when launching app.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        isFinish = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        getviews();
        playingSong.setSelected(true);
        progressBar.getProgressDrawable().setColorFilter(getResources()
                .getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        if (PlayerConstants.SONG_LIST.size() <= 0) {
            PlayerConstants.SONG_LIST = UltilFunctions.songArrayList(MainActivity.this);
        }
        setListItems();
        setListeners();
    }

    private void setListItems() {
        songAdapter = new SongAdapter(this, R.layout.custom_list, PlayerConstants.SONG_LIST);
        songListView.setAdapter(songAdapter);
        songListView.setFastScrollEnabled(true);
    }

    public void getviews() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPrevious = (Button) findViewById(R.id.btnPrevious);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnBack = (Button) findViewById(R.id.btnClearText);
        btnUnShuffle = (Button) findViewById(R.id.btnUnShuffle);
        btnShuffle = (Button) findViewById(R.id.btnShuffle);

        songLayout = (LinearLayout) findViewById(R.id.linerLayoutMusicList);
        linearLayoutPlayingSong = (LinearLayout) findViewById(R.id.linerLayoutPlayingSong);

        textBufferDuration = (TextView) findViewById(R.id.textBufferDuration);
        playingSong = (TextView) findViewById(R.id.textNowPlaying);
        textDuration = (TextView) findViewById(R.id.textDuration);

        songListView = (ListView) findViewById(R.id.listViewMusic);

        imageViewAlbumArt = (ImageView) findViewById(R.id.imageViewAlbumArt);

        progressBar = (ProgressBar) findViewById(R.id.progressBarSongDuration);

        editTextSearch = (EditText) findViewById(R.id.editSearch);
    }

    private void setListeners() {
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "TAG Tapped INOUT(IN)");
                PlayerConstants.SONG_PAUSE = false;
                PlayerConstants.SONG_NUMBER = position;
                boolean isServiceRunning = UltilFunctions
                        .isServiceRunning(SongService.class.getName(), getApplicationContext());
                if (!isServiceRunning) {
                    Intent i = new Intent(getApplicationContext(), SongService.class);
                    startService(i);
                } else {
                    PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
                }
                updateUI();
                changeButton();
                Log.d("TAG", "TAG tapped INOUT(OUT)");
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
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controls.previousControl(getApplicationContext());
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SongService.class);
                stopService(intent);
                linearLayoutPlayingSong.setVisibility(View.GONE);
            }
        });
        imageViewAlbumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AudioPlayerActivity.class);
                startActivity(intent);
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
                try {
                    AudioPlayerActivity.btnShuffle.setVisibility(View.GONE);
                } catch (Exception e) {
                }

            }
        });
        editTextSearchSong();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearch.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void editTextSearchSong() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                songAdapter.filter(text);
            }
        });
    }

    public static void changeButton() {
        if (PlayerConstants.SONG_PAUSE) {
            btnPause.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
        } else {
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        }
    }

    private static void updateUI() {
        try {
            Song song = PlayerConstants.SONG_LIST.get(PlayerConstants.SONG_NUMBER);
            playingSong.setText(song.getTitle() + " " + song.getArtist() + " " + song.getAlbum());
            Bitmap albumArt = UltilFunctions.getAlbumImg(context, song.getAlbumID());
            if (albumArt != null) {
                imageViewAlbumArt.setBackgroundDrawable(new BitmapDrawable(albumArt));
            } else {
                imageViewAlbumArt.setBackgroundDrawable(new BitmapDrawable(UltilFunctions.getDefaultAlbumImg(context)));
            }
            linearLayoutPlayingSong.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onResume() {
        super.onResume();
        try {
            boolean isServiceRunning = UltilFunctions.isServiceRunning(SongService.class.getName(), context);
            if (isServiceRunning) {
                updateUI();
            } else {
                linearLayoutPlayingSong.setVisibility(View.GONE);
            }
            changeButton();
            changeShuffle();
            PlayerConstants.PROGRESSBAR_HANDLER = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Integer i[] = (Integer[]) msg.obj;
                    textBufferDuration.setText(UltilFunctions.getDuration(i[0]));
                    textDuration.setText(UltilFunctions.getDuration(i[1]));
                    progressBar.setMax(i[1]);
                    progressBar.setProgress(i[0]);
                }
            };
        } catch (Exception e) {
        }
    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    public static void changeShuffle() {
        if (PlayerConstants.SONG_SHUFFLE) {
            btnShuffle.setVisibility(View.VISIBLE);
            btnUnShuffle.setVisibility(View.GONE);
        } else {
            btnUnShuffle.setVisibility(View.VISIBLE);
            btnShuffle.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Permission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSTION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSTION_REQUEST);
                Log.d("PERMISSION", "2222");
            }
        } else {
            init();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Result", "result");
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("close_activity", false)) {
            this.finish();
        }
        if (intent.getBooleanExtra("open_activity", false)) {
            Intent intent1 = new Intent(MainActivity.this, AudioPlayerActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            isFinish = true;
        }
    }
}
