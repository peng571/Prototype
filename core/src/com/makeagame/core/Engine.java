package com.makeagame.core;

import java.util.ArrayList;

import org.json.JSONException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.MouseEvent;

public class Engine extends ApplicationAdapter {

    public static boolean LOG = false;
    public static boolean DEBUG = true;
    public static final String TAG = "MakeAGame";

    SpriteBatch batch;
    BitmapFont gameLable;
    Bootstrap bootstrap;

    ArrayList<SignalEvent> signalList = new ArrayList<SignalEvent>();
    ArrayList<RenderEvent> renderList;

    public Engine(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @Override
    public void create() {
        System.out.println("game start");
        if (LOG) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }
        batch = new SpriteBatch();
        bootstrap.resourceFactory(ResourceManager.get());
        Controler.get().register( bootstrap.setMainModel(), bootstrap.setMainView());
        gameLable = new BitmapFont();
        gameLable.setColor(new Color(1, 0, 0, 1));

        texture = new Texture("mr3/role.png");
    }

    Texture texture;
    long time;
    int count = 0;

    @Override
    public void render() {
        // Gdx.gl.glClearColor(Bootstrap.BACKGROUND_COLOR.r, Bootstrap.BACKGROUND_COLOR.g, Bootstrap.BACKGROUND_COLOR.b, Bootstrap.BACKGROUND_COLOR.a);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                signalList.add(new SignalEvent(SignalEvent.MOUSE_EVENT, SignalEvent.ACTION_UP, new int[] { button, screenX, screenY }));
                return super.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                signalList.add(new SignalEvent(SignalEvent.MOUSE_EVENT, SignalEvent.ACTION_DRAG, new int[] { SignalEvent.MouseEvent.ANY_KEY, screenX, screenY }));
                return super.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                signalList.add(new SignalEvent(SignalEvent.MOUSE_EVENT, SignalEvent.ACTION_MOVE, new int[] { MouseEvent.ANY_KEY, screenX, screenY }));
                return super.mouseMoved(screenX, screenY);
            }

            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                logI("touch " + screenX + " " + screenY);

