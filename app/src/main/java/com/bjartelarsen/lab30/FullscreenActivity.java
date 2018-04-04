package com.bjartelarsen.lab30;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    Canvas view;

    private Vibrator v;
    private Sound s;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new Canvas(this);
      //  s = new Sound(this);
    //    s.registerSound(MediaPlayer.create(this, R.raw.ding));
        mp = MediaPlayer.create(this, R.raw.ding);

        startSensorManagement();
        this.v = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        final Entities entities = new Entities();

        addBalls(entities, view);
        view.addEntities(entities);
        view.onTouch(new Canvas.OnTouchInterface() {
            @Override
            public void onTouch(int eventAction, int x, int y) {

                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN: {
                        entities.addEntity(addBall(x, y));
                    } break;
                }
            }
        });

        setContentView(view);
    }

    private void onBallHit(Ball ball) {
        this.v.vibrate(50);
        //this.s.playRandom();
        this.mp.start();
        this.mp.seekTo(10);

    }

    protected Ball addBall(int x, int y) {
        Ball ball = new Ball(x, y);

        ball.onCollide(new Ball.OnCollideInterface() {
            @Override
            public void onCollide(Ball b) {
                onBallHit(b);
            }
        });

        return ball;
    }

    protected void addBalls(Entities entities, Canvas view) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;;

        Random rand = new Random();
        int randX = rand.nextInt(width);
        int randY = rand.nextInt(height);

        entities.addEntity(addBall(randX, randY));
    }



    private void startSensorManagement() {
        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        } else {
            Log.e("SensorManagerError: ", "nSensorManager cant get system service");
        }

        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                GameInfo gameInfo = GameInfo.getInstance();
                gameInfo.setDeviceOrientation(sensorEvent.values);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        if(sensor != null)
            sensorManager.registerListener(gyroscopeSensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.resume();
    }



    @Override
    public void onPause() {
        super.onPause();
        view.pause();
    }

}
