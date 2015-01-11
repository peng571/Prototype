package com.makeagame.first;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.model.MovableObject;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.View;

/**
 *[小型][反應] 森林豐收季
 * https://makeagame.hackpad.com/--DzTnljwQd57
 * 
 * TODO 
 * @author Peng
 *
 */
public class FunnyForest {

    private Engine engine;

    public Engine getEngine() {
        return engine;
    }

    public FunnyForest() {

        engine = new Engine(new Bootstrap() {

            @Override
            public View setMainView() {
                return new GameView();
            }

            @Override
            public Model setMainModel() {
                return new GameModel();
            }

            @Override
            public void resourceFactory(ResourceManager resource) {
                // TODO:
//                resource.bind("pear1", new Resource().image("image/pear4.png"));
//                resource.bind("pear2", new Resource().image("image/avocado.png"));
//                resource.bind("banana1", new Resource().image("image/banana4.png"));
//                resource.bind("banana2", new Resource().image("image/banana7.png"));
//                for (int i = 1; i <= 5; i++) {
////                    resource.bind("mengo" + i, new Resource().image("image/mengo.png").src(128 * i, 0, 128, 128));
//                }
//                resource.bind("boom", new Resource().image("image/boom3.png"));
//                resource.bind("bird", new Resource().image("image/bird.png").attribute("data/bird.txt"));
//                resource.bind("fruit", new Resource().attribute("data/fruit.txt"));
//                resource.bind("timmer", new Resource().attribute("data/game.txt"));
            }
        });
    }

    class GameView implements View {

        int mouseX, mouseY;
        int touchStartX, touchStartY;

        @Override
        public void signal(ArrayList<SignalEvent> signalList) {
            Sign sign = new Sign();
            for (SignalEvent s : signalList) {
                if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                    if (s.action == SignalEvent.ACTION_DOWN) {
                        sign.type = 1;
                        sign.x = s.signal.x;
                        sign.y = s.signal.y;
                    } else if (s.action == SignalEvent.ACTION_DRAG) {
                        sign.type = 2;
                        sign.x = s.signal.x;
                        sign.y = s.signal.y;
                    }
                }
            }
//            Controler.get().call("main", new Gson().toJson(sign));
        }

        @Override
        public ArrayList<RenderEvent> render(String build) {
            ArrayList<RenderEvent> list = new ArrayList<RenderEvent>();
                Hold hold = new Gson().fromJson(build, Hold.class);
                // TODO:
//                list.add(new RenderEvent(ResourceManager.get().fetch("bird")).XY(hold.x, hold.y).srcWH(128, 128).Ratio(0.6f).Rotation(hold.angle));
//                for (Fruit f : hold.fruits) {
//                    list.add(new RenderEvent(ResourceManager.get().fetch(f.type + f.level)).XY(f.x, f.y).srcWH(128, 128));
//                    // list.add(new RenderEvent(ResourceManager.get().fetch(f.type)).XY(f.x, f.y).srcWH(128, 128));
//                }
                list.add(new RenderEvent(String.valueOf(hold.score)).XY(50, 50));
            return list;
        }

