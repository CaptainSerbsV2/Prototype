package prototype.gameobjects.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import prototype.gameobjects.Entity2D;
import prototype.levels.Tile;
import prototype.gameobjects.things.Bullet;
import static prototype.system.Prototype.soundengine;
import static prototype.utility.Constants.Animations.*;
import static prototype.utility.Constants.Audio.*;
import static prototype.utility.Constants.System.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import prototype.gameobjects.things.Blood;
import static prototype.system.Prototype.renderpanel;
import static prototype.system.Prototype.soundengine;
import prototype.utility.Constants.LevelData.TileType;
import prototype.gameobjects.interfaces.Physics2D;
import prototype.gameobjects.things.LootDrop.partPrefix;
import prototype.gameobjects.things.LootDrop.partRarity;
import prototype.gameobjects.things.LootDrop.partType;
import prototype.lootengine.WeaponPart;

/**
 *
 * @author Serbs
 */
public class Player extends Entity2D implements Physics2D{

    @Override
    protected void attack() {
    }

    private Rectangle2D collisionDetector;

    private BufferedImage[] currentAnim, moveAnim, idleAnim;
    private int moveAnimTicker, actionAnimTicker, moveAnimPosition, actionAnimPosition, moveAnimSpeed = (int) (0.075 * UPDATES_PER_SECOND), actionAnimSpeed = (int) (0.15 * UPDATES_PER_SECOND);

    private boolean up, down, left, right, dash, iframe, pickup, action;
    private int buffer;

    private boolean pickupArea;

    private int dashTimer, dashCooldown, dashModifier;

    private int maxHP;
    private int regenSpeed;
    private int regenTimer;
    private int regenSpeedTimer;
    private double viewDistance;

    private int  xpRequired, xpAdder, xp;

   // private int hurtFlash = 0;

    private Weapon weapon;

    public Player(double x, double y, Polygon hitboxData, int level) {
        super(x,y,hitboxData,level);
        

        collisionDetector = new Rectangle2D.Double(bounds.getX() + 4, bounds.getY() + 4, 8, 8);

        idleAnim = getAnimation(animation.PLAYER_IDLE_TEMPLATE);
        moveAnim = getAnimation(animation.PLAYER_MOVE_TEMPLATE);

        up = false;
        down = false;
        left = false;
        right = false;
        dash = false;
        iframe = false;
        action = false;
        buffer = 0;

        pickup = false;
        pickupArea = false;


        dashTimer = 0;

        xp = 0;
        xpRequired = 100;
        xpAdder = 0;

        weapon = new Weapon(this,new WeaponPart(partType.CORE, partPrefix.BASIC, partRarity.COMMON, 1),new WeaponPart(partType.BARREL, partPrefix.BASIC, partRarity.COMMON, 1),
                new WeaponPart(partType.STOCK, partPrefix.BASIC, partRarity.COMMON, 1),new WeaponPart(partType.GRIP, partPrefix.BASIC, partRarity.COMMON, 1),
        new WeaponPart(partType.MAG, partPrefix.BASIC, partRarity.COMMON, 1));

        maxHP = 100;
        HP = maxHP;
        regenSpeed = (int) (0.25 * UPDATES_PER_SECOND);
        regenTimer = 0;
        regenSpeedTimer = 0;
        speed = (TILE_SIZE*4f/UPDATES_PER_SECOND);
        this.level=level;
        
        dashCooldown = (int) (5 * UPDATES_PER_SECOND);
        dashModifier = 0;
        viewDistance = TILE_SIZE * 60;
        friendly=true;
    }

    public boolean isDash() {
        return dash;
    }

    public int getMaxHP() {
        return maxHP;
    }

    private double getCooldown() {
        double cd = (double) Math.round(((double) (dashTimer) / UPDATES_PER_SECOND) * 100d) / 100d;
        return cd;
    }

    public void Ability(boolean d) {
        dash = d;
    }

    public void pickUp(boolean p) {
        pickup = p;
    }

    public void setMovementBoolean(int leftUpRightDown1234, boolean set) {
        switch (leftUpRightDown1234) {
            case 1 ->
                left = set;
            case 2 ->
                up = set;
            case 3 ->
                right = set;
            case 4 ->
                down = set;
        }
        if (state == State.IDLE) {
            state = State.MOVING;
        }
    }

    public void setAction(boolean a) {
        action = a;
    }

    public void resetInputActions() {
        pickup = false;
        dash = false;
        up = false;
        down = false;
        left = false;
        right = false;
        weapon.Fire(false);
    }

