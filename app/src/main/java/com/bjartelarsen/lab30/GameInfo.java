package com.bjartelarsen.lab30;

import android.media.MediaPlayer;
import android.os.Vibrator;

class GameInfo {
    private static final GameInfo ourInstance = new GameInfo();

    static GameInfo getInstance() {
        return ourInstance;
    }

    public int margin = 10;
    public int dh = 0;
    public int dw = 0;

    public float gravity = 0.8f;

    public float orientationX = 0;
    public float orientationY = 0;

    public int targetFrameRate = 60;


    static void setDH(int dh) {
        getInstance().dh = dh;
    }

    static void setDW(int dw) {
        getInstance().dw = dw;
    }

    static void setDeviceOrientation(float[] orientation) {
        GameInfo instance = getInstance();
        instance.orientationX = orientation[0] / 5;
        instance.orientationY = orientation[1] / 5;
    }


    private GameInfo() {
    }
}
