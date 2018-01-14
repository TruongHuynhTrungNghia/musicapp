package com.example.nghiatruong.musicapp;

import android.content.Context;
import android.widget.Toast;

import com.example.nghiatruong.musicapp.R;
import com.example.nghiatruong.musicapp.UltilFunctions;
import com.example.nghiatruong.musicapp.PlayerConstants;
import com.example.nghiatruong.musicapp.SongService;

/**
 * Created by Nghia Truong on 12/24/2017.
 */

public class Controls  {
    public static void playControl(Context context) {

        sendMessage(context.getResources().getString(R.string.play));
    }

    public static void pauseControl(Context context) {
        sendMessage(context.getResources().getString(R.string.pause));
    }

    public static void nextControl(Context context) {
        boolean isServiceRunning=UltilFunctions.isServiceRunning(SongService.class.getName(),context);
        if(!isServiceRunning){
            return;
        }
        if(PlayerConstants.SONG_SHUFFLE){
            PlayerConstants.SONG_NUMBER=UltilFunctions.songRandom(
                    PlayerConstants.SONG_NUMBER,PlayerConstants.SONG_LIST.size());
            PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
        }
        else {
            if (PlayerConstants.SONG_LIST.size() > 0) {
                if (PlayerConstants.SONG_NUMBER < (PlayerConstants.SONG_LIST.size() - 1)) {
                    PlayerConstants.SONG_NUMBER++;
                    PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
                } else {
                    PlayerConstants.SONG_NUMBER = 0;
                    PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
                    if (PlayerConstants.SONG_REPEAT == 0) {
                        PlayerConstants.SONG_PAUSE = true;
                        sendMessage("Pause");
                    }
                }
            }
        }
        PlayerConstants.SONG_PAUSE=false;
    }

    public static void previousControl(Context context) {
        boolean isServiceRunning=UltilFunctions.isServiceRunning(SongService.class.getName(),context);
        if(!isServiceRunning){
            return;
        }
        if(PlayerConstants.SONG_SHUFFLE){
            PlayerConstants.SONG_NUMBER=UltilFunctions.songRandom(
                    PlayerConstants.SONG_NUMBER,PlayerConstants.SONG_LIST.size());
            PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
        }
        else {
            if (PlayerConstants.SONG_LIST.size() > 0) {
                if (PlayerConstants.SONG_NUMBER > 0) {
                    PlayerConstants.SONG_NUMBER--;
                    PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
                } else {
                    PlayerConstants.SONG_NUMBER = PlayerConstants.SONG_LIST.size() - 1;
                    PlayerConstants.SONG_CHANGED_HANDLER.sendMessage(PlayerConstants.SONG_CHANGED_HANDLER.obtainMessage());
                }
            }
        }
        PlayerConstants.SONG_PAUSE=false;
    }

    public static void shuffleControl(Context context){
        PlayerConstants.SONG_NUMBER_CHANGED.sendMessage(PlayerConstants.SONG_NUMBER_CHANGED.obtainMessage());
    }

    private static void sendMessage(String message) {
        try{
            PlayerConstants.PLAY_PAUSE_HANDLER.sendMessage(PlayerConstants.PLAY_PAUSE_HANDLER.obtainMessage(0,message));
        }catch(Exception e){}
    }

    public static void repeatControl(Context context) {
        if(PlayerConstants.SONG_REPEAT<2){
            PlayerConstants.SONG_REPEAT++;
        }else if(PlayerConstants.SONG_REPEAT==2){
            PlayerConstants.SONG_REPEAT=0;
        }

    }
}
