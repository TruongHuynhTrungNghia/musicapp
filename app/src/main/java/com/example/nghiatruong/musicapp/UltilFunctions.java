package com.example.nghiatruong.musicapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.EditText;


import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Nghia Truong on 12/22/2017.
 */

public class UltilFunctions {

    static String LOG_CLASS="UltilFunctions";

    //check if service is running or not

    public static boolean isServiceRunning(String serviceName, Context context){
        ActivityManager manager =(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for(ActivityManager.RunningServiceInfo serviceInfo: manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(serviceName.equals(serviceInfo.service.getClassName()))
                return true;
        }
        return false;
    }

    //Read songs in external storage


    public static ArrayList<Song> songArrayList(Context context){
        Uri uri= android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver musicResolver=context.getContentResolver();
        Cursor cursor=musicResolver.query(uri,null, MediaStore.Audio.Media.IS_MUSIC+"!=0",null,null);
        ArrayList<Song> songsArrayList =new ArrayList<Song>();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Song songData=new Song();

           String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
           String artist=cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
           String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)) ;
           long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) ;
           String data=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)) ;
           long albumID=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)) ;
           String composer=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)) ;

           songData.setTitle(title);
           songData.setArtist(artist);
           songData.setAlbum(album);
           songData.setDuration(duration);
           songData.setAlbumID(albumID);
           songData.setPath(data);
           songData.setComposer(composer);
           songsArrayList.add(songData);
        }
       cursor.close();
        Log.d("SIZE","SIZE"+songsArrayList.size());
        sortList(songsArrayList);
        return songsArrayList;
    }


    //get Album image
    public static Bitmap getAlbumImg(Context context,long albumID){
        Bitmap bitmap=null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        try{
            final Uri imgUri=Uri.parse("content://media/external/audio/albumart");
            Uri uri= ContentUris.withAppendedId(imgUri,albumID);
            ParcelFileDescriptor parcelFileDescriptor=context.getContentResolver().openFileDescriptor(uri,"r");
            if(parcelFileDescriptor!=null)
            {
                FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
                bitmap=BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
            }
        } catch (Error error) {
        }catch (Exception e){
        }
        return bitmap;
    }
    //get default Album Art

    public static Bitmap getDefaultAlbumImg(Context context){
        Bitmap bitmap = null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        try{
            bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.default_album_img,options);
        }catch (Error error){}
        catch (Exception e){}
            return bitmap;
    }
    //change milliSecond to String
    public static String getDuration (long milliSecond){
        long sec=(milliSecond/1000)%60;
        long min=(milliSecond/(1000*60))%60;
        long hour=milliSecond/(60*60*1000);
        String s ;
        String m;
        if(sec<10){
            s="0"+sec;
        }
        else {
            s=""+sec;
        }
        if(min<10){
             m="0"+min;
        }else {
             m =""+min;
        }
        String h=""+hour;
        String time="";
        if(hour>0){
            time =h+":" +m +":" + s;
        }else {
            time=m+":"+s;
        }
        return time;
    }

    public static boolean currentVersionSupportBigNofication(){
        int sdkVersion=android.os.Build.VERSION.SDK_INT;
        if(sdkVersion>= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            return true;
        }
        return false;
    }
    public static boolean currentVersionSupportLockScreenControls(){
        int sdkVersion=android.os.Build.VERSION.SDK_INT;
        if(sdkVersion>= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            return true;
        }
        return false;
    }
    public static void sortList(ArrayList<Song> songList){
        for(int i=0;i<songList.size();i++){
            for (int j=i;j<songList.size()-1;j++){
                if(songList.get(i).getTitle().compareTo(songList.get(j).getTitle())>=0){
                    Collections.swap(songList,i,j);
                }
            }
        }
    }

    public static int positionChangeCharacterColor(String mainText, String ColorText){
        int position=0;
        if(ColorText.length()==0){
            return -1;
        }
        else {
            position=mainText.indexOf(ColorText);
        }
        return position;
    }
    public static Spannable stringColor(String maintext, String text){
        Spannable wordSpan = new SpannableString(maintext);
        int position=UltilFunctions.positionChangeCharacterColor(maintext.toLowerCase(),text);
        if(position>=0) {
            wordSpan.setSpan(new ForegroundColorSpan(Color.YELLOW), position,
                    position + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return wordSpan;
    }
    public static int songRandom(int songNumber,int songLengh){
        int flag=songNumber;
        Random rand=new Random();
        songNumber=rand.nextInt(songLengh);
        while(songNumber==flag){
            songNumber=rand.nextInt();
        }
        return songNumber;
    }
}
