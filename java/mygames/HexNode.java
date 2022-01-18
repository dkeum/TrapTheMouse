package mygames;

import java.util.LinkedList;
import java.util.List;


public class HexNode {
     public int position;
     public float hex_x,hex_y;

     public List<HexNode> children = new LinkedList<>();


    public HexNode(){

    }
     public HexNode (int data, float xx, float yy){
         position = data;
         hex_x = xx;
         hex_y = yy;
      }

     public HexNode (int data,List<HexNode> child){
         position = data;
         children = child;
      }
    public void insert (HexNode child){
        children.add(child);
    }



}
