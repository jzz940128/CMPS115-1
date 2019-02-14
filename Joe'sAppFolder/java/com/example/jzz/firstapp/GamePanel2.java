package ucsc.wildfirestudios.BattleForWesteros;

//-------------------------------------------------------------------------------------
// GamePanel.java
// Wildfire Studios
// Created by: Zhizhou Jiang
// Modified by: Margarita Fernandez, zhizhou Jiang, Thomas Zhen
// Notes: Visual output for 2D game state.
//-------------------------------------------------------------------------------------

/* Game panel2 is the visual output from our 2D game state. It calls the tic method (SecondThread initialization)
 * and calls the draw method for everything to be displayed. You can basically think of it as a
 * "gameActivity" class in terms of its usage.
 *
 * NOTE: if you have any new classes to be drawn (critter, decoration, item, enemy, etc.) it will need
 * to be done so in the draw method of this class.
 * Note by:  Ian
 *
 * GamePanel2 use the same mapManager with Gamepanel, it initialize the Page number as 3 so it starts the 4th map
 * Note By: Zhizhou Jiang
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class GamePanel2 extends SurfaceView implements SurfaceHolder.Callback {
    /* DATA --------------------------------------------------------------------------- */
    private SecondThread thread2;
    private RectPlayer player;
    private Point clickerPoint;
    private MapManager map;
    private int deathAnimation = 0;

    /* Castle and Castle's position*/
    private ImgCastle castle;
    private Point castlePoint;

    private ArrayList<RectEnemy> enemy;

    private boolean tilesNotProcessed = true;

    private RectBoss boss;

    // Init the current number of the map
    int page_num;
    /* CONSTRUCTOR -------------------------------------------------------------------- */

    /* The constructor starts up a new tic method with this context, creates a player
       with an initial point of 150 150, and creates the map manager (the tiles on the
       screen)*/
    public GamePanel2(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread2 = new SecondThread(getHolder(), this);

        // initial player coordinates
        player = new RectPlayer(getContext(), new Coord(150, 150));
        clickerPoint = new Point(150, 150);
        /*Initializes the enemy arrayList*/
        enemy = new ArrayList<RectEnemy>();
        // castle
        castle = new ImgCastle(getContext());
        castlePoint = new Point(700, 50);

        // enemy & enemy point
        for (int i = 0; i < 5; i++) {
            enemy.add(new RectEnemy(getContext(), player));

        }

        // init map and the object on the map
        page_num = 3; // dungeon2 starts at map3
        map = new MapManager(getContext(), page_num, enemy);
        castle.update(castlePoint);
        setFocusable(true);

        player.setEnemies(enemy);

        boss = new RectBoss(getContext());
    }


    /* MAIN MODIFIERS ----------------------------------------------------------------- */


    // void update()
    // Updates game state (player, tiles, enemies...). As of now, only updates player
    // because it is the only entity that is affected during gameplay
    /* Ian - If I want to do something like tiles updating or being animated, then it may
       need to be done in this method. */
    public void update() {
        boss.update();

        // Initializes Enemy locations if necessary, otherwise updates page number
        if (!map.enemiesInitialized())
            enemy = map.updateEnemies(page_num);
        else
            map.update(page_num);


        // go to screen 5: page4
        if (page_num == 3 && (clickerPoint.x > 1000 && clickerPoint.x < 1200)
                && (clickerPoint.y > 1000 && clickerPoint.y < 1200)) {

            // set the current page number & reset player position
            page_num = 4;
            player = new RectPlayer(getContext(), new Coord(150, 150));
            player.update(clickerPoint, map.getTiles());

            if (!map.enemiesInitialized())
                enemy = map.updateEnemies(page_num);
            else
                map.update(page_num);
        }
        // go to screen 6: page5
        if (page_num == 4 &&(clickerPoint.x > 800 && clickerPoint.x < 1000)
                && (clickerPoint.y > 200 && clickerPoint.y < 400)) {

            page_num = 5;
            player = new RectPlayer(getContext(), new Coord(150, 150));
            castlePoint = new Point(-400, -400);
            castle.update(castlePoint);
            player.update(clickerPoint, map.getTiles());

            if (!map.enemiesInitialized())
                enemy = map.updateEnemies(page_num);
            else
                map.update(page_num);
        }

        /*Added by Ian July 23. Parses through enemy array and updates them */
        for (RectEnemy e : enemy) {
            e.update();
        }
        for (RectEnemy e : enemy) {
            if (e.getHealth() <= 0 || !e.isAlive()) {
                e = null;
            }
        }

        if (tilesNotProcessed) {
            for (Tile t : map.getTiles())
                t.update(map.getTiles());

            /*This will eventually be put back in - it saves on runtime*/

            tilesNotProcessed = false;
        }
        /*Begin death animation if the player dies*/
        if (!player.isAlive()) {
            deathAnimation = 30;
        } /*Once the death animation is over launch a new activity*/
        if (deathAnimation < 0) {
            System.exit(0);
        }
        if (deathAnimation > 0) deathAnimation -= 2;
        player.setEnemies(enemy);
        player.update(clickerPoint, map.getTiles());
        for (RectEnemy e : enemy) if (e == null) enemy.remove(e);
    }


    // void draw()
    // Draws everything (player, map, enemies, items...) to be displayed to canvas. Main
    // draw method of the game. Entities are drawn back to front (so last thing drawn
    // will be "on top" of all other things already drawn. Think layers)
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // fills base of canvas with color specified
        canvas.drawColor(Color.WHITE);
        // draws 2d array of tiles as specified in map
        map.draw(canvas);
        player.draw(canvas);
        if (page_num == 4) castle.draw(canvas);

        for (int i = 0; i < 5; i++) {
            enemy.get(i).draw(canvas);
        }

        boss.draw(canvas);
    }


    /* INTERNAL MODIFIERS ------------------------------------------------------------- */

    // void surfaceCreated()
    // Starts up the tic method of the game state
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread2 = new SecondThread(getHolder(), this);

        thread2.setRunning(true);
        thread2.start();
    }

    // void surfaceChanged()
    // Overrides surface view. Necessary to avoid errors for proper inheritance
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    // void surfaceDestroyed()
    // Tries to handle the tech of the game state
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread2.setRunning(false);
                thread2.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    // boolean onTouchEvent()
    // Called when screen is clicked. Sets clickerPoint to wherever the user touched on
    // the screen
    // *always returns true
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //  clickerPoint.set((int)event.getX(), (int)event.getY());

                int x = (int) event.getX();
                int y = (int) event.getY();


                clickerPoint.set((int) event.getX(), (int) event.getY());
        }

        //  }
        /*Returning false will give the app reasons not to detect the touch in the future, but we want the app
         *to pretty much return every touch*/
        return true;
    }
}
