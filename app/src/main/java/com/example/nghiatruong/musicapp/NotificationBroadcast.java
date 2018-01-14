package com.example.nghiatruong.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Objects;

/**
 * Created by Nghia Truong on 12/24/2017.
 */

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), Intent.ACTION_MEDIA_BUTTON)){
            KeyEvent keyEvent=(KeyEvent)intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            assert keyEvent != null;
            if(keyEvent.getAction()!=KeyEvent.ACTION_DOWN){
                return;
            }
            switch(keyEvent.getKeyCode()){
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if(!PlayerConstants.SONG_PAUSE){
                        Controls.pauseControl(context);
                    }else{
                        Controls.playControl(context);
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("Tag","TAG: KEYCODE_MEDIA_NEXT");
                    Controls.nextControl(context);
                    break;
                case  KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("TAG","TAG: KEYCODE_MEDIA_PREVIOUS");
                    Controls.previousControl(context);
                    break;
            }
        }else{
            if(Objects.equals(intent.getAction(), SongService.NOTIFY_PREVIOUS)){
                Controls.previousControl(context);
            }else if (Objects.equals(intent.getAction(), SongService.NOTIFY_PLAY)){
                Controls.playControl(context);
            }else if(Objects.equals(intent.getAction(), SongService.NOTIFY_NEXT)){
                Controls.nextControl(context);
            }else if(Objects.equals(intent.getAction(), SongService.NOTIFY_DELETE)){
                Intent intent1=new Intent(context,SongService.class);
                context.stopService(intent1);
                if(!MainActivity.isFinish)
                {
                    MainActivity.isFinish=false;
                    Intent intent2=new Intent(context,MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("close_activity",true);
                    context.startActivity(intent2);
                }
            }else if(Objects.equals(intent.getAction(), SongService.NOTIFY_PAUSE)){
                Controls.pauseControl(context);
            }else if(Objects.equals(intent.getAction(), (SongService.NOTIFY_MENU))){
                Intent intent1=new Intent(context,MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("open_activity",true);
                context.startActivity(intent1);
            }

        }
    }

    public String ComponentName() {
        return this.getClass().getName();
    }
}
