package com.bjartelarsen.lab30;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.Thread;


public class Canvas extends SurfaceView implements Runnable {
    @Nullable
    Entities entities = null;

    @Nullable
    Context ctx;

    @Nullable
    android.graphics.Canvas canvas;

    @Nullable
    SurfaceHolder surface = null;

    Bitmap backGround           = null;

    Thread thread = null;


    GameInfo gameInfo = GameInfo.getInstance();

    int dw = 0;
    int dh = 0;


    boolean running = false;

    int frameCounter = 0;

    interface OnTouchInterface {
        void onTouch(int eventAction, int x, int y);
    }

    OnTouchInterface onTouchCallback;


    public Canvas(Context context) {
        super(context);

        init(null, context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs, context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, context);
    }

    public Canvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs, context);
    }

    private void init(@Nullable AttributeSet attrs, Context context) {
        surface = getHolder();
        ctx = context;

    }

    public void addEntities(Entities entobj) {
        entities = entobj;
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        running = false;

        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        thread = null;
    }




    @Override
    public void run() {
            while(running) {
                if (!surface.getSurface().isValid()) {
                    Log.i("Surface", "No surface valid");
                    continue;
                }


                canvas = surface.lockCanvas();
                dh = canvas.getHeight();
                dw = canvas.getWidth();

                GameInfo.setDH(dh);
                GameInfo.setDW(dw);


                canvas.drawColor(Color.BLACK);

                drawBorder(canvas);

                if(entities != null)
                    entities.onDraw(canvas);

                //canvas.drawRect(200, 20, 500, 100, p);
                surface.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(1000 / gameInfo.targetFrameRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                frameCounter++;
                //Log.i("FrameCounter", String.valueOf(frameCounter));


            }

    }

    public void onTouch(OnTouchInterface onTouch) {
        this.onTouchCallback = onTouch;
    }


    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        // you may need the x/y location
        int x = (int)event.getX();
        int y = (int)event.getY();


        onTouchCallback.onTouch(eventAction, x, y);



        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;

    }

    public void drawBorder(android.graphics.Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);

        canvas.drawRect(gameInfo.margin,gameInfo.margin, dw-gameInfo.margin, dh-gameInfo.margin, p);
        //canvas.drawLine(0, 0, 50, 50, p);
    }

}
