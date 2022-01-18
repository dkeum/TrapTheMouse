package mygames;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class GameViews extends SurfaceView implements SurfaceHolder.Callback{
    private final mainthreads PrimaryThread;
    //private Context context;
    private Hexagon hexagonn;
    private Character1 mouse;
    private int score =120;
    private boolean gameisover = false;
    private boolean readytoretry = false;
    private int radius;

    private Bitmap game_lose,game_win;

    public GameViews(Context context){

        super(context);
        //this.context = context;
        getHolder().addCallback(this);
        PrimaryThread = new mainthreads(getHolder(), this);
        setFocusable(true);

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){


        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        radius = (int)(screenWidth/23);
        hexagonn = new Hexagon(radius);
        mouse = new Character1(BitmapFactory.decodeResource(getResources(), R.drawable.mouse),hexagonn.mouse_position_x,hexagonn.mouse_position_y,radius);

        game_lose = BitmapFactory.decodeResource(getResources(),R.drawable.mouse_endgame);
        game_lose = Bitmap.createScaledBitmap(game_lose,mouse.screenWidth,mouse.screenHeight,false);
        game_win = BitmapFactory.decodeResource(getResources(),R.drawable.game_win);
        game_win = Bitmap.createScaledBitmap(game_win,mouse.screenWidth,mouse.screenHeight,false);

        int leftside,rightside,upside, downside;
        leftside = (int)hexagonn.NodeList_map.get(1).hex_x;
        upside = (int)hexagonn.NodeList_map.get(0).hex_y;
        rightside= (int)hexagonn.NodeList_map.get(120).hex_x;
        downside = (int)hexagonn.NodeList_map.get(120).hex_y;
        mouse.leftside = leftside;
        mouse.rightside = rightside;
        mouse.upside = upside;
        mouse.downside=  downside;





        PrimaryThread.setRunning(true);
        PrimaryThread.start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (retry) {
            try {
                //temp.setRunning(false);
                PrimaryThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }
    public void update(){
        mouse.update();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {

            canvas.drawColor(Color.rgb(0, 100, 0));
            // no more moves that the mouse can make
            if(gameisover){
                canvas.drawBitmap(game_win,0,0,null);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(0, 0, 0));
                paint.setTextSize(radius);
                // canvas.drawRect(0, 150, 300, 0, paint);
                int xx= (int)(hexagonn.screenWidth/2)-250;
                int yy = (int)(hexagonn.screenHeight/2);
                canvas.drawText("Mouse is Trapped. You Win! ",xx,yy,paint);
                try {
                    PrimaryThread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readytoretry= true;
                return;
            }

            //mouse has won
            if(mouse.mouseisgone){

                canvas.drawBitmap(game_lose,0,0,null);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(0, 0, 0));
                paint.setTextSize(radius);
                // canvas.drawRect(0, 150, 300, 0, paint);
                int xx= (int)(hexagonn.screenWidth/2) - 200;
                int yy = (int)(hexagonn.screenHeight/2);
                canvas.drawText("Mouse has Escaped! ",xx,yy,paint);
                try {
                    PrimaryThread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readytoretry = true;
                return;
            }

            Paint paint = new Paint();
            paint.setTextSize(100f);
            paint.setColor(Color.rgb(250, 250, 250));
            String textscore = "Score: " + score;
            canvas.drawText(textscore,0, 100,paint);
            hexagonn.drawHexagon(canvas);
            mouse.draw(canvas);
            hexagonn.drawIndex_Map(canvas);
            hexagonn.drawInValidNodes(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xx,yy;
        HexNode hexinstance = new HexNode();
        int xxx = (int)event.getX();
        int yyy = (int)event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                if(readytoretry== true){
                    readytoretry = false;
                    gameisover = false;
                    mouse.mouseisgone = false;
                    mouse.gameover = false;
                    hexagonn.invalid_nodes.clear();
                    hexagonn.invalidNodes_Path.reset();
                    mouse.x = hexagonn.NodeList_map.get(60).hex_x-hexagonn.radius;
                    mouse.y= hexagonn.NodeList_map.get(60).hex_y-hexagonn.radius;
                    mouse.current_hex = 60;
                    score = 121;
                    hexagonn.randomize_invalidHexNodes();
                    hexagonn.calculateGoalPosition();
                }


                score--; // decrease the score whenever touch
                //(0) detect which hex has been touched
                //handle cases where mouse cannot move
                hexinstance = hexagonn.Calculate_HexNodeTouch(xxx,yyy);
                //(1) change the hex color to red// or whatever
                hexagonn.paint_New_invalidHex();

                //update goal position if needed
                //************
                /*
                if(hexagonn.calculateNewGoalPosition(mouse.current_hex) == null){
                    gameisover = true;
                    return true;
                }
                */
                if( hexagonn.BFS(mouse.current_hex) == false){
                    gameisover = true;
                    return true;
                }

                xx = hexagonn.calculateNextMove(mouse.current_hex).hex_x - hexagonn.radius;
                yy = hexagonn.next_hexnode.hex_y - hexagonn.radius;
                //************





                mouse.set_mouse_coord(xx,yy,hexagonn.next_hexnode.position);

                //help with the character disappearing to the sides
                mouse.current_hexNode_position = hexagonn.next_hexnode;


                break;

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // touch up code
                if(mouse.isGoalNodes(mouse.current_hex)){//mouse.character_disappear() != 0)
                    mouse.gameover = true;
                    mouse.update();
                    //mouse.updatee = false;
                }
                break;
        }
        return true;
    }
}
