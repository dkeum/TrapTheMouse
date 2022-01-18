package mygames;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Hexagon {
    public int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public Path hexagonPath;
    public Path hexagonBorderPath;
    public Path invalidNodes_Path;
    public Path Mouse_Path;

    public float radius;
    private int starting_map_x= 0;
    private int starting_map_y= screenWidth/2;

    public float mouse_position_x,mouse_position_y;

    List<HexNode> NodeList_map = new LinkedList<HexNode>();
    public HexNode next_hexnode;

    public Vector<Integer> invalid_nodes = new Vector<Integer>();
    private Vector<Integer> temp = new Vector<Integer>(); // used to store the current calculated mouse path to goal node

    private HexNode goalPosition;


    public Hexagon(float r){
        hexagonPath = new Path();
        hexagonBorderPath = new Path();
        invalidNodes_Path= new Path();
        this.radius = r;
        drawHexagonMap();
        correct_index();
        allowedMoves_Setup();
        randomize_invalidHexNodes();
        calculateGoalPosition();

    }
    public void drawHexagon(Canvas canvas){
        //canvas.drawColor(Color.GREEN);
        canvas.clipPath(this.hexagonPath);
        canvas.drawColor(Color.GREEN);
       // canvas.drawColor(Color.WHITE);
      //  canvas.clipPath(this.hexagonBorderPath);
       // canvas.drawColor(Color.GREEN);
    }
    public void drawInValidNodes(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));
        canvas.drawPath(invalidNodes_Path,paint);
    }
    public void drawMousePath(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 0));
        canvas.drawPath(Mouse_Path,paint);
    }
    public void drawHexagonMap(){
        for(int ii = 0; ii < 11; ii++ ) {
            for(int jj =0; jj< 11; jj++) {

                float xx,yy;

                if(jj%2 == 0) {
                     xx = starting_map_x + (ii * 2 * radius);
                     yy = starting_map_y + ((2*jj) * radius);
                }else{
                    xx = starting_map_x+radius + (ii * 2 * radius);
                    yy = starting_map_y +((2*jj)*radius);
                }


                calculatePath(xx, yy);
                HexNode hexinstance = new HexNode(ii+jj, xx,yy);
                NodeList_map.add(hexinstance);

                //mouse position below
                if(ii == 5 && jj ==5 ){
                    mouse_position_x = xx-radius;//    690
                    mouse_position_y = yy-radius; // = 667
                }

            }
        }
    } // eof



    //returns true is the node is a goal node
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
    private void allowedMoves_Setup(){
        HexNode hexinstance, hexinstance1, hexinstance2, hexinstance3, hexinstance4,hexinstance5;
        // not a goal nodes set up
        for (int ii = 0;ii<NodeList_map.size();ii++){

            if(!isGoalNodes(ii)){

                hexinstance = NodeList_map.get(ii-11);
                hexinstance1 = NodeList_map.get(ii+11);
                hexinstance2 = NodeList_map.get(ii-1); // yes
                hexinstance3 = NodeList_map.get(ii+1); //yes



            if((ii-1)%11 == 0 || (ii-3)%11 == 0 || (ii-5)%11 == 0 ||(ii-7)%11 == 0 || (ii-9)%11 == 0) {
                hexinstance4 = NodeList_map.get(ii + 10);
                hexinstance5 = NodeList_map.get(ii + 12);
            }
            else{
                hexinstance4 = NodeList_map.get(ii - 10);
                hexinstance5 = NodeList_map.get(ii - 12);
            }

                NodeList_map.get(ii).insert(hexinstance);
                NodeList_map.get(ii).insert(hexinstance1);
                NodeList_map.get(ii).insert(hexinstance2);
                NodeList_map.get(ii).insert(hexinstance3);
                NodeList_map.get(ii).insert(hexinstance4);
                NodeList_map.get(ii).insert(hexinstance5);
            }
            else{// corner cases
                if(ii ==0){
                    hexinstance = NodeList_map.get(1);
                    hexinstance1 = NodeList_map.get(11);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                }
                else if(ii == 10){
                    hexinstance = NodeList_map.get(9);
                    hexinstance1 = NodeList_map.get(21);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                }
                else if(ii == 110){
                    hexinstance = NodeList_map.get(99);
                    hexinstance1 = NodeList_map.get(100);
                    hexinstance2 = NodeList_map.get(111);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                }
                else if(ii == 120){
                    hexinstance = NodeList_map.get(108);
                    hexinstance1 = NodeList_map.get(109);
                    hexinstance2 = NodeList_map.get(119);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                }
                else if(ii > 10 && ii < 100 && ii%11 ==0 ){
                    hexinstance = NodeList_map.get(ii-11);
                    hexinstance1 = NodeList_map.get(ii-10);
                    hexinstance2 = NodeList_map.get(ii+1);
                    hexinstance3 = NodeList_map.get(ii+11);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                    NodeList_map.get(ii).insert(hexinstance3);
                }
                else if(ii > 20 && ii < 110 && (ii+1)%11 ==0 ){
                    hexinstance = NodeList_map.get(ii-11);
                    hexinstance1 = NodeList_map.get(ii-12);
                    hexinstance2 = NodeList_map.get(ii+1);
                    hexinstance3 = NodeList_map.get(ii+11);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                    NodeList_map.get(ii).insert(hexinstance3);
                }
                else if(ii ==2 || ii == 4|| ii == 6 || ii == 8){
                    hexinstance = NodeList_map.get(ii-1);
                    hexinstance1 = NodeList_map.get(ii+11);
                    hexinstance2 = NodeList_map.get(ii+1);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                }
                else if(ii == 1|| ii == 3|| ii == 5|| ii == 7|| ii == 9){
                    hexinstance = NodeList_map.get(ii-1);
                    hexinstance1 = NodeList_map.get(ii+10);
                    hexinstance2 = NodeList_map.get(ii+11);
                    hexinstance3 = NodeList_map.get(ii+12);
                    hexinstance4 = NodeList_map.get(ii+11);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                    NodeList_map.get(ii).insert(hexinstance3);
                    NodeList_map.get(ii).insert(hexinstance4);
                }
                else if(ii == 111|| ii==113|| ii==115 || ii==117||  ii==119){
                    hexinstance = NodeList_map.get(ii-1);
                    hexinstance1 = NodeList_map.get(ii-11); // ii = 113 + 11
                    hexinstance2 = NodeList_map.get(ii+1);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                }
                else if(ii==112|| ii==114|| ii==116 || ii==118){
                    hexinstance = NodeList_map.get(ii-1);
                    hexinstance1 = NodeList_map.get(ii-12);
                    hexinstance2 = NodeList_map.get(ii-11);
                    hexinstance3 = NodeList_map.get(ii-10);
                    hexinstance4 = NodeList_map.get(ii+1);
                    NodeList_map.get(ii).insert(hexinstance);
                    NodeList_map.get(ii).insert(hexinstance1);
                    NodeList_map.get(ii).insert(hexinstance2);
                    NodeList_map.get(ii).insert(hexinstance3);
                    NodeList_map.get(ii).insert(hexinstance4);
                }


            }
        }
    }
    public void drawIndex_Map(Canvas canvas){
        for(int ii =0 ; ii < NodeList_map.size(); ii++){
            String textnumber = "" + ii;
            Paint paint = new Paint();
            paint.setTextSize(radius);
            paint.setColor(Color.rgb(250, 250, 250));
            canvas.drawText(textnumber, NodeList_map.get(ii).hex_x-(radius/2),NodeList_map.get(ii).hex_y,paint);
        }

    }
    private void correct_index(){
        for(int ii =0 ; ii < NodeList_map.size(); ii++) {
            NodeList_map.get(ii).position = ii;
        }
    }



    public HexNode calculateNextMove(int current_hex){

        int size =  NodeList_map.get(current_hex).children.size();
        int counter =0 ;
        //(1) get rid of any children within invalid nodes
        for(int ii = 0; ii < size; ii++) {
            int position_child = NodeList_map.get(current_hex).children.get(ii-counter).position;

            //if the child is found in the invalid node list
            if(invalid_nodes.contains(position_child)){
                NodeList_map.get(current_hex).children.remove(ii-counter);
                counter++;
            }
        }



        //(2) get the available moves of the mouse
        size =  NodeList_map.get(current_hex).children.size();

        if(size == 0 ){
            next_hexnode = null;
            return null;
        }

        float position_x,position_y;
        HexNode hexinstance;
        hexinstance = new HexNode();
        float distance = 9999999;
        float calculated_distance_x,calculated_distance_y;
        double calculated_distance;
        next_hexnode = new HexNode();

        for(int ii = 0; ii < size; ii++) {
            position_x = NodeList_map.get(current_hex).children.get(ii).hex_x;
            position_y = NodeList_map.get(current_hex).children.get(ii).hex_y;
            hexinstance = goalPosition;
            calculated_distance_x = (Math.abs(hexinstance.hex_x - position_x)) *(Math.abs(hexinstance.hex_x - position_x));
            calculated_distance_y = (Math.abs(hexinstance.hex_y - position_y)) *(Math.abs(hexinstance.hex_y - position_y));
            calculated_distance = (calculated_distance_x+ calculated_distance_y);
            calculated_distance = Math.sqrt(calculated_distance);

            if(calculated_distance < distance){
                distance = (float)calculated_distance;
                next_hexnode = NodeList_map.get(current_hex).children.get(ii);
            }
        }
        return next_hexnode;
    }

    // function to use the drawhexagonmap
    private void calculatePath(float x, float y) {
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = x;
        float centerY = y;
        hexagonPath.moveTo(centerX, centerY + radius);
        hexagonPath.lineTo(centerX - triangleHeight, centerY + radius/2);
        hexagonPath.lineTo(centerX - triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX, centerY - radius);
        hexagonPath.lineTo(centerX + triangleHeight, centerY - radius/2);
        hexagonPath.lineTo(centerX + triangleHeight, centerY + radius/2);
        hexagonPath.moveTo(centerX, centerY + radius);

        float radiusBorder = radius - 5;
        float triangleBorderHeight = (float) (Math.sqrt(3) * radiusBorder / 2);
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + radiusBorder/2);
        hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - radiusBorder/2);
        hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - radiusBorder/2);
        hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + radiusBorder/2);
        hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
    }


    // chooses a goal position from the beginning of the game
    public HexNode calculateGoalPosition(){
        int min = 0;
        int max = 1;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        if(random_int == 1){
            if(  invalid_nodes.contains(55)){
                return goalPosition = NodeList_map.get(65);
            }
            return goalPosition =NodeList_map.get(55);
        }
        else{
            if(  invalid_nodes.contains(65)){
                return goalPosition =NodeList_map.get(55);
            }
            return goalPosition =NodeList_map.get(65);
        }
    }

    public HexNode calculateNewGoalPosition(int current_hex){
        HexNode hexinstance = new HexNode();
        float position_x,position_y;
        float distance = 9999999;
        float calculated_distance_x,calculated_distance_y;
        double calculated_distance;

        int counter = 0;
        for(int ii = 0; ii < NodeList_map.get(current_hex).children.size();ii++ ){
            if(invalid_nodes.contains(NodeList_map.get(current_hex).children.get(ii).position)){
                counter++;
            }
        }

        if(counter == NodeList_map.get(current_hex).children.size()){
            return null;
        }

        // hexinstance contains the current p
        if(invalid_nodes.contains(goalPosition.position)){
            //calculate new goal position
            for(int ii =0; ii <  NodeList_map.size(); ii++){
                if(isGoalNodes(NodeList_map.get(ii).position)){
                    position_x = NodeList_map.get(ii).hex_x;
                    position_y = NodeList_map.get(ii).hex_y;
                    hexinstance = goalPosition;
                    calculated_distance_x = (Math.abs(hexinstance.hex_x - position_x)) *(Math.abs(hexinstance.hex_x - position_x));
                    calculated_distance_y = (Math.abs(hexinstance.hex_y - position_y)) *(Math.abs(hexinstance.hex_y - position_y));
                    calculated_distance = (calculated_distance_x+ calculated_distance_y);
                    calculated_distance = Math.sqrt(calculated_distance);

                    if(calculated_distance < distance){
                        if(!invalid_nodes.contains(NodeList_map.get(ii).position)){
                            distance = (float)calculated_distance;
                            hexinstance = NodeList_map.get(ii);
                        }
                    }
                }

            }
            return hexinstance;

        }
        // if the current node has no children -> no available move -> then return null
        if(NodeList_map.get(current_hex).children.size() == 0 ){
            return null;
        }
        return goalPosition = hexinstance;
    }


    //randomize the spawning of invalid blocks
    public void randomize_invalidHexNodes(){
        int min,max, random_int1,  random_int;

        for(int ii = 0; ii< 5; ii++){
            min = 0;
            max = 10;
            random_int1 = (int)Math.floor(Math.random()*(max-min+1)+min);
            min = 0;
            max = 2;
             random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
            random_int = random_int1*11+random_int;

            if(random_int == 65){
                random_int += 11;
            }

            if(invalid_nodes.indexOf(random_int) == -1) {
                invalid_nodes.add(random_int);
            }
        }

        // for bottom 3 row
        for(int ii = 0; ii< 5; ii++){
            min = 0;
            max = 10;
            random_int1 = (int)Math.floor(Math.random()*(max-min+1)+min);
            min = 0;
            max = 2;
            random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
            random_int = random_int1*11+random_int+8;

            if(random_int == 65){
                random_int += 11;
            }

            if(invalid_nodes.indexOf(random_int) == -1) {
                invalid_nodes.add(random_int);
            }
        }




        for(int ii = 0; ii< invalid_nodes.size(); ii++){
            float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
            float centerX = NodeList_map.get(invalid_nodes.get(ii)).hex_x;
            float centerY =  NodeList_map.get(invalid_nodes.get(ii)).hex_y;
            invalidNodes_Path.moveTo(centerX, centerY + radius);
            invalidNodes_Path.lineTo(centerX - triangleHeight, centerY + radius/2);
            invalidNodes_Path.lineTo(centerX - triangleHeight, centerY - radius/2);
            invalidNodes_Path.lineTo(centerX, centerY - radius);
            invalidNodes_Path.lineTo(centerX + triangleHeight, centerY - radius/2);
            invalidNodes_Path.lineTo(centerX + triangleHeight, centerY + radius/2);
            invalidNodes_Path.moveTo(centerX, centerY + radius);
        }
    }


    public void CalculateMousePath(){
        for(int ii = 0; ii< temp.size(); ii++){
            float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
            float centerX = NodeList_map.get(invalid_nodes.get(ii)).hex_x;
            float centerY =  NodeList_map.get(invalid_nodes.get(ii)).hex_y;
            Mouse_Path.moveTo(centerX, centerY + radius);
            Mouse_Path.lineTo(centerX - triangleHeight, centerY + radius/2);
            Mouse_Path.lineTo(centerX - triangleHeight, centerY - radius/2);
            Mouse_Path.lineTo(centerX, centerY - radius);
            Mouse_Path.lineTo(centerX + triangleHeight, centerY - radius/2);
            Mouse_Path.lineTo(centerX + triangleHeight, centerY + radius/2);
            Mouse_Path.moveTo(centerX, centerY + radius);
        }

    }

    //for each invalid node paint brown over it
    public void paint_New_invalidHex(){

        //get the last item in the vector
        int last_element = invalid_nodes.lastElement();


        // on touch event it will paint everything in the vector using path
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = NodeList_map.get(last_element).hex_x;
        float centerY = NodeList_map.get(last_element).hex_y;
        invalidNodes_Path.moveTo(centerX, centerY + radius);
        invalidNodes_Path.lineTo(centerX - triangleHeight, centerY + radius/2);
        invalidNodes_Path.lineTo(centerX - triangleHeight, centerY - radius/2);
        invalidNodes_Path.lineTo(centerX, centerY - radius);
        invalidNodes_Path.lineTo(centerX + triangleHeight, centerY - radius/2);
        invalidNodes_Path.lineTo(centerX + triangleHeight, centerY + radius/2);
        invalidNodes_Path.moveTo(centerX, centerY + radius);
    }


    public HexNode Calculate_HexNodeTouch(int xx, int yy){

        float position_x,position_y;
        float distance = 9999999;
        int closet_hex=0;
        float calculated_distance_x,calculated_distance_y;
        double calculated_distance;
        next_hexnode = new HexNode();

        for(int ii = 0; ii < NodeList_map.size(); ii++) {
            position_x = NodeList_map.get(ii).hex_x;
            position_y = NodeList_map.get(ii).hex_y;
            calculated_distance_x = (Math.abs(xx- position_x)) * (Math.abs(xx - position_x));
            calculated_distance_y = (Math.abs(yy - position_y)) * (Math.abs(yy - position_y));
            calculated_distance = (calculated_distance_x + calculated_distance_y);
            calculated_distance = Math.sqrt(calculated_distance);

            if(calculated_distance < distance){
                distance = (float)calculated_distance;
                closet_hex = ii;
            }

        }


        invalid_nodes.add(closet_hex); // update which nodes has been touced
        return  NodeList_map.get(closet_hex);

    }


    public boolean BFS(int current_hex){

        //check if there are any available moves

        int counterr = 0;
        for(int ii = 0; ii < NodeList_map.get(current_hex).children.size();ii++ ){
            if(invalid_nodes.contains(NodeList_map.get(current_hex).children.get(ii).position)){
                counterr++;
            }
        }

        if(counterr == NodeList_map.get(current_hex).children.size()){
            return false;
        }




        // Mark all the vertices as not visited(By default set
        // as false)
        boolean visited[] = new boolean[121];

        //go through the goal nodes if it is not contained in invalid node vector then add to linked list
        for(int ii = 0; ii < NodeList_map.size(); ii++){
            if(isGoalNodes(NodeList_map.get(ii).position)){
                if(!invalid_nodes.contains(NodeList_map.get(ii).position)){
                    temp.add(NodeList_map.get(ii).position);
                }
            }
        }

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[current_hex]=true;
        queue.add(current_hex);




        while (queue.size()!=0)
        {
            // Dequeue a vertex from queue and print it
            current_hex = queue.poll();

            int n;
            //i = adj[current_hex].listIterator();
            int size = NodeList_map.get(current_hex).children.size();



            int counter =0;

            while (counter != size)
            {
                n = NodeList_map.get(current_hex).children.get(counter).position;

                if(invalid_nodes.contains(n)){
                    visited[n] = true;
                    counter++;
                    continue;
                }


                // If this adjacent node is the destination node,
                // then return true
                if (temp.contains(n)) {
                    goalPosition = NodeList_map.get(current_hex).children.get(counter);
                    return true;
                }

                // Else, continue to do BFS
                if (!visited[n])
                {
                    visited[n] = true;
                    queue.add(n);
                }
                counter++;
            }
        }
        return false;
    }





}
/**
 * 1) we store the index of the hex in position
      ---> we also store its coordinates in the map in the hexnode data structure
    2) compute the neighbours


    3) implement mouse running away from the board    //  almost done
    4) implement hex color change and invalid board   // DONE
            ---> update calculatenextmove so it doesnt move on invalid square DONE
    5) add background to intro and gameView           // easy to do
    6) music                                          // easy to do
    7) ads                                            //needs research // if it does well
    8) score system and turn system                   //easy to do
    9) Try Again                                      //not trivial
    10)game logic

 */
