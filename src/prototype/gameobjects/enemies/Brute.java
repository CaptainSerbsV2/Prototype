package prototype.gameobjects.enemies;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import prototype.gameobjects.Entity2D;
import prototype.gameobjects.interfaces.Enemy2D;
import prototype.gameobjects.interfaces.Physics2D;
import prototype.gameobjects.things.Blood;
import static prototype.utility.Constants.Audio.*;
import static prototype.utility.Constants.System.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.soundengine;
import prototype.utility.Constants;
import static prototype.utility.Constants.Animations.*;
import static prototype.utility.Constants.hitboxData.*;

/**
 *
 * @author Serbs
 */
public class Brute extends Entity2D implements Enemy2D, Physics2D{

    private BufferedImage[] currentAnim, lastAnim, idleAnim, moveAnim, deathAnim, attackAnim;
    private int animTicker, animPosition, animSpeed, attackSpeed, attackTimer, attackRange, minDistanceBetweenPlayer;
    
    private SFX deathSFX;
    
    
     private Rectangle2D collisionDetector;
    
    private float detectionRange,separationForce;
    
    private boolean los;
    
    private double targetRotation;
    
    private Area attackHitbox=new Area(BRUTE_ATTACK_HITBOX);

    public Brute(double x, double y, Polygon hitboxData, int level) {
        super(x,y,hitboxData,level);
        
        collisionDetector = new Rectangle2D.Double(bounds.getX() + 4, bounds.getY() + 4, 8, 8);
        currentAnim = getAnimation(animation.PLAYER_IDLE_TEMPLATE);
        speed = (float) ((TILE_SIZE * 0.02f) * getVariableSpeedModifier());
        HP = (int) (4 * Math.pow(1.15, gameboard.getDifficulty() - 1));
        attackTimer=0;
        attackSpeed = (int)(0.5 * UPDATES_PER_SECOND);
        attackRange= TILE_SIZE*2;
        minDistanceBetweenPlayer=0;
        detectionRange = TILE_SIZE*30;
        
        AffineTransform moveToBounds = new AffineTransform();
        moveToBounds.translate(bounds.getX(), bounds.getY());
        attackHitbox.transform(moveToBounds);
        
        AffineTransform rot = new AffineTransform();
        rot.rotate(Math.toRadians(90), bounds.getCenterX(), bounds.getCenterY());
        attackHitbox.transform(rot);
        hitbox.transform(rot);
        
        idleAnim=null;
        moveAnim=getAnimation(animation.BRUTE_MOVE);
        deathAnim=getAnimation(animation.BRUTE_MOVE);
        attackAnim=getAnimation(animation.BRUTE_ATTACK);
        animSpeed = (int) (0.1 * UPDATES_PER_SECOND);
        deathSFX = SFX.BRUTE_DEATH;
        state=State.ROAMING;
        friendly=false;
    }

    @Override
    public void update() {
        rotate();
        move();
        attack();
        setAnimation();
        updateAnimation();
        
    }
    

