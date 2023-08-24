package prototype.gameobjects.things;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import prototype.gameobjects.Entity2D;
import prototype.gameobjects.interfaces.Physics2D;
import prototype.gameobjects.things.LootDrop.partPrefix;
import prototype.levels.Tile;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.Animations.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.renderpanel;
import static prototype.system.Prototype.soundengine;
import prototype.utility.Constants;
import static prototype.utility.Constants.hitboxData.*;

/**
 *
 * @author Serbs
 */
public class Bullet implements Physics2D{

    private Rectangle2D bounds;
    private Area hitbox;

    private final BufferedImage[] currentAnim;
    private int animTicker, animPosition, animSpeed = (int) (0.05 * UPDATES_PER_SECOND);

    private int lifetime;
    private final float damage;
    private float speed;
    private final float size;
    private final double accuracySway;
    private final float accuracy;
    private int multihit;
    private final float knockback;

    private final float homing;
    private final int reverse;
    private final float crit;
    private final float odd;
    private final float acceleration;
    private final float maxAcceleration;

    private int homingBuffer;
    private int homingBufferValue;

    private double direction;
    private boolean active;
    private boolean friendly;

    private Point2D spawnpoint;

    public Bullet(float damage, float accuracy, float speed, float size, int multihit, float knockback, float homing, int lifetime, int reverse,
            float crit, float odd, float acceleration, boolean friendly, Point2D spawnpoint, double direction, partPrefix textureType) {
        this.accuracy = 1 - accuracy;
        this.speed = speed;
        this.size = size;
        this.multihit = multihit;
        this.knockback=knockback;
        this.homing = homing;
        this.reverse = reverse;
        this.crit = crit;
        this.odd = odd;
        this.acceleration = acceleration;
        maxAcceleration = TILE_SIZE * 1.25f;
        this.friendly = friendly;
        this.spawnpoint = spawnpoint;
        this.direction=direction;

        double critChance = Math.random();
        if (critChance < crit) {
            this.damage = damage * 1.75f;
            

        } else {
            this.damage = damage;
            
        }
        switch(textureType){
            case BASIC->{
                currentAnim=getAnimation(animation.BULLET_BASIC_TEMPLATE);
                hitbox=new Area(BULLET_BASIC_HITBOX);
            }
            case MARKSMAN->{
                currentAnim=getAnimation(animation.BULLET_MARKSMAN_TEMPLATE);
                hitbox=new Area(BULLET_MARKSMAN_HITBOX);
            }
            case RAPID->{
                currentAnim=getAnimation(animation.BULLET_RAPID_TEMPLATE);
                hitbox=new Area(BULLET_RAPID_HITBOX);
            }
            case HUGE -> {
                currentAnim = getAnimation(animation.BULLET_HUGE_TEMPLATE);
                hitbox = new Area(BULLET_HUGE_HITBOX);
            }
            case ODD -> {
                currentAnim = getAnimation(animation.BULLET_ODD_TEMPLATE);
                hitbox = new Area(BULLET_ODD_HITBOX);
            }
            case SPLATTER -> {
                currentAnim = getAnimation(animation.BULLET_SPLATTER_TEMPLATE);
                hitbox = new Area(BULLET_SPLATTER_HITBOX);
            }
            case SWIFT -> {
                currentAnim = getAnimation(animation.BULLET_SWIFT_TEMPLATE);
                hitbox = new Area(BULLET_SWIFT_HITBOX);
            }
            default->{
                currentAnim=getAnimation(animation.BULLET_BASIC_TEMPLATE);
                hitbox=new Area(BULLET_BASIC_HITBOX);
            }
        }
        
        homingBuffer = 0;
        homingBufferValue = (int) (4 / (homing / 2));
        this.lifetime = lifetime;
        if (this.reverse > 0) {
            this.direction=this.direction*-1;
        }
        double random = Math.random();
        if (random < 0.5) {
            accuracySway = (Math.random() * this.accuracy) * 1;
        } else {
            accuracySway = (Math.random() * this.accuracy) * -1;
        }
        this.direction+=accuracySway;
        
        
        
        if (this.reverse > 0) {
            bounds = new Rectangle2D.Double(renderpanel.getCurrentMouseLocation().x - (TILE_SIZE*this.size / 2), renderpanel.getCurrentMouseLocation().y - (TILE_SIZE*this.size / 2), TILE_SIZE*this.size, TILE_SIZE*this.size);
        } else {
            bounds = new Rectangle2D.Double(spawnpoint.getX() - (TILE_SIZE*this.size / 2), spawnpoint.getY() - (TILE_SIZE*this.size / 2), TILE_SIZE*this.size, TILE_SIZE*this.size);
        }
        active = true;
        //Scales hitbox with size variable
        AffineTransform scl = new AffineTransform();
        scl.scale(this.size, this.size);
        hitbox.transform(scl);
        
        //Moves hitbox from 0,0 to bounds;
        AffineTransform moveToBounds = new AffineTransform();
        moveToBounds.translate(bounds.getX(), bounds.getY());
        hitbox.transform(moveToBounds);
        
        //Rotates hitbox based on direction;
        AffineTransform rot = new AffineTransform();
        rot.rotate((this.direction + Math.toRadians(90)), bounds.getCenterX(), bounds.getCenterY());
        hitbox.transform(rot);
        
    }

