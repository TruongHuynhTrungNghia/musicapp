package com.example.nghiatruong.musicapp;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nghia Truong on 12/23/2017.
 */

public class SongAdapter extends ArrayAdapter<Song> {

    private ArrayList<Song> listOfSongs = null;
    private ArrayList<Song> temporaryList;
    private Context context;
    private LayoutInflater inflater;
    private Song song;
    private String searchText;

    public SongAdapter(Context context, int custom_list, ArrayList<Song> listOfSongs) {
        super(context, custom_list, listOfSongs);
        this.listOfSongs = listOfSongs;
        temporaryList = new ArrayList<>();
        this.temporaryList.addAll(listOfSongs);
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    private class ViewHolder {
        TextView textViewSongName, textViewSongArtist, textViewDuration;
        ImageView albumImg;
    }

    private ViewHolder holder;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.custom_list, parent, false);
            holder = new ViewHolder();
            holder.textViewSongName = view.findViewById(R.id.texViewSongName);
            holder.textViewSongArtist = view.findViewById(R.id.textViewArtist);
            holder.textViewDuration = view.findViewById(R.id.textDuration);
            holder.albumImg = view.findViewById(R.id.imageViewAlbumArt);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        song = listOfSongs.get(position);
        if (searchText == null) {
            holder.textViewSongArtist.setText(song.getArtist());
            holder.textViewSongName.setText(song.getTitle());

        } else {
            holder.textViewSongName.setText(UltilFunctions.stringColor(song.getTitle(), searchText));
            holder.textViewSongArtist.setText(UltilFunctions.stringColor(song.getArtist(), searchText));
        }
        String duration = UltilFunctions.getDuration(song.getDuration());
        holder.textViewDuration.setText(duration);
        Bitmap bm = UltilFunctions.getAlbumImg(context, song.getAlbumID());
        if (bm != null) {
            holder.albumImg.setBackgroundDrawable(new BitmapDrawable(bm));
        } else {
            holder.albumImg.setBackgroundResource(R.drawable.default_album_img);
        }
        return view;
    }

    public void filter(String text) {
        listOfSongs.clear();
        if (text.length() == 0) {
            listOfSongs.addAll(temporaryList);
            searchText = null;
        } else {
            for (Song element : temporaryList) {
                if (element.getTitle().toLowerCase(Locale.getDefault()).contains(text) ||
                        element.getArtist().toLowerCase(Locale.getDefault()).contains(text)) {
                    listOfSongs.add(element);
                    searchText = text;
                }
            }

        }
        notifyDataSetChanged();
    }

}
