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
package prototype.gameobjects;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import prototype.levels.Tile;
import static prototype.system.Prototype.levelmanager;
import static prototype.utility.Constants.System.TILE_SIZE;

/**
 *
 * @author Sere Kuusitaival
 */
public abstract class Entity2D {
    
    public enum State {
        IDLE,
        MOVING,
        DEAD,
        ROAMING,
        CHASING,
        ATTACK
    }
    
    protected State state;

    protected Rectangle2D bounds;
    protected Area hitbox;
    
    protected AffineTransform position = new AffineTransform();
    
    protected Tile location;
    
    protected boolean active,friendly;
    
    protected double rotation, direction, knockbackDirection;
    protected float velocity, knockbackForce, speed, slow, HP;
    
    protected int level;
    
    public Entity2D(double x, double y, Polygon hitboxData, int level){
        
        position.translate(x, y);
        bounds = new Rectangle2D.Double(x, y, TILE_SIZE, TILE_SIZE);
        hitbox=new Area(hitboxData);
        AffineTransform moveToBounds = new AffineTransform();
        moveToBounds.translate(bounds.getX(), bounds.getY());
        hitbox.transform(position);
        
        active = true;
        
        rotation = 0;
        direction = 0;
        velocity = 0;
        slow = 1;

        this.level=level;
        location = levelmanager.getMap().getTiles()[(int) (x / 16)][(int) (x / 16)];
    }
    
    public Rectangle2D getBounds() {
        return bounds;
    }
    public Area getHitbox() {
        return hitbox;
    }

    public Point2D getCenterPoint() {
        return new Point2D.Float((float) bounds.getCenterX(), (float) bounds.getCenterY());
    }

    public Point2D getLocationIndex() {
        return new Point2D.Float((int) (bounds.getCenterX() / TILE_SIZE), (int) (bounds.getCenterY() / TILE_SIZE));
    }
    
    public Tile getLocation(){
        return location;
    }
    
    public float getHP() {
        return HP;
    }
    
    public int getLevel() {
        return level;
    }
    public double getRotation(){
        return rotation;
    }

    public boolean isActive() {
        return active;
    }
    
    public boolean isFriendly() {
        return friendly;
    }
    
    public boolean isDead(){
        return state==State.DEAD;
                
    }
    
    public State getState() {
        return state;
    }
    
    public void knockback(double dir, float force) {
        knockbackDirection = dir;
        knockbackForce = force;
    }
    
    public abstract void hurt(float damage);
    
    protected abstract void attack();
    
    protected abstract void rotate();
    
    protected abstract void move();
    
    public abstract void update();
    
    public abstract Graphics2D render(Graphics2D g2d, AffineTransform normal);
    
    public abstract void setAnimation();
    
    protected abstract void updateAnimation();
    
}
