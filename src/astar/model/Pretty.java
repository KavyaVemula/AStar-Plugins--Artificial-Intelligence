/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.model;

import astar.plugin.IModel;
import astar.util.Node;
import astar.util.Helper;

/**
 *
 * @author kavyareddy
 */
public class Pretty implements IModel{
    int penalty;
    protected char[][] tileMap = null;
    
    @Override
    public void init(char[][] tileMap) {
        this.tileMap=tileMap;
    }
    
    /**
     * This method is invoked for each neighboring node
     * It checks for walls and zig-zags and returns the
     * heuristic based on penalties
     * @param heuristic Heuristic value
     * @param curNode Current Node
     * @param adjNode Neighboring node
     * @return Shaped heuristic value
     */
    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) { 
        
        
        //checks if the adjacent node lies along the wall
        boolean isWall = Helper.tracksWall(tileMap, adjNode); 
        
        
        //checks if the nodes are zagging
        boolean isZagging = false;        
        if(curNode.getParent() != null) {
            Node parent = curNode.getParent();
            int dRow1 = adjNode.getRow() - curNode.getRow();
            int dRow2 = curNode.getRow() - parent.getRow();
            int dCol1 = adjNode.getCol() - curNode.getCol();
            int dCol2 = curNode.getCol() - parent.getCol();            
            isZagging = (dRow1!=dRow2) || (dCol1!=dCol2);
        }
        
        if(isZagging == false) {
            adjNode.setInertia(curNode.getInertia() + 1);            
        }
        
        if(adjNode.getInertia()>=8) {
            return heuristic;
        }
        
        // adds respective penalties to the heuristic and returns the total
        else {
            if(isZagging && isWall) {
              penalty = 13;
            }
            if(!isZagging && isWall) {
                penalty = 10;
            }
            if(isZagging && !isWall) {
                penalty = 2;
            }
            if(!isZagging && !isWall) {
                penalty = 0;
            }
            heuristic = heuristic + penalty;
            return heuristic;
        }
    }
    
    /**
     * This method is used to fix the Bridges in the path
     * after the Goal node is reached
     * @param child The child node
     */
    public void fixBridges(Node child) {        
        Node grand = child.getParent();                
        grand.setCol(child.getCol() );
    }
    
    /**
     * This method is used to fix the Roof problem in path
     * after the Goal node is reached
     * @param child Child Node 
     */
    public void fixRoofs(Node child) {
        Node parent = child.getParent();
        Node grand = parent.getParent();
        grand.setRow(child.getRow());
        parent.setRow(child.getRow());
    }

    @Override
    public void complete(Node curNode) {
        Node parent = curNode.getParent();
        
        // int count = 1; 
        
        // iterates through all the nodes in the path
        while(parent.getParent()!=null) {
            Node grand = parent.getParent();
            if(grand == null)
                return;
            Node child = parent.getChild();
            // Node greatparent = grand.getParent();
            
            boolean checkParentCol = parent.getCol() == grand.getCol();
            boolean checkChildCol = child.getCol() == grand.getCol();
            
            
            /** For Debugging purpose*/
            // System.out.println("parent" +parent.getCol());
            // System.out.println("grand" +grand.getCol());
            // System.out.println("child" +child.getCol());
            
            
            if(checkParentCol == false && checkChildCol == true) {
                fixBridges(child);
            }
            
            boolean checkParentRow = parent.getRow() == child.getRow();
            boolean checkGrandRow = child.getRow() == grand.getRow();
            
            
            // boolean d3 = child.getRow() == greatparent.getRow();
            // boolean d4 = parent.getRow() == grand.getRow();
            
            
            /** For Debugging purpose*/
            // System.out.println("parent" +parent.getRow());
            // System.out.println("grand" +grand.getRow());
            // System.out.println("child" +child.getRow());
            // System.out.println("great" +greatparent.getRow());
            
            
            if(checkParentRow == false && checkParentCol == false && checkGrandRow == true) {
                fixRoofs(child);
                // System.out.println("inside if" +count);
            }
       
            parent=grand;
            // ++count;
            
        }
   
    }
    
}
