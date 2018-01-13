package com.example.nghiatruong.musicapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nghia Truong on 12/23/2017.
 */

public class SongApdapter extends ArrayAdapter<Song> {

    ArrayList<Song> listOfSongs=null;
    ArrayList<Song> temporaryList;
    Context context;
    LayoutInflater inflater;
    Song song;
    String searchText;

    public SongApdapter(Context context, int custom_list, ArrayList<Song> listOfSongs) {
        super(context,custom_list,listOfSongs);
        this.listOfSongs=listOfSongs;
        temporaryList=new ArrayList<Song>();
        this.temporaryList.addAll(listOfSongs);
        this.context=context;
        inflater=LayoutInflater.from(context);

    }

    private class ViewHolder{
        TextView textViewSongname,textViewSongArtist,textViewDuration;
    }

    ViewHolder holder;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view=inflater.inflate(R.layout.custom_list,parent,false);
            holder=new ViewHolder();
            holder.textViewSongname=(TextView)view.findViewById(R.id.texViewSongName);
            holder.textViewSongArtist=(TextView)view.findViewById(R.id.textViewArtist);
            holder.textViewDuration=(TextView)view.findViewById(R.id.textDuration);
            view.setTag(holder);
        }else {
            holder=(ViewHolder)view.getTag();
        }
        song =listOfSongs.get(position);
        if(searchText==null) {
            holder.textViewSongArtist.setText(song.getArtist());
            holder.textViewSongname.setText(song.getTitle());
        }
        else {
            holder.textViewSongname.setText(UltilFunctions.stringColor(song.getTitle().toString(),searchText));
            holder.textViewSongArtist.setText(UltilFunctions.stringColor(song.getArtist().toString(),searchText));
        }
        String duration=UltilFunctions.getDuration(song.getDuration());
        holder.textViewDuration.setText(duration);
        return view;
    }

    public void filter(String text){
        listOfSongs.clear();
        if(text.length()==0){
            listOfSongs.addAll(temporaryList);
            searchText=null;
        }else{
            for(Song element:temporaryList){
                if(element.getTitle().toLowerCase(Locale.getDefault()).contains(text)||
                        element.getArtist().toLowerCase(Locale.getDefault()).contains(text)){
                    listOfSongs.add(element);
                    searchText=text;
                }
            }

        }
        notifyDataSetChanged();
    }

}
