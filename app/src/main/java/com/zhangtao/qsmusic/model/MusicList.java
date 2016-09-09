package com.zhangtao.qsmusic.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tao.zhang on 16-9-7.
 */
public class MusicList  {
    private ArrayList<Music> musics;
    private String name;
    private MusicListType type;
    private int currentIndex;



    public enum MusicListType{
        LIKE,
        ALBUM,
        DEFAULT,
        CUSTOM
    }

    public MusicList(String name, MusicListType type) {
        this.name = name;
        this.type = type;
        this.currentIndex = 0;
        this.musics = new ArrayList<>();
    }

    public MusicList(String name){
        this(name,MusicListType.DEFAULT);
    }

    public ArrayList<Music> getMusics() {
        return musics;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MusicListType getType() {
        return type;
    }

    public void setType(MusicListType type) {
        this.type = type;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Music next(){
        Log.d("next",currentIndex+"-"+musics.size());
        if(musics.size()<=++currentIndex){
            Log.d("next111111111",currentIndex+"");
            currentIndex = 0;
        }
        Log.d("next22222",currentIndex+"");
        return musics.get(currentIndex);
    }

    public Music pre(){
        if(--currentIndex<0){
            if(!(musics.size() == 0)){
                currentIndex = musics.size()-1;
            }else currentIndex = -1;
        }
        return musics.get(currentIndex);
    }

    public int indexOf(Music music){
        for (Music m:musics){
            if (music.equals(m)){
                return musics.indexOf(m);
            }
        }
        return -1;
    }

    public void setCurrentMusic(Music music){
        this.currentIndex = indexOf(music);
    }

    public Music current(){
        return musics.size() == 0?null:musics.get(currentIndex);
    }

    public Music get(int index){
        return musics.get(index);
    }
}