        @Override
        public String info() {
            return "main view";
        }
    }

    class GameModel implements Model {

        Random rand = new Random();
        int totalScore;
        BirdModel bird;
        ArrayList<FruitModel> fruits;
        Timmer timmer;
        String[] fruitTypes = { "mengo", "pear", "banana" };

        public GameModel() {
            // bird = create(ResourceManager.get().read("bird"));
            timmer = new Gson().fromJson(ResourceManager.get().read("timmer"), Timmer.class);
            bird = new BirdModel(ResourceManager.get().read("bird"));
            fruits = new ArrayList<FruitModel>();
        }

        class Timmer {
            long reflyTime;
            long reshootTime;
            long shootTime;
            long gameTime;
            long protectTime;
        }

        class FruitModel extends MovableObject {
            String type;
            int level;
            int score;
            boolean failed;
            boolean bomb;
            boolean protect;
            long createTime;

            public FruitModel(String type, int level, String gson) {
                super(gson);
                this.type = type;
                this.level = level;
                model.sX += rand.nextInt(10) - 5f;
                model.sY = model.initSY - rand.nextInt(5);
                level = 1;
                score = 5 * level;
                failed = false;
                bomb = false;
                protect = true;
                createTime = System.currentTimeMillis();
            }

            public void run() {
                model.sY += model.aY;
                if (model.sY > model.maxSY) {
                    model.sY = model.maxSY;
                } else if (model.sY < -model.maxSY) {
                    model.sY = -model.maxSY;
                }

                model.x += model.sX;
                model.y += model.sY;
                // System.out.println(new Gson().toJson(model));

                if (model.y < 0) {
                    model.y = 0;
                } else if (model.y - model.h > Bootstrap.screamHeight()) {
                    model.y = Bootstrap.screamHeight() - model.h;
                }

                if (!protect) {
                    if (model.x < bird.pointX && model.x + model.w > bird.pointX
                            && model.y < bird.pointY && model.y + model.h > bird.pointY) {
                        System.out.println("boom bird!!");
                        totalScore += this.score;
                        level++;
                        bomb = true;
                    }
                } else {
                    if (System.currentTimeMillis() - createTime > timmer.protectTime) {
                        System.out.println("close protected");
                        protect = false;
                    }
                }

                if (model.y > Bootstrap.screamHeight()) {
                    model.x = model.initX;
                    model.y = model.initY;
                    model.sX = rand.nextInt(10) - 5;
                    model.sY = model.initSY;
                    failed = true;
                }
            }
        }

        class BirdModel extends MovableObject {
            boolean flying = false;
            boolean LtoR = true;
            long flyTime;
            float angle;
            int pointX, pointY;
            int displacementX, displacementY;

            public BirdModel(String gson) {
                super(gson);
                angle = 0;
                displacementX = model.x + model.w;
                displacementY = model.y + model.h / 2;
            }

            public void run(String gsonString) {
                Sign signs = new Gson().fromJson(gsonString, Sign.class);
                pointX = model.x + displacementX;
                pointY = model.y + displacementY;

                if (!flying) {
                    if (System.currentTimeMillis() - flyTime > timmer.reflyTime) {
                        if (signs.x != 0 && signs.y != 0 && signs.type == 1) {
                            model.y = signs.y;
                            flying = true;
                            System.out.println("start flying");
                        }
                    }
                } else {
                    if (signs.x != 0 && signs.y != 0) {
                        if (signs.x < model.x) {
                            model.sX -= model.aX;
                        } else if (signs.x > model.x) {
                            model.sX += model.aX;
                        }
                        if (signs.y < model.y) {
                            model.sY -= model.aY;
                        } else if (signs.y > model.y) {
                            model.sY += model.aY;
                        }
                    } else {
                        if (model.sY < model.aY && model.sY > -model.aY) {
                            model.sY = 0;
                        } else {
                            model.sY += model.sY > 0 ? -model.aY : model.aY;
                        }
                    }
                    if (LtoR) {
                        if (model.sX > model.maxSX) {
                            model.sX = model.maxSX;
                        } else if (model.sX < model.minSX) {
                            model.sX = model.minSX;
                        }
                    } else {
                        if (model.sX < -model.maxSX) {
                            model.sX = -model.maxSX;
                        } else if (model.sX > -model.minSX) {
                            model.sX = -model.minSX;
                        }
                    }
                    if (model.sY > model.maxSY) {
                        model.sY = model.maxSY;
                    } else if (model.sY < -model.maxSY) {
                        model.sY = -model.maxSY;
                    }

                    model.x += model.sX;
                    model.y += model.sY;
                    angle = (float) (-1 * Math.toDegrees(Math.sin(model.sY / model.sX)));
                    // System.out.println("sin " + (model.sY / model.sX) + " = " + angle);
                    // System.out.println(new Gson().toJson(m));

                    if (model.y < 0) {
                        model.y = 0;
                    } else if (model.y > Bootstrap.screamHeight()) {
                        model.y = Bootstrap.screamHeight() - model.h;
                    }

                    if (LtoR && model.x > Bootstrap.screamWidth() || (!LtoR && model.x < -model.w)) {
                        flying = false;
                        model.sX = ((LtoR) ? 1 : -1) * model.initSX;
                        LtoR = !LtoR;
                        displacementX += LtoR ? 1 : -1 * model.w;
                        flyTime = System.currentTimeMillis();
                        System.out.println("stop flying");
                        // TODO:
                        //ResourceManager.get().fetch("bird").flip(true, false);
                    }
                }
            }
        }

        @Override
        public void process(int command, JSONObject params) {
            // Sign signs = new Gson().fromJson(gsonString, Sign.class);
            if (System.currentTimeMillis() - timmer.shootTime > timmer.reshootTime) {
                // System.out.println("shoot again");
                int num = rand.nextInt(fruitTypes.length);
                fruits.add(new FruitModel(fruitTypes[num], 1, ResourceManager.get().read("fruit")));
                timmer.shootTime = System.currentTimeMillis();
            }
            bird.run("");
            for (ListIterator<FruitModel> it = fruits.listIterator(); it.hasNext();) {
                FruitModel f = it.next();
                f.run();
                if (f.bomb) {
                    it.remove();
                    it.add(new FruitModel(f.type, 2, ResourceManager.get().read("fruit")));
                    it.add(new FruitModel(f.type, 2, ResourceManager.get().read("fruit")));
                }
                if (f.failed) {
                    it.remove();
                }
            }
        }

        @Override
        public String hold() {
            Hold hold = new Hold();
            hold.score = totalScore;
            hold.angle = bird.angle;
            hold.x = bird.model.x;
            hold.y = bird.model.y;
            hold.fruits = new ArrayList<Fruit>();
            for (FruitModel f : fruits) {
                hold.fruits.add(new Fruit(f.type, f.model.x, f.model.y, f.level));
            }
            return new Gson().toJson(hold);
        }

        @Override
        public String info() {
            return "main model";
        }

    }

    class Sign {
        int type;// 1 down, 2 drag
        int x;
        int y;
    }

    class Hold {
        int score;
        float angle;
        int x;
        int y;
        ArrayList<Fruit> fruits;
    }

    class Fruit {
        String type;
        int level;
        int x;
        int y;

        public Fruit(String type, int x, int y, int level) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.level = level;
        }
    }
}
