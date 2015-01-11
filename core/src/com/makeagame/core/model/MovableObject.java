package com.makeagame.core.model;

import com.google.gson.Gson;

public abstract class MovableObject {

    public Attribute model;
    public boolean controllable;
    public boolean outable;

    public class Attribute {
        public String id;
        public int x;
        public int y;
        public int initY;
        public int initX;
        public float initSY;
        public float initSX;
        public float sX;
        public float sY;
        public float aX;
        public float aY;
        public float minSX;
        public float minSY;
        public float maxSX;
        public float maxSY;
        public int w;
        public int h;
    }

    public MovableObject(String gson) {
        model = init(gson);
    }

    public Attribute init(String gson) {
//        Engine.logI("init with gson " + gson);
        Attribute model = new Gson().fromJson(gson, Attribute.class);
        return model;
    }

}
