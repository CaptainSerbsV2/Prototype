/*
 * Copyright (C) 2023 imfai
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package prototype.gameobjects.interfaces;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import prototype.gameobjects.Entity2D;
import prototype.levels.Tile;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;

/**
 *
 * @author Sere Kuusitaival
 */
public interface Enemy2D {
    
    default Point2D getSeparation(Tile location, Rectangle2D bounds){
        float separationForce=0;
        double separationDirection=0;
        HashSet<Tile> surroundingTiles=location.getSurroundingTiles();
        HashSet<Entity2D> adjacentEnemies=new HashSet<>();
        Point2D separation=new Point2D.Double();
        surroundingTiles.add(location);
        for(var tile : surroundingTiles){
            adjacentEnemies.addAll(tile.getObjectsOnTile());
        }
        
        if(adjacentEnemies.isEmpty()){
            separationForce=0;
        }else{
            for(var enemy : adjacentEnemies){
                if (bounds.intersects(enemy.getBounds())&&enemy!=this){
                    double dx = bounds.getCenterX() - enemy.getBounds().getCenterX();
                    double dy = bounds.getCenterY() - enemy.getBounds().getCenterY();
                    
                    separationDirection = Math.atan2(dy, dx);
                    separationForce=0.05f;
                    separation.setLocation(separation.getX()+separationForce * Math.cos(separationDirection),separation.getY()+ separationForce * Math.sin(separationDirection));
                   
                }
            }
        }
        return separation;
    }
    
    default double getMovementDirection(Tile location, Rectangle2D bounds, boolean los){
        double dx = gameboard.getPlayer().getCenterPoint().getX() - bounds.getCenterX();
        double dy = gameboard.getPlayer().getCenterPoint().getY() - bounds.getCenterY();
//        if (Math.atan2(dy, dx) - location.getDirection() < 3
//                && Math.atan2(dy, dx) - location.getDirection() > -3 && los) {
//            return ((location.getDirection() + Math.atan2(dy, dx)) / 2);
//        } else {
//            return location.getDirection();
//        }
        return location.getDirection();
    }
    
    default double getVariableSpeedModifier(){
        return (Math.random() * 0.4) + 0.8;
    }
    
}
