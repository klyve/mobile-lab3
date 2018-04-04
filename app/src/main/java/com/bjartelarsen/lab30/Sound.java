package com.bjartelarsen.lab30;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

public class Sound {

    ArrayList<MediaPlayer> soundList;
    Context context;

    public Sound(Context context) {
        this.context = context;
    }

    public void registerSound(int rid) {
        soundList.add(MediaPlayer.create(this.context, rid));
    }

    public MediaPlayer random() {
        int rnd = new Random().nextInt(soundList.size());
        return soundList.get(rnd);
    }

    public void playRandom() {
        MediaPlayer md = this.random();
        md.seekTo(0);
        md.start();
    }
}
