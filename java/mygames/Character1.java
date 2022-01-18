package mygames;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Character1 {
    private Bitmap image;
    public float x,y;
    public int current_hex= 60;
    public boolean updatee = false;
    public float next_mouse_position_x,next_mouse_position_y;
    public float xVelocity,yVelocity;
    public HexNode current_hexNode_position;
    public int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public boolean gameover=false;
    public boolean mouseisgone = false;

    public int leftside,rightside,upside,downside;



    public Character1(Bitmap bmp,float xx, float yy, int scalefactor) {
        image = bmp;
        image = Bitmap.createScaledBitmap(image,scalefactor*2, scalefactor*2,false);
        x = xx;
        y= yy;
        xVelocity=7;
        yVelocity=7;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null); //
    }


    public void update(){



        if(gameover){
            int rv = character_disappear();
            if(rv == 1){ // move left
                x = x - xVelocity;

                if(x < -50){
                    mouseisgone = true;
                }
            }
            else if (rv == 2 ){ // move right
                x = x + xVelocity;

                if(x > screenWidth+50){
                    mouseisgone = true;
                }
            }
            else if (rv == 3 ){ // move up
                y = y - yVelocity;
                if(y < -50){
                    mouseisgone = true;
                }

            }
            else if (rv == 4 ){ // move down
                y = y + yVelocity;
                if(y > downside+50){
                    mouseisgone = true;
                }
            }

            return;
        }
        if(updatee){
            //guarantee even number
            float temp_x = x- next_mouse_position_x;
            float temp_y = y- next_mouse_position_y;


            if(temp_x >0) {
                x = x - xVelocity;
            }
            else{
                x = x + xVelocity;
            }
            if(temp_y >0) {
                y = y - yVelocity;
            }
            else{
                y = y + yVelocity;
            }
            if(temp_y ==  0 && temp_x== 0){
                // do nothing
                updatee = false;
            }
        }



    }


    public void set_mouse_coord(float xxx, float yyy , int position){
        updatee = true;
        this.next_mouse_position_x = xxx;
        this.next_mouse_position_y = yyy;
        this.current_hex = position;
    }


    //get the character off the screen if it is at the goalnode
    // leftside corresponds to the min x coordinates of hexnodes
    // rightside '''''''''' to the max '''''''''''''''''''''''''

    public int character_disappear(){
        HexNode hexinstance = new HexNode();
        hexinstance = current_hexNode_position;
        //if it is at a goal node
        if(isGoalNodes(hexinstance.position)){
            if(hexinstance.hex_x <= leftside+xVelocity){
                // move left

                return 1;

            }
            else if(hexinstance.hex_x >= rightside-xVelocity){
                //move right
                return 2;
            }
            if(hexinstance.hex_y <= upside+yVelocity){
                // move up
                return 3;
            }
            else if(hexinstance.hex_y >= -yVelocity){
                //move down;
                return 4;
            }
            // disappear to the sides
        }
        return 0;
    }

    public boolean isGoalNodes(int node_position){
        if(node_position>=0 && node_position <= 10){
            return true;
        }
        if(node_position>=110 && node_position <= 120){
            return true;
        }
        if ((node_position+1)%11 == 0 || node_position%11 == 0 ) {
            return true;
        }
        return false;
    }


}

