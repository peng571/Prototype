package com.makeagame.core.model;

import java.util.ArrayList;

import com.google.gson.Gson;

public class AnimationObject extends MovableObject {

    ArrayList<Mission> missionList;
    Mission currentMission;

    int deviation = 5;

    public AnimationObject(String gson) {
        super(gson);
        missionList = new ArrayList<Mission>();
    }

    public void run() {
        if (!missionList.isEmpty()) {
            if (currentMission == null) {
                currentMission = missionList.get(0);
                currentMission.start(model);
            }
            currentMission.run(model);
//            switch (currentMission.MessionID()) {
//            case 1:
//                MoveMission moveMession = (MoveMission) currentMission;
//                moveMession.run(model);
//                break;
//            case 2:
//                ShapeMission shapeMssion = (ShapeMission) currentMission;
//                shapeMssion.run(model);
//                break;
//            }
        }
    }

    public void moveTo(int x, int y, Action callback) {
        // System.out.println("from " + model.x + ", " + model.y + " move to " + x + ", " + y);
        MoveMission mission = new MoveMission(x, y, callback);
        missionList.add(mission);
    }

    public void shapeTo(int w, int h, int time, Action callback)    {
        ShapeMission mission = new ShapeMission(w, h, time, callback);
        missionList.add(mission);
    }

    // public void stop(Action callback) {
    // model.sX = 0;
    // model.sY = 0;
    // if (callback != null) {
    // callback.execute();
    // }
    // }

    class MoveMission implements Mission {
        public int x;
        public int y;
        public float slowDownLimitX, slowDownLimitY;
        public Action callback;

        public MoveMission(int dstX, int dstY, Action callback) {
            this.x = dstX;
            this.y = dstY;
            this.callback = callback;
        }

        public MoveMission(int dstX, int dstY) {
            this(dstX, dstY, null);
        }

        public void start(Attribute model) {
            int degreeX = Math.abs(model.x - x);
            int degreeY = Math.abs(model.y - y);
            slowDownLimitX = (model.maxSX * model.maxSX) / (2 * model.aX);
            slowDownLimitY = (model.maxSY * model.maxSY) / (2 * model.aY);
            if (slowDownLimitX > degreeX) {
                slowDownLimitX = degreeX / 2;
            }
            if (slowDownLimitY > degreeY) {
                slowDownLimitY = degreeY / 2;
            }
        }

        public void run(Attribute model) {
            if (Math.abs(x - model.x) < deviation && Math.abs(y - model.y) < deviation) {
                model.x = x;
                model.y = y;
                stop();
            } else {
                model.sX += Math.abs(x - model.x) > deviation ? model.aX * (x > model.x ? 1 : -1)
                        * (Math.abs(x - model.x) > slowDownLimitX ? 1 : -1) : 0;
                model.sY += Math.abs(y - model.y) > deviation ? model.aY * (y > model.y ? 1 : -1)
                        * (Math.abs(y - model.y) > slowDownLimitY ? 1 : -1) : 0;
                model.sX = Math.abs(model.sX) > model.maxSX ? model.maxSX * (model.sX > 0 ? 1 : -1) : model.sX;
                model.sY = Math.abs(model.sY) > model.maxSY ? model.maxSY * (model.sY > 0 ? 1 : -1) : model.sY;
                model.x += model.sX;
                model.y += model.sY;
            }
        }

        public void stop() {
            model.sX = 0;
            model.sY = 0;
            if (callback != null) {
                callback.execute();
            }
            missionList.remove(this);
            currentMission = null;
        }

    }

    class ShapeMission implements Mission {

        int w;
        int h;
        int time;
        int shapingW, shapingH;
        public Action callback;

        public ShapeMission(int dstW, int dstH, int time, Action callback) {
            System.out.println("add shape mission");
            this.w = dstW;
            this.h = dstH;
            this.time = time;
            this.callback = callback;
        }

        @Override
        public void start(Attribute model) {
            System.out.println("start shape mission");
            shapingW = (model.w - w) / time;
            shapingH = (model.h - h) / time;
        }

        @Override
        public void run(Attribute model) {
            System.out.println("run shape mission");
            model.w -= shapingW;
            model.h -= shapingH;
            if (model.w < 0) {
                model.w = 0;
            }
            if (model.h < 0) {
                model.h = 0;
            }
            if (model.w <= w && model.h <= h) {
                stop();
            }
        }

        @Override
        public void stop() {
            System.out.println("stop shape mission");
            if (callback != null) {
                callback.execute();
            }
            missionList.remove(this);
            currentMission = null;
        }

    }

    interface Mission {

        public void start(Attribute model);

        public void run(Attribute model);

        public void stop();

    }

    public String info() {
        return new Gson().toJson(this);
    }

}
