/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package astar.model;

import astar.plugin.IModel;
import astar.util.Helper;
import astar.util.Node;

/**
 * This class is used to implement the Stealthy Model
 * @author kavyareddy
 */
public class Stealthy implements IModel{
    
    protected char[][] tileMap = null;

    @Override
    public void init(char[][] tileMap) {
        this.tileMap=tileMap;
    }
    
    /**
     * This method checks for walls and discounts the heuristic by 10% if
     * it tracks a wall 
     * @param heuristic The Heuristic value
     * @param curNode Current Node
     * @param adjNode Adjacent Node
     * @return 
     */
    @Override
    public double shape(double heuristic, Node curNode, Node adjNode) {
        boolean isWall = Helper.tracksWall(tileMap, adjNode);
        
        if(isWall) {
            heuristic = heuristic * 0.9;
        }
        return heuristic;
    }

    @Override
    public void complete(Node curNode) {
        
    }
    
}