    public double getRotation() {
        return rotation;
    }

    @Override
    public void hurt(float damage) {
        if (!iframe) {
            HP -= damage;
            soundengine.playSFX(SFX.HURT);
            gameboard.getBloods().add(new Blood(gameboard.getPlayer().getCenterPoint(), 1.5f));
            regenTimer = (int) (10 * UPDATES_PER_SECOND);
        }
    }

    public void setXP(int xpGiven) {
        xp = xp + xpGiven;
        if (xp >= xpRequired) {
            level++;
            maxHP++;
            xpAdder = xpAdder + 10;
            xpRequired = xpRequired + 100 + xpAdder;

        }
    }

    public void setSpeed(float mod) {
        speed = (TILE_SIZE*4f/UPDATES_PER_SECOND) * mod;
    }

    public void setDashCooldown(double mod) {
        dashCooldown = (int) ((5 * UPDATES_PER_SECOND) * mod);
    }

    public void setDashModifier(int mod) {
        dashModifier = mod;
    }

    public int getXP() {
        return xp;
    }

    public double getXpRequired() {
        return xpRequired;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public void update() {
        checkDeath();
        mapDirection();
        rotate();
        move();
        weapon.fireBullet(new Point2D.Double(renderpanel.getCurrentMouseLocation().x, renderpanel.getCurrentMouseLocation().y),rotation);
        useDash();
        if(!iframe){
            checkBulletHits(location,hitbox);
        }
        
        pickUpLootBox();
        manageHP();
        setAnimation();
        updateAnimation();
    }

    @Override
    public Graphics2D render(Graphics2D g2d, AffineTransform normal) {
        levelmanager.rayCaster(getCenterPoint(), viewDistance, this.hashCode());
        
        g2d.rotate(rotation, bounds.getCenterX(), bounds.getCenterY());
        g2d.drawImage(currentAnim[moveAnimPosition], position, null);
        g2d.setTransform(normal);

        g2d.setColor(Color.black);
        g2d.setFont(new Font("Impact", Font.ITALIC, (int) (TILE_SIZE * 0.75)));
        if (dashTimer > 0) {
            g2d.drawString(getCooldown() + "s", (int) bounds.getX(), (int) (bounds.getMaxY() + TILE_SIZE * 1.5));
        }
        if (pickupArea) {
            g2d.drawString("Pick up ( E )", (int) (bounds.getX() - TILE_SIZE * 0.5), (int) (bounds.getMaxY() + TILE_SIZE * 1.75));
        }

        g2d.drawString(weapon.getMagString(), (int) (bounds.getX() - TILE_SIZE * 2.5), (int) bounds.getCenterY());
        if (weapon.getReload()) {
            g2d.drawString("" + weapon.getReloadCooldown(), (int) (bounds.getX() - TILE_SIZE * 2.5), (int) (bounds.getCenterY() + TILE_SIZE * 0.75));
        }
        
        weapon.render(g2d, normal,position,location, bounds.getCenterX(),bounds.getCenterY(), rotation);

        //Debug
        if (GameLoop.isDebug()) {
                    g2d.setColor(Color.MAGENTA);
        g2d.draw(bounds);
        g2d.setColor(Color.orange);
        g2d.draw(collisionDetector);
        g2d.setColor(Color.cyan);
        g2d.draw(hitbox);
        g2d.drawString("Location: " + getLocationIndex().getX() + ", " + getLocationIndex().getY(), (int) (bounds.getX()), (int) (bounds.getMaxY()));
        }

        return g2d;
    }

    @Override
    public void setAnimation() {
        switch(state){
            case IDLE ->{
                currentAnim = idleAnim;
            }
            case MOVING ->{
                currentAnim = moveAnim;
            }
        }
//        if (action) {
//            if (weapon.getReload() && !iframe) {
//                actionAnimSpeed = (int) (weapon.getReloadSpeed() / 12);
//                currentActionAnim = getAnimation(animation.PLAYER_RELOAD);
//            } else if (weapon.getFire() && !iframe) {
//                actionAnimSpeed = (int) (weapon.getFireRate() / 5);
//                currentActionAnim = getAnimation(animation.PLAYER_SHOOT);
//            } else if (iframe) {
//                actionAnimSpeed = (int) (0.5 * UPDATES_PER_SECOND / 12);
//                currentActionAnim = getAnimation(animation.PLAYER_DASH);
//            } else {
//                actionAnimPosition = 0;
//                currentActionAnim = getAnimation(animation.PLAYER_IDLE);
//                actionAnimSpeed = (int) (UPDATES_PER_SECOND / 24);
//            }
//        }
    }

    @Override
    protected void updateAnimation() {
        if (moveAnimTicker >= moveAnimSpeed) {
            moveAnimPosition++;
            if (moveAnimPosition >= currentAnim.length) {
                moveAnimPosition = 0;
                if (state == State.DEAD) {
                    active = false;
                }

            }
            moveAnimTicker = 0;
        }
        moveAnimTicker++;
//        if (action) {
//            if (actionAnimTicker >= actionAnimSpeed) {
//                actionAnimPosition++;
//                if (actionAnimPosition >= currentActionAnim.length) {
//                    actionAnimPosition = 0;
//                    if (state == State.DEAD) {
//                        active = false;
//                    }
//                    action = false;
//                }
//                actionAnimTicker = 0;
//            }
//            actionAnimTicker++;
//        }

    }

    @Override
    protected void rotate() {
        double previousRotation = rotation;
        double deltax = renderpanel.getCurrentMouseLocation().getX() - bounds.getCenterX();
        double deltay = renderpanel.getCurrentMouseLocation().getY() - bounds.getCenterY();
        rotation = Math.atan2(deltay, deltax)+Math.toRadians(90);
        double hitboxDeltarotation = rotation - previousRotation;
        AffineTransform rot = new AffineTransform();
        rot.rotate(hitboxDeltarotation, getCenterPoint().getX(), getCenterPoint().getY());
        hitbox.transform(rot);
    }

    @Override
    protected void move() {
        // Player movement;
        location.removeObjectFromTile(this);
        location = levelmanager.getMap().getCurrentTile(getLocationIndex());
        location.setObjectOnTile(this);
        if(location.getType()==TileType.CRACKED_WALL ||
                location.getType()==TileType.CRACKED_WALL_BESIDE_WALL){
            slow=0.5f;
        }else{
            slow=1;
        }
        switch (state) {
            case MOVING, IDLE -> {
                if(state==State.MOVING){
                    velocity= accelerate(velocity);
                }else{
                   velocity= decelerate(velocity);
                }
                knockbackForce=forceReduction(knockbackForce);

                //checks if new position is available
                double premoveX = getMovementX(true,speed,slow,Math.toRadians(direction - 90),velocity, knockbackForce, knockbackDirection);
                double premoveY = getMovementY(true,speed,slow,Math.toRadians(direction - 90),velocity, knockbackForce, knockbackDirection);
                Rectangle2D newPositionX = new Rectangle2D.Double(collisionDetector.getX() + premoveX,
                        collisionDetector.getY(), collisionDetector.getWidth(), collisionDetector.getHeight());
                Rectangle2D newPositionY = new Rectangle2D.Double(collisionDetector.getX(),
                        collisionDetector.getY() + premoveY, collisionDetector.getWidth(), collisionDetector.getHeight());
                
                //sets blocked states if new position collides with wall
                boolean blockedX = levelmanager.collidesWithWall(newPositionX, getLocationIndex(), false);
                boolean blockedY = levelmanager.collidesWithWall(newPositionY, getLocationIndex(),false);
                
                //Moves based on blocked;
                double moveX=getMovementX(!blockedX,speed,slow,Math.toRadians(direction - 90),velocity, knockbackForce, knockbackDirection);
                double moveY=getMovementY(!blockedY,speed,slow,Math.toRadians(direction - 90),velocity, knockbackForce, knockbackDirection);
                position.translate(moveX, moveY);
                bounds.setRect(position.getTranslateX(), position.getTranslateY(), bounds.getWidth(), bounds.getHeight());
                collisionDetector.setRect(position.getTranslateX()+4,position.getTranslateY()+4, collisionDetector.getWidth(), collisionDetector.getHeight());
                
                AffineTransform move = new AffineTransform();
                move.translate(moveX, moveY);
                hitbox.transform(move);

            }


        }

    }

    private void manageHP() {
        if (regenTimer <= 0) {
            if (regenSpeedTimer <= 0 && HP < maxHP) {
                HP += 1 + (level / 10);
                regenSpeedTimer = regenSpeed;
            }
        }
        if (regenTimer > 0) {
            regenTimer--;
        }
        if (regenSpeedTimer > 0) {
            regenSpeedTimer--;
        }

    }

    //Handles dash logic
    private void useDash() {

        //Have to input any direction for dash.
        if (dash && (left || right || up || down)) {
            if (dashTimer == 0) {
                state = State.MOVING;
                actionAnimPosition = 0;
                actionAnimTicker = 0;
                velocity = velocity*5;
                iframe = true;
                action = true;
                dashTimer = dashCooldown;
                switch (dashModifier) {
                    case 0 -> {
                        break;
                    }
                    case 1, 2, 3, 4, 5 -> {
                        Point2D spawn = new Point2D.Double(gameboard.getPlayer().getBounds().getCenterX(), gameboard.getPlayer().getBounds().getCenterY());
                        Point2D target = new Point2D.Double(renderpanel.getCurrentMouseLocation().x, renderpanel.getCurrentMouseLocation().y);
                        double dx = target.getX() - spawn.getX();
                        double dy = target.getY() - spawn.getY();
                        double bulletDirection = Math.atan2(dy, dx);
                        for (int i = 0; i < (10 * dashModifier); i++) {
                            gameboard.getBullets().add(new Bullet(level, -3, (TILE_SIZE * 0.04125f), 1, 1,
                                    0, 0, (int) (3 * UPDATES_PER_SECOND), 0, 1, 0, 0, true, spawn, bulletDirection, weapon.getBarrel().getPrefix()));
                        }
                    }



                }
                soundengine.playSFX(SFX.DASH);
            }
        }
        if (dashCooldown - dashTimer >= 0.5 * UPDATES_PER_SECOND) {
            iframe = false;
        }
        if (dashTimer > 0) {
            dashTimer--;
        }

    }

    private void pickUpLootBox() {
        for(var lootDrop:gameboard.getLootDrops()){
            if (bounds.intersects(lootDrop.getTriggerArea())) {
                pickupArea = true;
                if (pickup) {
                    lootDrop.pickUp();
                    pickupArea = false;
                    break;
                }
            } else {
                pickupArea = false;
            }
        }
    }

    private void checkBulletHits(Tile location, Area hitbox) {
//        HashSet<Tile> surroundingTiles=location.getSurroundingTiles();
//        HashSet<Bullet> surroundingBullets=new HashSet<>();
//        
//        surroundingTiles.add(location);
//        for(var tile : surroundingTiles){
//            surroundingBullets.addAll(tile.getEnemyBulletsOnTile());
//        }
//        for(var bullet : surroundingBullets){
//            if(collides(hitbox,bullet.getHitbox())){
//                bullet.setActive(false);
//                hurt(10);
//                knockback(bullet.getDirection(),bullet.getKnockback());
//            }
//        }
        
    }

    private void checkDeath() {

        if (HP <= 0 && state != State.DEAD) {
            moveAnimSpeed = (int) (0.1 * UPDATES_PER_SECOND);
            actionAnimSpeed = (int) (0.1 * UPDATES_PER_SECOND);
//            currentActionAnim = getAnimation(animation.PLAYER_DEATH);
            actionAnimPosition = 0;
            action = true;
  //          currentMoveAnim = getAnimation(animation.PLAYER_DEATH);
            moveAnimPosition = 0;
            state = State.DEAD;
        }
    }

    private void mapDirection() {
        if (state != State.DEAD) {
            if (buffer > 0) {
                buffer--;
            }
            if (buffer == 0) {
                if (!up && !left && !right && !down) {
                    direction = direction;
                    state = State.IDLE;
                } else if (up && left && right && down) {
                    direction = direction;
                    buffer = 5;
                } else if (up && left && right && !down) {
                    direction = 0;
                    buffer = 5;
                } else if (up && left && !right && down) {
                    direction = 270;
                    buffer = 5;
                } else if (up && !left && right && down) {
                    direction = 90;
                    buffer = 5;
                } else if (!up && left && right && !down) {
                    direction = direction;
                    buffer = 5;
                } else if (up && !left && !right && down) {
                    direction = direction;
                    buffer = 5;
                } else if (up && !left && !right && !down) {
                    direction = 0;
                } else if (up && left && !right && !down) {
                    direction = 315;
                    buffer = 5;
                } else if (up && !left && right && !down) {
                    direction = 45;
                    buffer = 5;
                } else if (!up && !left && right && down) {
                    direction = 135;
                    buffer = 5;
                } else if (!up && left && !right && down) {
                    direction = 225;
                    buffer = 5;
                } else if (!up && left && right && down) {
                    direction = 180;
                    buffer = 5;
                } else if (!up && !left && right && !down) {
                    direction = 90;
                } else if (!up && !left && !right && down) {
                    direction = 180;
                } else if (!up && left && !right && !down) {
                    direction = 270;
                }

            }
        }

    }
}
