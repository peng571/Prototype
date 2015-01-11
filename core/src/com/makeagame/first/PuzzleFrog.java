package com.makeagame.first;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Action;
import com.makeagame.core.model.AnimationObject;
import com.makeagame.core.model.Model;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.KeyEvent;
import com.makeagame.core.view.View;

/**
 * 轉珠(三消)遊戲
 * not finish yet
 * 
 */
public class PuzzleFrog {

    private Engine engine;

    final static int BALL_W = 60;
    final static int BALL_H = 60;
    final static int ROW = 7;
    final static int COL = 7;

    public PuzzleFrog() {

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
//                resource.bind("ball", new Resource().attribute("data/automove.txt"));
//                resource.bind("ball1", new Resource().image("image/black.png"));
//                resource.bind("ball2", new Resource().image("image/blue.png"));
//                resource.bind("ball3", new Resource().image("image/grey.png"));
//                resource.bind("ball4", new Resource().image("image/green.png"));
//                resource.bind("ball5", new Resource().image("image/orange.png"));
//                resource.bind("ball6", new Resource().image("image/pink.png"));
//                resource.bind("ball7", new Resource().image("image/red.png"));
//                resource.bind("boom", new Resource().image("image/boom3.png"));
                
                // or
                resource.bindImage("ball", "data/automove.txt");
                resource.bindImage("ball1", "image/black.png");
                resource.bindImage("ball2", "image/blue.png");
                resource.bindImage("ball3", "image/grey.png");
                resource.bindImage("ball4", "image/green.png");
                resource.bindImage("ball5", "image/orange.png");
                resource.bindImage("ball6", "image/pink.png");
                resource.bindImage("ball7", "image/red.png");
                resource.bindImage("boom", "image/boom3.png");
            }
        });
    }

    public Engine getEngine() {
        return engine;
    }

    class GameView implements View {

        private int gameState = -1; // 1 user move, 2 ball remove, 3 new ball drop

        int startX = 100, startY = 100;
        int ballW = 48;
        int ballH = 48;
        Sign sign;

        Random rand = new Random();
        final static int BALL_W = 60;
        final static int BALL_H = 60;

        Ball[][] balls = new Ball[ROW][COL];
        int downX, downY;
        int upX, upY;
        int move;

        public GameView()
        {
            String ballInitJson = ResourceManager.get().read("ball");
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    balls[i][j] = new Ball(ballInitJson, i, j);
                }
            }
        }

        @Override
        public void signal(ArrayList<SignalEvent> signalList) {
            for (SignalEvent s : signalList) {
                if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                    if (s.signal.press(KeyEvent.ANY_KEY) && s.action == SignalEvent.ACTION_DOWN) {
                        sign = new Sign();
                        downX = s.signal.x - startX;
                        downY = s.signal.y - startY;
                    }
                    if (s.action == SignalEvent.ACTION_UP) {
                        upX = s.signal.x - startX;
                        upY = s.signal.y - startY;
                        sign.row = downX / BALL_W;
                        sign.col = downY / BALL_H;
                        move = -1;
                        if (Math.abs(downX - upX) > BALL_W / 2) {
                            move = downX > upX ? 0 : 2;
                        }
                        if (Math.abs(downY - upY) > BALL_H / 2) {
                            move = downY > upY ? 1 : 3;
                        }
                        System.out.println("get move " + move);
                        sign.move = move;
//                        Controler.get().call("main", new Gson().toJson(sign));
                    }
                }
            }
        }

        class Ball extends AnimationObject {

            boolean trans;

            public Ball(String gson, int raw, int col) {
                super(gson);
                model.x = BALL_W * raw;
                model.y = BALL_H * col;
                System.out.println("add new ball " + model.x + ", " + model.y);
                model.h = BALL_H;
                model.w = BALL_W;
            }

            // public void transformation(int dstW, int dstH, long time) {
            // trans = true;
            //
            // }

            @Override
            public void run() {
                super.run();
            }
        }

        int[][] temp;
        ArrayList<GameModel.Ball> tempRemove;

        @Override
        public ArrayList<RenderEvent> render(String build) {

            ArrayList<RenderEvent> list = new ArrayList<RenderEvent>();
                final Hold hold = new Gson().fromJson(build, Hold.class);

                if (temp == null) {
                    temp = hold.ballMap;
                }
                for (int i = 0; i < ROW; i++) {
                    for (int j = 0; j < COL; j++) {
                        balls[i][j].run();
                        if (hold.ballMap[i][j] > 0) {
                            // TODO:
//                            list.add(new RenderEvent(ResourceManager.get().fetch("ball" + String.valueOf(temp[i][j])))
//                                    .XY(startX + balls[i][j].model.x, startY + balls[i][j].model.y).srcWH(ballW, ballH).dstWH(balls[i][j].model.w, balls[i][j].model.h));
                        }
                    }

                if (hold.moved) {
                    gameState = 1;
                    balls[hold.srcR][hold.srcC].moveTo(balls[hold.dstR][hold.dstC].model.x, balls[hold.dstR][hold.dstC].model.y, null);
                    balls[hold.dstR][hold.dstC].moveTo(balls[hold.srcR][hold.srcC].model.x, balls[hold.srcR][hold.srcC].model.y, new Action() {
                        @Override
                        public void execute() {
                            Ball tempBall = balls[hold.srcR][hold.srcC];
                            balls[hold.srcR][hold.srcC] = balls[hold.dstR][hold.dstC];
                            balls[hold.dstR][hold.dstC] = tempBall;
                            temp = hold.ballMap;
                            // gameState = 2;
                            for (GameModel.Ball ball : hold.remove) {
                                balls[ball.r][ball.c].shapeTo(0, 0, 30, null);
                            }
                        }
                    });
                    // if (gameState == 2) {
                    // System.out.println("3 hold null? " + (hold == null));
                    // System.out.println("3 hold remove null? " + (hold.remove == null));
                    // System.out.println("gson " + new Gson().toJson(hold));
                    // for (GameModel.Ball ball : hold.remove) {
                    // balls[ball.r][ball.c].shapeTo(0, 0, 10, null);
                    // }
                    // gameState = 3;
                    // }
                }

                // if (!hold.remove.isEmpty()) {
                // for (Position p : hold.remove) {
                // list.add(new RenderEvent(ResourceManager.get().fetch("boom")).XY(startX + p.r, startY + p.c).srcWH(ballW, ballH));
                // }
                // }
            }

            return list;
        }

        @Override
        public String info() {
            return "main view";
        }

    }

    class GameModel implements Model {

        Random rand = new Random();

        Ball[][] ballMap = new Ball[ROW][COL];
        ArrayList<Ball> remove;
        boolean moved;

        public GameModel() {
            //
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    ballMap[i][j] = new Ball(i, j, rand.nextInt(7) + 1);
                }
            }
            remove = countBall();
            do {
                if (!remove.isEmpty()) {
                    for (Ball p : remove) {
                        ballMap[p.r][p.c] = new Ball(p.r, p.c, rand.nextInt(7) + 1);
                    }
                    remove.clear();
                }
                remove = countBall();

            } while (!remove.isEmpty());
        }

        class Ball {
            int type;
            int r;
            int c;

            public Ball(int r, int c, int type) {
                this.type = type;
                this.r = r;
                this.c = c;
            }

            public Ball(int r, int c) {
                this(r, c, -1);
            }
        }

        private ArrayList<Ball> countBall() {
            int countColor = -1;
            ArrayList<Ball> temp = new ArrayList<Ball>();
            ArrayList<Ball> remove = new ArrayList<Ball>();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    Ball p = new Ball(i, j);
                    if (ballMap[i][j].type == countColor) {
                        temp.add(p);
                    } else {
                        if (temp.size() >= 3) {
                            remove.addAll(temp);
                        }
                        temp.clear();
                        temp.add(p);
                        countColor = ballMap[i][j].type;
                    }
                }
            }
            countColor = -1;
            temp.clear();
            for (int j = 0; j < COL; j++) {
                for (int i = 0; i < ROW; i++) {
                    Ball p = new Ball(i, j);
                    if (ballMap[i][j].type == countColor) {
                        temp.add(p);
                    } else {
                        if (temp.size() >= 3) {
                            remove.addAll(temp);
                        }
                        temp.clear();
                        temp.add(p);
                        countColor = ballMap[i][j].type;
                    }
                }
            }
            for (Ball p : remove) {
                System.out.println("remove " + p.r + ", " + p.c);
            }
            return remove;
        }

        int row, col;
        int nextRow, nextCol;


        boolean change = true;

        @Override
        public String hold() {
            Hold hold = new Hold();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    hold.ballMap[i][j] = ballMap[i][j].type;
                }
            }
            if (moved) {
                hold.srcR = row;
                hold.srcC = col;
                hold.dstR = nextRow;
                hold.dstC = nextCol;
                hold.moved = moved;
                moved = false;
                hold.remove = (ArrayList<GameModel.Ball>) remove.clone();
                remove.clear();
            }
            return new Gson().toJson(hold);
        }

        @Override
        public String info() {
            return "main model";
        }

        @Override
        public void process(int command, JSONObject json) throws JSONException {
            Sign signs = null;//new Gson().fromJson(gsonString, Sign.class);
            if (signs.move != -1) {

                System.out.println(signs.row + ", " + signs.col);
                row = signs.row;
                col = signs.col;
                moved = false;
                System.out.println("raw " + row + " , cal " + col);
                if (row >= 0 && row < ROW && col >= 0 && col < COL) {
                    nextRow = row;
                    nextCol = col;
                    switch (signs.move) {
                    case 0: // up
                        if (row > 0) {
                            nextRow--;
                        }
                        break;
                    case 1: // left
                        if (col > 0) {
                            nextCol--;
                        }
                        break;
                    case 2: // down
                        if (row < ROW - 1) {
                            nextRow++;
                        }
                        break;
                    case 3: // right
                        if (col < COL - 1) {
                            nextCol++;
                        }
                        break;
                    }
                    moved = true;
                    final int temp = ballMap[nextRow][nextCol].type;
                    ballMap[nextRow][nextCol].type = ballMap[row][col].type;
                    ballMap[row][col].type = temp;

                    // System.out.println(row + " " + col + " move to " + nextRow + " " + nextCol);

                    remove = countBall();
                    for (Ball ball : remove) {
                        ballMap[ball.r][ball.c].type = -1;
                    }
                    for (int i = ROW - 1; i >= 0; i--) {
                        for (int j = 0; j < COL; j++) {
                            if (ballMap[i][j].type == -1) {
                                for (int k = i; k >= 0; k--) {
                                    if (ballMap[k][j].type != -1) {
                                        ballMap[i][j].type = ballMap[k][j].type;
                                        ballMap[k][j].type = -1;
                                        break;
                                    }
                                }
                                if (ballMap[i][j].type == -1) {
                                    ballMap[i][j].type = rand.nextInt(7) + 1;
                                }
                            }
                        }
                    }
                }
            }            
        }

    }

    class Sign {
        int row;
        int col;
        int move;
        int[][] balls = new int[ROW][COL];
    }

    class Hold {
        boolean moved;
        int srcR, srcC;
        int dstR, dstC;
        int[][] ballMap = new int[7][7];
        ArrayList<GameModel.Ball> remove;
    }

}