    public Rectangle2D getBounds() {

        return bounds;
    }
    public Area getHitbox() {

        return hitbox;
    }
    
    public Point2D getCenterPoint(){
        return new Point2D.Float((float)bounds.getCenterX(), (float)bounds.getCenterY());
    }

    public Point2D getLocationIndex() {
        return new Point2D.Float((int) (bounds.getCenterX() / TILE_SIZE), (int) (bounds.getCenterY() / TILE_SIZE));
    }

    public void setActive(boolean act) {
        active = act;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public double getDirection() {
        return direction;
    }

    public float getDamage() {
        return damage;
    }

    public int getMultiHit() {
        return multihit;
    }

    public void setMultihit() {
        multihit--;
    }
    public float getKnockback(){
        return knockback;
    }

    public void update() {
        if (active) { 
            rotate();
            move();        
            checkCollision();
            if (lifetime <= 0) {
                active = false;
            }
            lifetime--;
            updateAnimation(); 
        }
        else{
            
        }
    }
    
    private void checkCollision(){
        if(friendly){
            HashSet<Tile> surroundingTiles = levelmanager.getMap().getCurrentTile(getLocationIndex()).getSurroundingTiles();
            HashSet<Entity2D> surroundingGameObjects = new HashSet<>();

            surroundingTiles.add(levelmanager.getMap().getCurrentTile(getLocationIndex()));
            for (var tile : surroundingTiles) {
                surroundingGameObjects.addAll(tile.getObjectsOnTile());
            }
            for (var object : surroundingGameObjects) {
                if(!object.isFriendly()){
                    if (collides(hitbox, object.getHitbox()) && object.isActive() && !object.isDead()) {
                        if (multihit == 1) {
                            active = false;
                            soundengine.playSFX(Constants.Audio.SFX.HIT);
                        } else if (multihit > 1) {
                            multihit--;
                        }
                        object.hurt(damage);
                        object.knockback(direction, knockback);
                    }
                }
                
            }
        }else{
            if (collides(hitbox, gameboard.getPlayer().getHitbox()) && gameboard.getPlayer().isActive() && !gameboard.getPlayer().isDead()) {
                if (multihit == 1) {
                    active = false;
                    soundengine.playSFX(Constants.Audio.SFX.HIT);
                } else if (multihit > 1) {
                    multihit--;
                }
                gameboard.getPlayer().hurt(damage);
                gameboard.getPlayer().knockback(direction, knockback);
            }
        }
        
        if (levelmanager.collidesWithWall(hitbox, getLocationIndex())) {
                active = false;
            }
    }
    
    private void rotate(){
        double previousDirection = direction;
        if (odd > 0) {
                
                double random = Math.random();
                if (random > 0.5) {
                    direction += 0.05 * odd;
                } else {
                    direction -= 0.05 * odd;
                }
                
            }
        if (homing != 0) {
                if (homingBuffer > 0) {
                    homingBuffer--;
                }
                if (homingBuffer == 0) {
                    double homingDeltax = renderpanel.getCurrentMouseLocation().x - bounds.getCenterX();
                    double homingDeltay = renderpanel.getCurrentMouseLocation().y - bounds.getCenterY();
                    direction = (Math.atan2(homingDeltay, homingDeltax) + accuracySway);
                    homingBuffer = homingBufferValue;
                }
            }
        
        if(odd>0||homing!=0){
            double hitboxDeltarotation = direction - previousDirection;
                AffineTransform rot = new AffineTransform();
                rot.rotate(hitboxDeltarotation, getCenterPoint().getX(), getCenterPoint().getY());
                hitbox.transform(rot);
        }
    }
    
    private void move(){
        if (acceleration > 0) {
                if (speed < maxAcceleration) {
                    speed = speed * (1 + acceleration);
                }
            }
        
            bounds.setRect(bounds.getX() + speed * Math.cos(direction), bounds.getY() + speed * Math.sin(direction), TILE_SIZE*size, TILE_SIZE*size);
            AffineTransform move = new AffineTransform();
            move.translate( speed * Math.cos(direction),  speed * Math.sin(direction));
            hitbox.transform(move);
    }

    private void updateAnimation() {
        if (animTicker >= animSpeed) {
            animPosition++;
            if (animPosition >= currentAnim.length) {
                animPosition = 0;

            }
            animTicker = 0;
        }
        animTicker++;
    }

    public Graphics2D render(Graphics2D g2d, AffineTransform normal) {
        g2d.rotate((direction + Math.toRadians(90)), (bounds.getCenterX()), (bounds.getCenterY()));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, levelmanager.getMap().getCurrentTile(getLocationIndex()).getLight()));

        g2d.translate(bounds.getX(), bounds.getY());
        g2d.drawImage(currentAnim[animPosition], 0, 0, (int) (bounds.getWidth()), (int) (bounds.getHeight()), null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.setTransform(normal);       
        if (GameLoop.isDebug()) {
            g2d.setColor(Color.magenta);
            g2d.draw(bounds);
            g2d.setColor(Color.cyan);
            g2d.draw(hitbox);
        }
        return g2d;
    }

}
