package com.bjartelarsen.lab30;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class Ball extends Entity {


    Canvas canvas = null;

    public float posX = 100;
    public float posY = 100;
    private int radius = 50;

    public float velocityX = 0;
    public float velocityY = 0;

    private int basecolor = Color.WHITE;
    private int hitcolor = Color.RED;

    Paint paint = new Paint();

    GameInfo gameInfo = GameInfo.getInstance();

    interface OnCollideInterface {
        void onCollide(Ball ball);
    }

    OnCollideInterface collideCallback = null;

    ArrayList<Entity> entityList;


    public Ball(int x, int y) {
        this.posX = x;
        this.posY = y;
        init();
    }


    private void init() {
        this.paint.setColor(basecolor);
        this.paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas, @Nullable ArrayList<Entity> entityList) {
        super.onDraw(canvas, entityList);
        this.entityList = entityList;
        this.canvas = canvas;
        this.calculatePosition();
        this.drawBall();

    }
    private boolean collision(float position1, float position2) {
        return (position1 >= position2);
    }


    public void onCollide(OnCollideInterface oncollide) {
        collideCallback = oncollide;
    }

    public float getNextPosX() {
        float velocityX = this.velocityX + gameInfo.orientationX;
        return (this.posX - this.velocityX);
    }

    public float getNextPosY() {
        float velocityY = this.velocityY - gameInfo.orientationY;
        return (this.posY - velocityY);
    }

    private void calculatePosition() {

        float nextPosY =  this.getNextPosY();
        float nextPosX =  this.getNextPosX();

        this.velocityX += gameInfo.orientationX;
        this.velocityY -= gameInfo.orientationY;

        boolean hit = false;
        if(collision(gameInfo.margin, nextPosX-this.radius)) {
            this.velocityX *= -gameInfo.gravity;
            nextPosX = gameInfo.margin + this.radius;
            hit = true;
        }
        if(collision(nextPosX+this.radius, gameInfo.dw-gameInfo.margin)) {
            this.velocityX *= -gameInfo.gravity;
            nextPosX = gameInfo.dw-gameInfo.margin - this.radius;
            hit = true;
        }

        if(collision(gameInfo.margin, nextPosY-this.radius)) {
            this.velocityY *= -gameInfo.gravity;
            nextPosY = gameInfo.margin + this.radius;
            hit = true;
        }
        if(collision(nextPosY+this.radius, gameInfo.dh-gameInfo.margin)) {
            this.velocityY *= -gameInfo.gravity;
            nextPosY = gameInfo.dh-gameInfo.margin - this.radius;
            hit = true;
        }

        if(hit) {
            this.paint.setColor(hitcolor);

            if(this.collideCallback != null)
                collideCallback.onCollide(this);
        }
        this.posX = nextPosX;
        this.posY = nextPosY;
    }


    private void drawBall() {
        int paintcolor = this.paint.getColor();
        if(paintcolor != this.basecolor) {
            this.paint.setColor(lerp(paintcolor, basecolor, 0.7f));
        }
        this.canvas.drawCircle(this.posX, this.posY, radius, paint);
    }





    int lerp(int c0, int c1, float factor) {
        return Math.round(c0 + factor * (c1 - c0));
    }
}
