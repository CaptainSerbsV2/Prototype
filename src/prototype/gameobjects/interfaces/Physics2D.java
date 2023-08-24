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

import java.awt.Shape;
import java.awt.geom.Area;

/**
 *
 * @author Sere Kuusitaival
 */
public interface Physics2D {
    
    default double getMovementX(boolean preCondition, float speed, float slow, double direction, float velocity, float knockbackForce, double knockbackDirection) {
        if (preCondition) {
            return (speed *slow* Math.cos(direction) * velocity) + knockbackForce * Math.cos(knockbackDirection);
        } else {
            return 0;
        }
    }
    
    default double getMovementY(boolean preCondition, float speed, float slow, double direction, float velocity, float knockbackForce, double knockbackDirection) {
        if (preCondition) {
            return (speed *slow* Math.sin(direction) * velocity) + knockbackForce * Math.sin(knockbackDirection);
        } else {
            return 0;
        }
    }
    
    default float forceReduction(float force){
        return force*0.99f;
    }
    
    default float accelerate(float velocity){
        if(velocity<=1){
            return (velocity+0.01f)*1.01f;
        }else{
            return decelerate(velocity);
        }
    }
    
    default float decelerate(float velocity){
        if(velocity>0){
            return velocity*0.99f;
        }else{
            return velocity;
        }
    }
    
    default boolean collides(Shape hitboxA, Shape hitboxB){
            Area intersection= new Area(hitboxA);
            intersection.intersect(new Area(hitboxB));
            return !intersection.isEmpty();
        }
    
}
