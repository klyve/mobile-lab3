package com.bjartelarsen.lab30;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

public class Entities {


    ArrayList<Entity> entityList;

    public Entities() {
        entityList = new ArrayList<Entity>();
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public void onDraw(Canvas canvas) {
        for (int i = 0; i < entityList.size(); ++i) {
            //Log.i("Entities", "Drawing entities");
            entityList.get(i).onDraw(canvas, entityList);
        }
    }


}