    @Override
    public Graphics2D render(Graphics2D g2d, AffineTransform normal) {
            g2d.rotate((rotation+ Math.toRadians(90)), bounds.getCenterX(), bounds.getCenterY());
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, levelmanager.getMap().getCurrentTile(getLocationIndex()).getLight()));
            g2d.drawImage(currentAnim[animPosition], position, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.setTransform(normal);
        if(GameLoop.isDebug()){
            if(!los && state==State.ROAMING){
                g2d.setColor(Color.red);
            }else if(!los && state == State.CHASING){
                g2d.setColor(Color.orange);
            }else{
                g2d.setColor(Color.green);
            }
           // g2d.draw(losLine);
            g2d.setColor(Color.magenta);
            g2d.draw(bounds);
            g2d.setColor(Color.orange);
            g2d.draw(collisionDetector);
            g2d.setColor(Color.cyan);
            g2d.draw(hitbox);
            g2d.setColor(Color.red);
            g2d.draw(attackHitbox);
            
        }
        
        return g2d;
    }
    
    @Override
    protected void rotate() {
        double previousRotation = rotation;
        double dx = gameboard.getPlayer().getCenterPoint().getX() - bounds.getCenterX();
        double dy = gameboard.getPlayer().getCenterPoint().getY() - bounds.getCenterY();
        
        if(los){
            targetRotation = Math.atan2(dy, dx);
        }else{
            targetRotation=direction;
        }
        if(Math.atan2(dy, dx)-rotation<1.5
                                && Math.atan2(dy, dx)-rotation>-1.5&&los){
            if(rotation>targetRotation){
                rotation-=0.01;
            }else if(rotation<targetRotation){
                rotation+=0.01;
            }
        }else{
            rotation=targetRotation;
        }
        
        double hitboxDeltarotation = rotation - previousRotation;
        AffineTransform rot = new AffineTransform();
        rot.rotate(hitboxDeltarotation, getCenterPoint().getX(), getCenterPoint().getY());
        hitbox.transform(rot);
        attackHitbox.transform(rot);
        
        
    }

    @Override
    public void move() {
        location.removeObjectFromTile(this);
        location=levelmanager.getMap().getCurrentTile(getLocationIndex());
        location.setObjectOnTile(this);
        los=levelmanager.checkLOS(bounds, gameboard.getPlayer().getBounds(), detectionRange);
        direction=getMovementDirection(location, bounds, los);
        
        
        if(levelmanager.getMap().getCurrentTile(getLocationIndex()).getType()==Constants.LevelData.TileType.CRACKED_WALL ||
                levelmanager.getMap().getCurrentTile(getLocationIndex()).getType()==Constants.LevelData.TileType.CRACKED_WALL_BESIDE_WALL){
            slow=0.5f;
        }else if(getLocationIndex().distance(gameboard.getPlayer().getLocationIndex())<minDistanceBetweenPlayer&&los){
            slow=0;
        }else{
            slow=1;
        }
        knockbackForce = forceReduction(knockbackForce);
        separationForce = forceReduction(separationForce);
        
        switch(state){

            case ROAMING, CHASING, DEAD ->{
                
                Point2D preSeparation = getSeparation(levelmanager.getMap().getCurrentTile(getLocationIndex()),bounds);
                double premoveX = getMovementX(true,speed,slow,direction,1, knockbackForce, knockbackDirection)+preSeparation.getX();
                double premoveY = getMovementY(true,speed,slow,direction,1, knockbackForce, knockbackDirection)+preSeparation.getY();
                Rectangle2D newPositionX = new Rectangle2D.Double(collisionDetector.getX() + premoveX,
                collisionDetector.getY(), collisionDetector.getWidth(), collisionDetector.getHeight());
                Rectangle2D newPositionY = new Rectangle2D.Double(collisionDetector.getX(),
                        collisionDetector.getY() + premoveY, collisionDetector.getWidth(), collisionDetector.getHeight());
                
                boolean blockedX = levelmanager.collidesWithWall(newPositionX, getLocationIndex(),true);
                boolean blockedY = levelmanager.collidesWithWall(newPositionY, getLocationIndex(),true);
                
                double separationX=0;
                if(!blockedX){
                    separationX=preSeparation.getX();
                }
                double separationY=0;
                if(!blockedY){
                    separationY=preSeparation.getY();
                }
                double moveX = getMovementX(!blockedX,speed,slow,direction,1, knockbackForce, knockbackDirection)+separationX;
                double moveY = getMovementY(!blockedY,speed,slow,direction,1, knockbackForce, knockbackDirection)+separationY;
                position.translate(moveX, moveY);
                bounds.setRect(position.getTranslateX(), position.getTranslateY(), bounds.getWidth(), bounds.getHeight());
                collisionDetector.setRect(position.getTranslateX()+4,position.getTranslateY()+4, collisionDetector.getWidth(), collisionDetector.getHeight());
                
                AffineTransform move = new AffineTransform();
                move.translate(moveX, moveY);
                hitbox.transform(move);
                attackHitbox.transform(move);
            }
        }
    }
        
    @Override
    protected void attack(){
        if(state != State.DEAD){
            if(getCenterPoint().distance(gameboard.getPlayer().getCenterPoint())<attackRange && los){
                if(attackTimer==0){
                    if(collides(attackHitbox, gameboard.getPlayer().getHitbox())){
                        gameboard.getPlayer().hurt(10);
                        gameboard.getPlayer().knockback(direction, 0.8f);
                    }
                    
                    attackTimer = attackSpeed;
                    animPosition=0;
                    //state=State.ATTACK;
                }
                if (attackTimer > 0) {
                    attackTimer--;
                }
            }else{
                attackTimer = attackSpeed;
            }
        }
        
    }

    @Override
    public void hurt(float damage) {
        HP = HP - damage;
                gameboard.getBloods().add(new Blood(getCenterPoint(), 1f));
                if (HP <= 0) {
                    currentAnim = deathAnim;
                    soundengine.playSFX(deathSFX);
                    animPosition = 0;
                    state=State.DEAD;
                    gameboard.getBloods().add(new Blood(getCenterPoint(), 1.5f));
                    gameboard.lootDropSpawner(getCenterPoint());
                }
    }

    @Override
    public void setAnimation() {
//         if(state==State.IDLE){
//            switch(state){
//
//                case IDLE -> currentAnim=idleAnim;
//                case ROAMING, CHASING -> currentAnim=moveAnim;
//                case DEAD -> currentAnim=deathAnim;
//            }
//        }else{
//            switch(state){
//                case ATTACK->currentAnim=attackAnim;
//            }
//        }
    }

    @Override
    protected void updateAnimation() {
        if(currentAnim!=lastAnim){
            animPosition = 0;
        }
        if (animTicker >= animSpeed) {
            animPosition++;
            if (animPosition >= currentAnim.length) {
                animPosition = 0;
                if(state==State.ATTACK){
                    state=State.IDLE;
                }
                if (state==State.DEAD) {
                    
                    active = false;
                }
            }
            animTicker = 0;
        }
        animTicker++;
        lastAnim=currentAnim;
    }
}
