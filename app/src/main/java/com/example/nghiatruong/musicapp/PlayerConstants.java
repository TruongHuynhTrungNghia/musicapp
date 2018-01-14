package com.example.nghiatruong.musicapp;

import android.media.browse.MediaBrowser;
import android.provider.MediaStore;

import java.util.ArrayList;
import android.os.Handler;


/**
 * Created by Nghia Truong on 12/23/2017.
 */

public class PlayerConstants {
    //List of songs
    public static ArrayList<Song> SONG_LIST=new ArrayList<Song>();
    //song number which is playing right now
    public static int SONG_NUMBER=0;
    //song is playing or pause
    public static boolean SONG_PAUSE=true;
    //song changed(next,previous)
    public static boolean SONG_SHUFFLE=false;
    //song changed handler define in(SongService)
    public static Handler SONG_CHANGED_HANDLER;
    //song play pause handler define in SongService
    public static Handler PLAY_PAUSE_HANDLER;
    //showing song progress handler define in main act, audio act
    public static Handler PROGRESSBAR_HANDLER;
    public static Boolean IS_PROGRESSBAR_SEEK=false;
    public static int CURRENT_POSITION;

    public static Handler SONG_NUMBER_CHANGED;
    public static int SONG_REPEAT=0;
}