                signalList.add(new SignalEvent(SignalEvent.MOUSE_EVENT, SignalEvent.ACTION_DOWN, new int[] { button, screenX, screenY }));
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean keyDown(int keycode) {
                signalList.add(new SignalEvent(SignalEvent.KEY_EVENT, SignalEvent.ACTION_DOWN, new int[] { keycode }));
                return super.keyDown(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                signalList.add(new SignalEvent(SignalEvent.KEY_EVENT, SignalEvent.ACTION_UP, new int[] { keycode }));
                return super.keyUp(keycode);
            }
        });

        try {
            Controler.get().mainView.signal(signalList);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        signalList.clear();

        // time = System.currentTimeMillis();
        // count = 0;
        // batch.begin();
        // while (System.currentTimeMillis() - time <= 1000) {
        // batch.draw(texture, 0, 0, 0, 0, 128, 128);
        // count++;
        // }
        // batch.end();
        // System.out.println("draws " + count + " times");

        logD("batch begine time " + System.currentTimeMillis());
        batch.begin();
        // batch.enableBlending();
        renderList =     Controler.get().mainView.render(Controler.get().build());
        // Engine.logI("srcH: " + new Integer(renderList.size()).toString());

        for (RenderEvent e : renderList) {
            // if (e.useBlend) {
            // batch.enableBlending();
            // batch.setBlendFunction(e.srcFunc, e.dstFunc);
            // } else {
            // batch.disableBlending();
            // }

            // if (e.useBlend) {
            // batch.setBlendFunction(e.srcFunc, e.dstFunc);
            // } else {
            // batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            // }
            batch.setColor(e.color);

            switch (e.type) {
            case RenderEvent.IMAGE:
                // if (e.texture != null) {
                // batch.draw(e.texture, e.x, Bootstrap.screamHeight() - e.y - e.dstH, 0, 0, e.srcW, e.srcH, e.ratioX, e.ratioY, e.angle);
                // draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
                // Texture texture = ResourceManager.get().textureMap.get(e.s);
                Texture texture = ResourceManager.get().textureMap.get(e.res.path);
                // Engine.logI("x: " + new Float(e.x).toString());
                // Engine.logI("y: " + new Float(e.y).toString());

                int dim[] = e.res.getSrcDim();
                int dim2[] = new int[] { e.srcX, e.srcY, e.srcW, e.srcH };
                dim2[0] = dim2[0] + dim[0];
                dim2[1] = dim2[1] + dim[1];
                dim2[2] = dim2[2] == -1 ? dim[2] : dim2[2];
                dim2[3] = dim2[3] == -1 ? dim[3] : dim2[3];

                dim = inner(dim, dim2);
                int srcX = dim[0];
                int srcY = dim[1];
                int srcW = dim[2];
                int srcH = dim[3];
//                srcW = 16;
//                srcH = 16;

                // Engine.logI("src: (" + new Integer(srcX).toString() + ","
                // + new Integer(srcY).toString() + ","
                // + new Integer(srcW).toString() + ","
                // + new Integer(srcH).toString());

                // Engine.logI("srcH: " + new Integer(srcH).toString());
                float x = e.x;
                float y = Bootstrap.screamHeight() - e.y - srcH;
                batch.draw(texture, x, y, (float) srcW, (float) srcH, srcX, srcY, srcW, srcH, e.flipX, e.flipY);
                // batch.draw(texture, x, y);
                // }
                break;
            case RenderEvent.LABEL:
                gameLable.draw(batch, e.s, e.x, Bootstrap.screamHeight() - e.y);
                break;
            case RenderEvent.SOUND:
                Sound sound = ResourceManager.get().soundMap.get(e.res.path);
                sound.play(e.vol);
                break;
            }
        }
        renderList.clear();
        renderList = null;
        batch.end();
        logD("batch end time " + System.currentTimeMillis());
        try {
            Thread.sleep((long) (1000 / Bootstrap.FPS - Gdx.graphics.getDeltaTime()));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    public static void logI(String s) {
        if (LOG) {
            // Gdx.app.log(TAG, s);
            System.out.println(s);
        }
    }

    public static void logD(String d) {
        if (LOG) {
            // Gdx.app.debug(TAG, d);
            System.out.println(d);
        }
    }

    public static void logE(String e) {
        // Gdx.app.error(TAG, e);
        System.out.println(e);
    }

    public static void logE(Exception e) {
        // Gdx.app.error(TAG, null, e);
        System.out.println(e);
    }

    public static int[] inner(int[] a, int[] b) {
        int ax1 = a[0];
        int ay1 = a[1];
        int bx1 = b[0];
        int by1 = b[1];
        // right bottom point
        int ax2 = a[0] + a[2];
        int ay2 = a[1] + a[3];
        int bx2 = b[0] + b[2];
        int by2 = b[1] + b[3];

        // no intersection
        if ((ax1 <= bx1 && ax2 <= bx1) ||
                (bx1 <= ax1 && bx2 <= ax1) ||
                (ax1 >= bx2 && ax2 >= bx2) ||
                (bx1 >= ax2 && bx2 >= ax2) ||

                (ay1 <= by1 && ay2 <= by1) ||
                (by1 <= ay1 && by2 <= ay1) ||
                (ay1 >= by2 && ay2 >= by2) ||
                (by1 >= ay2 && by2 >= ay2)

        ) {
            return new int[] { 0, 0, 0, 0 };
        } else {
            ax1 = Math.max(ax1, bx1);
            ay1 = Math.max(ay1, by1);
            ax2 = Math.min(ax2, bx2);
            ay2 = Math.min(ay2, by2);
            return new int[] { ax1, ay1, ax2 - ax1, ay2 - ay1 };
        }

    }
    
}
