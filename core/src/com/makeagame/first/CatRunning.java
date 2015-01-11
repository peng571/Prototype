package com.makeagame.first;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.KeyEvent;
import com.makeagame.core.view.View;

/**
 * a simple game example
 * 
 * @author Peng
 * 
 */
public class CatRunning {

    private Engine engine;

    public CatRunning() {

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
                //resource.bind("cat", new Resource().image("image/pussy.png").attribute("data/cat.txt"));
                //resource.bind("human", new Resource().image("image/person91.png").attribute("data/human.txt"));
            }
        });
    }

    public Engine getEngine() {
        return engine;
    }

    class GameView implements View {

        @Override
        public void signal(ArrayList<SignalEvent> signalList) {
            Sign sign = new Sign();
            sign.left = false;
            sign.up = false;
            sign.down = false;
            sign.right = false;
            sign.enter = false;
            for (SignalEvent s : signalList) {
                if (s.type == SignalEvent.KEY_EVENT) {
                    if (s.signal.press(KeyEvent.ENTER)) {
                        sign.enter = true;
                    }
                    if (s.signal.press(KeyEvent.LEFT)) {
                        sign.left = true;
                    }
                    if (s.signal.press(KeyEvent.UP)) {
                        sign.up = true;
                    }
                    if (s.signal.press(KeyEvent.DOWN)) {
                        sign.down = true;
                    }
                    if (s.signal.press(KeyEvent.RIGHT)) {
                        sign.right = true;
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
//                list.add(new RenderEvent(ResourceManager.get().fetch("cat")).XY(hold.cat.x, hold.cat.y));
//                for (Poistion human : hold.humans) {
//                    list.add(new RenderEvent(ResourceManager.get().fetch("human")).XY(human.x, human.y));
//                }
                
                String text = "";
                if (hold.reseting) {
                    if (hold.countDown == -1) {
                        text = "";
                    } else if (hold.countDown == 0) {
                        text = "start!!!";
                    } else {
                        text = String.valueOf(hold.countDown);
                    }
                } else if (!hold.running) {
                    text = "press Enter to start.";
                }
                list.add(new RenderEvent(text).XY(200, 200));
            return list;
        }

        @Override
        public String info() {
            return "main view";
        }

    }

    class GameModel implements Model {

        boolean alive = true;
        long startTime = 0;
        long countTime = 0;
        boolean reseting = false, running = false;
        int countDown = 0;

        RoleModel cat;
        ArrayList<RoleModel> humans;

        public GameModel() {
            cat = new RoleModel();
            humans = new ArrayList<RoleModel>();
        }

        class RoleModel {
            String id;
            float x, y;
            float speedX = 0, speedY = 0;
            float maxSpeedX, maxSpeedY;
            float a;
            int w, h;
        }

        public RoleModel create(String gsonString) {
            RoleModel model = new Gson().fromJson(gsonString, RoleModel.class);
            model.speedX = 0;
            model.speedY = 0;
            Random rand = new Random();
            model.maxSpeedX += ((rand.nextInt(20) - 10) * 0.1f);
            model.maxSpeedY += ((rand.nextInt(20) - 10) * 0.1f);
            model.a += ((rand.nextInt(10) - 5) * 0.02f);
            return model;
        }

        public void move(RoleModel m, Sign signs) {
            if (m.id.equals("cat")) {
                if (signs.left) {
                    m.speedX -= m.a;
                } else if (m.speedX < 0) {
                    m.speedX += m.a;
                }
                if (signs.right) {
                    m.speedX += m.a;
                } else if (m.speedX > 0) {
                    m.speedX -= m.a;
                }
                if (signs.up) {
                    m.speedY += m.a;
                } else if (m.speedY > 0) {
                    m.speedY -= m.a;
                }
                if (signs.down) {
                    m.speedY -= m.a;
                } else if (m.speedY < 0) {
                    m.speedY += m.a;
                }

            } else if (m.id.equals("human")) {
                if (m.x > cat.x) {
                    m.speedX -= m.a;
                } else if (m.x < cat.x) {
                    m.speedX += m.a;
                }
                if (m.y > cat.y) {
                    m.speedY -= m.a;
                } else if (m.y < cat.y) {
                    m.speedY += m.a;
                }
            }

            if (m.speedX > m.maxSpeedX) {
                m.speedX = m.maxSpeedX;
            } else if (m.speedX < -m.maxSpeedX) {
                m.speedX = -m.maxSpeedX;
            }
            if (m.speedY > m.maxSpeedY) {
                m.speedY = m.maxSpeedY;
            } else if (m.speedY < -m.maxSpeedY) {
                m.speedY = -m.maxSpeedY;
            }

            m.x += m.speedX;
            m.y += m.speedY;
            if (m.x < 0) {
                m.x = 0f;
            } else if (m.x + m.w > Bootstrap.screamWidth()) {
                m.x = Bootstrap.screamWidth() - m.w;
            }
            if (m.y < 0) {
                m.y = 0f;
            } else if (m.y + m.h > Bootstrap.screamHeight()) {
                m.y = Bootstrap.screamHeight() - m.h;
            }

        }


        private void start() {
            reseting = true;
            running = true;
            startTime = System.currentTimeMillis();
            cat = create(ResourceManager.get().read("cat"));
            humans.clear();
            humans.add(create(ResourceManager.get().read("human")));
        }

        @Override
        public String hold() {
            Hold hold = new Hold();
            hold.cat.x = cat.x;
            hold.cat.y = cat.y;
            for (int i = 0; i < humans.size(); i++) {
                Poistion p = new Poistion();
                p.x = humans.get(i).x;
                p.y = humans.get(i).y;
                hold.humans.add(p);
            }
            hold.countDown = countDown;
            hold.reseting = reseting;
            hold.running = running;
            return new Gson().toJson(hold);
        }

        @Override
        public String info() {
            return "main model";
        }

        @Override
        public void process(int command, JSONObject gsonString) {
            Sign signs = null;// = new Gson().fromJson(gsonString, Sign.class);
            if (running) {
                if (reseting) {
                    if (System.currentTimeMillis() - startTime < 1000) {
                        countDown = 3;
                    } else if (System.currentTimeMillis() - startTime < 2000) {
                        countDown = 2;
                    } else if (System.currentTimeMillis() - startTime < 3000) {
                        countDown = 1;
                    } else if (System.currentTimeMillis() - startTime < 4000) {
                        countDown = 0;
                    } else {
                        countDown = -1;
                        startTime = System.currentTimeMillis();
                        reseting = false;
                    }
                } else {
                    if (!alive) {
                        running = false;
                    }
                    move(cat, signs);
                    for (RoleModel human : humans) {
                        move(human, null);
                        if ((cat.x > human.x - human.w / 2 && cat.x < human.x + human.w / 2) && (cat.y > human.y - human.h / 2 && cat.y < human.y + human.h / 2)) {
                            System.out.println("game over");
                            alive = false;
                        }
                    }
                    countTime = System.currentTimeMillis() - startTime;
                    if (countTime > 3000 * humans.size()) {
                        humans.add(create(ResourceManager.get().read("human")));
                    }
                }
            } else {
                if (signs.enter) {
                    start();
                }
            }            
        }

    }

    class Sign {
        boolean up;
        boolean down;
        boolean left;
        boolean right;
        boolean enter;
    }

    class Hold {
        Poistion cat = new Poistion();
        ArrayList<Poistion> humans = new ArrayList<Poistion>();
        boolean reseting, running;
        int countDown;
    }

    class Poistion {
        float x;
        float y;
    }

}
