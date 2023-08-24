package prototype.gameobjects.player;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import prototype.gameobjects.Entity2D;
import prototype.gameobjects.things.Bullet;
import static prototype.system.Prototype.soundengine;
import prototype.gameobjects.things.LootDrop.*;
import prototype.levels.Tile;
import static prototype.utility.Constants.Audio.*;
import prototype.lootengine.WeaponPart;
import static prototype.utility.Constants.System.*;
import prototype.utility.Constants.LevelData.TileType;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.renderpanel;
import prototype.utility.LoadSave;
import prototype.utility.SpriteRandomizer;

/**
 *
 * @author Serbs
 */
public class Weapon {
    
    private Entity2D host;
    
    private BufferedImage test = SpriteRandomizer.Randomize(LoadSave.loadSpriteSheet("PLAYER_WEAPON_TEMPLATE"));

    private WeaponPart core;
    private WeaponPart barrel;
    private WeaponPart grip;
    private WeaponPart stock;
    private WeaponPart mag;

    private float damage;
    private int firerate;
    private int reloadSpeed;
    private int magSize;
    private float accuracy;
    private float projectileSize;
    private float projectileSpeed;
    private int multishot;
    private int multihit;
    private float knockback;
    private float homing;
    private int lifetime;
    private int reverse;
    private float crit;
    private float odd;
    private float speedMod;
    private float acceleration;
    private float cooldownMod;
    private int dashMod;

    private int weaponLevel;

    private int firerateTimer;
    private int reloadTimer;
    private int magCounter;
    private boolean fire;
    private boolean reload;
    private float recoil=0;

    private ArrayList<Double> stats;
    private ArrayList<String> statNames = new ArrayList<>(Arrays.asList("DAMAGE ", "FIRERATE (RPS) ", "ACCURACY (%x) ", "RELOAD SPEED (s) ", "MAG SIZE ", "PROJECTILE SIZE (Units) ", "PROJECTILE SPEED (Units) ", "MULTISHOT ", "MULTIHIT ", "HOMING (%x) ",
            "LIFETIME (s) ", "REVERSE (>1=true) ", "CRIT (%x) ", "ODD (%x) ", "SPEED (%x) ", "ACCELERATION (%x) ", "DASH COOLDOWN (s)", "DASH MODIFIER (EMP 8xThis)", "KNOCKBACK"));

    public Weapon(Entity2D host,WeaponPart core, WeaponPart barrel, WeaponPart stock, WeaponPart grip, WeaponPart mag) {
        this.host=host;
        
        this.core = core;
        this.barrel=barrel;
        this.stock=stock;
        this.grip=grip;
        this.mag=mag;
        weaponLevel = (core.getLevel() + barrel.getLevel() + stock.getLevel() + grip.getLevel() + mag.getLevel()) / 5;
        damage = (float) ((1 + (weaponLevel - 1)) * core.getDamageMod() * barrel.getDamageMod() * stock.getDamageMod() * grip.getDamageMod() * mag.getDamageMod());
        if (damage < 0.1) {
            damage = 0.1f;
        }
        firerate = (int) ((0.6 * UPDATES_PER_SECOND) * core.getFirerateMod() * barrel.getFirerateMod() * stock.getFirerateMod() * grip.getFirerateMod() * mag.getFirerateMod());
        if (firerate < 4) {
            firerate = 4;
        }
        reloadSpeed = (int) ((3 * UPDATES_PER_SECOND) * core.getReloadSpeedMod() * barrel.getReloadSpeedMod() * stock.getReloadSpeedMod() * grip.getReloadSpeedMod() * mag.getReloadSpeedMod());
        if (reloadSpeed < 10) {
            reloadSpeed = 10;
        }
        magSize = core.getMagSizeMod() + barrel.getMagSizeMod() + stock.getMagSizeMod() + grip.getMagSizeMod() + mag.getMagSizeMod();
        accuracy = 0.9f * core.getAccuracyMod() * barrel.getAccuracyMod() * stock.getAccuracyMod() * grip.getAccuracyMod() * mag.getAccuracyMod();
        if (accuracy > 1) {
            accuracy = 1;
        }
        projectileSize = core.getProjectileSizeMod() * barrel.getProjectileSizeMod() * stock.getProjectileSizeMod() * grip.getProjectileSizeMod() * mag.getProjectileSizeMod();
        if (projectileSize > 3) {
            projectileSize = 3;
        } else if (projectileSize < 0.33) {
            projectileSize = 0.33f;
        }
        projectileSpeed = (TILE_SIZE * 10f / UPDATES_PER_SECOND) * core.getProjectileSpeedMod() * barrel.getProjectileSpeedMod() * stock.getProjectileSpeedMod() * grip.getProjectileSpeedMod() * mag.getProjectileSpeedMod();
        if (projectileSpeed > TILE_SIZE * 70f / UPDATES_PER_SECOND) {
            projectileSpeed = TILE_SIZE * 70f / UPDATES_PER_SECOND;
        }
        multishot = 1 + core.getMultishotMod() + barrel.getMultishotMod() + stock.getMultishotMod() + grip.getMultishotMod() + mag.getMultishotMod();
        multihit = 1 + core.getMultihitMod() + barrel.getMultihitMod() + stock.getMultihitMod() + grip.getMultihitMod() + mag.getMultihitMod();
        knockback = core.getKnockbackMod() + barrel.getKnockbackMod() + stock.getKnockbackMod() + grip.getKnockbackMod() + mag.getKnockbackMod();
        if (knockback > 2) {
            knockback = 2;
        } else if (knockback < 0) {
            knockback = 0;
        }
        homing = 0 + core.getHomingMod() + barrel.getHomingMod() + stock.getHomingMod() + grip.getHomingMod() + mag.getHomingMod();
        lifetime = (int) (2.5 * UPDATES_PER_SECOND);
        reverse = 0 + core.getReverseMod() + barrel.getReverseMod() + stock.getReverseMod() + grip.getReverseMod() + mag.getReverseMod();
        crit = 0 + core.getCritMod() + barrel.getCritMod() + stock.getCritMod() + grip.getCritMod() + mag.getCritMod();
        speedMod = 1 + core.getSpeedMod() + barrel.getSpeedMod() + stock.getSpeedMod() + grip.getSpeedMod() + mag.getSpeedMod();
        odd = 0 + core.getOddMod() + barrel.getOddMod() + stock.getOddMod() + grip.getOddMod() + mag.getOddMod();
        acceleration = 0 + core.getAccelerationMod() + barrel.getAccelerationMod() + stock.getAccelerationMod() + grip.getAccelerationMod() + mag.getAccelerationMod();
        cooldownMod = 1 - core.getCooldownMod() - barrel.getCooldownMod() - stock.getCooldownMod() - grip.getCooldownMod() - mag.getCooldownMod();
        if (cooldownMod < 0.2) {
            cooldownMod = 0.2f;
        }
        dashMod = 0 + core.getDashMod() + barrel.getDashMod() + stock.getDashMod() + grip.getDashMod() + mag.getDashMod();

        fire = false;
        firerateTimer = 0;
        magCounter = magSize;

        stats = new ArrayList<>(Arrays.asList((double) (Math.round(damage * 100d) / 100d), (double) (Math.round((60 / (double) firerate) * 100d) / 100d),
                (double) (Math.round(accuracy * 100d) / 100d), (double) (Math.round(((double) reloadSpeed / 60) * 100d) / 100d), (double) magSize, (double) (Math.round(projectileSize * 100d) / 100d), (double) (Math.round((projectileSpeed / (TILE_SIZE * 0.5)) * 100d) / 100d), (double) multishot, (double) multihit, (double) (Math.round(homing * 100d) / 100d),
                (double) (Math.round(((double) lifetime / 60) * 100d) / 100d), (double) reverse, (double) (Math.round(crit * 100d) / 100d), (double) (Math.round(odd * 100d) / 100d),
                (double) (Math.round(speedMod * 100d) / 100d), (double) (Math.round(acceleration * 100d) / 100d), (double) (Math.round((5 / cooldownMod) * 100d) / 100d),
                (double) dashMod, (double) (Math.round(knockback * 100d) / 100d)));
    }

    //Sets every weapon stat based on all five weapon parts and their attributes.
    private void setWeaponStats() {
        weaponLevel = (core.getLevel() + barrel.getLevel() + stock.getLevel() + grip.getLevel() + mag.getLevel()) / 5;
        damage = (float) ((1 + (weaponLevel - 1)) * core.getDamageMod() * barrel.getDamageMod() * stock.getDamageMod() * grip.getDamageMod() * mag.getDamageMod());
        if (damage < 0.1) {
            damage = 0.1f;
        }
        firerate = (int) ((0.6*UPDATES_PER_SECOND) * core.getFirerateMod() * barrel.getFirerateMod() * stock.getFirerateMod() * grip.getFirerateMod() * mag.getFirerateMod());
        if (firerate < 4) {
            firerate = 4;
        }
        accuracy = (0.9f + ((weaponLevel - 1) / 100)) * core.getAccuracyMod() * barrel.getAccuracyMod() * stock.getAccuracyMod() * grip.getAccuracyMod() * mag.getAccuracyMod();
        if (accuracy > 1) {
            accuracy = 1;
        }
        reloadSpeed = (int) ((2.5*UPDATES_PER_SECOND) * core.getReloadSpeedMod() * barrel.getReloadSpeedMod() * stock.getReloadSpeedMod() * grip.getReloadSpeedMod() * mag.getReloadSpeedMod());
        if (reloadSpeed < 10) {
            reloadSpeed = 10;
        }
        magSize = core.getMagSizeMod() + barrel.getMagSizeMod() + stock.getMagSizeMod() + grip.getMagSizeMod() + mag.getMagSizeMod();
        projectileSize = core.getProjectileSizeMod() * barrel.getProjectileSizeMod() * stock.getProjectileSizeMod() * grip.getProjectileSizeMod() * mag.getProjectileSizeMod();
        if (projectileSize > 5) {
            projectileSize = 5;
        } else if (projectileSize < 0.25) {
            projectileSize = 0.25f;
        }
        projectileSpeed = (TILE_SIZE*10f/UPDATES_PER_SECOND) * core.getProjectileSpeedMod() * barrel.getProjectileSpeedMod() * stock.getProjectileSpeedMod() * grip.getProjectileSpeedMod() * mag.getProjectileSpeedMod();
        if (projectileSpeed > TILE_SIZE*70f/UPDATES_PER_SECOND) {
            projectileSpeed = TILE_SIZE*70f/UPDATES_PER_SECOND;
        }
        multishot = 1 + core.getMultishotMod() + barrel.getMultishotMod() + stock.getMultishotMod() + grip.getMultishotMod() + mag.getMultishotMod();
        multihit = 1 + core.getMultihitMod() + barrel.getMultihitMod() + stock.getMultihitMod() + grip.getMultihitMod() + mag.getMultihitMod();
        knockback= core.getKnockbackMod()+barrel.getKnockbackMod()+stock.getKnockbackMod()+grip.getKnockbackMod()+mag.getKnockbackMod();
        if(knockback>2){
            knockback=2;
        }else if(knockback<0){
            knockback=0;
        }
        homing = 0 + core.getHomingMod() + barrel.getHomingMod() + stock.getHomingMod() + grip.getHomingMod() + mag.getHomingMod();
        if (homing > 1) {
            homing = 1;
        }
        reverse = 0 + core.getReverseMod() + barrel.getReverseMod() + stock.getReverseMod() + grip.getReverseMod() + mag.getReverseMod();
        lifetime = (int) ((3*UPDATES_PER_SECOND) * core.getLifetimeMod() * barrel.getLifetimeMod() * stock.getLifetimeMod() * grip.getLifetimeMod() * mag.getLifetimeMod());
        crit = 0 + core.getCritMod() + barrel.getCritMod() + stock.getCritMod() + grip.getCritMod() + mag.getCritMod();
        odd = 0 + core.getOddMod() + barrel.getOddMod() + stock.getOddMod() + grip.getOddMod() + mag.getOddMod();
        speedMod = 1 + core.getSpeedMod() + barrel.getSpeedMod() + stock.getSpeedMod() + grip.getSpeedMod() + mag.getSpeedMod();
        acceleration = 0 + core.getAccelerationMod() + barrel.getAccelerationMod() + stock.getAccelerationMod() + grip.getAccelerationMod() + mag.getAccelerationMod();
        cooldownMod = 1 - core.getCooldownMod() - barrel.getCooldownMod() - stock.getCooldownMod() - grip.getCooldownMod() - mag.getCooldownMod();
        if (cooldownMod < 0.2) {
            cooldownMod = 0.2f;
        }
        dashMod = 0 + core.getDashMod() + barrel.getDashMod() + stock.getDashMod() + grip.getDashMod() + mag.getDashMod();
        if(host.equals(gameboard.getPlayer())){
            gameboard.getPlayer().setSpeed(speedMod);
            gameboard.getPlayer().setDashCooldown(cooldownMod);
            gameboard.getPlayer().setDashModifier(dashMod);
        }
        

        stats = new ArrayList<Double>(Arrays.asList((double) (Math.round(damage * 100d) / 100d), (double) (Math.round((60 / (double) firerate) * 100d) / 100d),
                (double) (Math.round(accuracy * 100d) / 100d),(double) (Math.round(((double) reloadSpeed/60) * 100d) / 100d),(double)magSize, (double) (Math.round(projectileSize * 100d) / 100d), (double) (Math.round((projectileSpeed/(TILE_SIZE*0.5)) * 100d) / 100d), (double) multishot, (double) multihit, (double) (Math.round(homing * 100d) / 100d),
                (double) (Math.round(((double) lifetime/60) * 100d) / 100d), (double) reverse, (double) (Math.round(crit * 100d) / 100d), (double) (Math.round(odd * 100d) / 100d),
                (double) (Math.round(speedMod * 100d) / 100d), (double) (Math.round(acceleration * 100d) / 100d), (double) (Math.round((5/cooldownMod) * 100d) / 100d),
                (double) dashMod, (double) (Math.round(knockback * 100d) / 100d)));
    }

    //Sets the picked up weapon part by its type to the corresponding variable. 
    //Calls setWeaponStats after to update based on the newly aquired weapon part.
    public void setWeaponPart(WeaponPart part) {
        switch (part.getType()) {
            case CORE -> core = part;
            case BARREL -> barrel = part;
            case STOCK -> stock = part;
            case GRIP -> grip = part;
            case MAG -> mag = part;
        }
        setWeaponStats();
    }

    public void Fire(boolean f) {
        fire = f;
    }

    //Handles Firing bullets with firerate, mag size and reload speed.
    public void fireBullet(Point2D target, double rotation) {
        if (fire && !reload) {
            if (firerateTimer == 0 && magCounter > 0) {
                Point2D firepoint=getFirePoint(rotation);
                if(levelmanager.getBounds().contains(firepoint)
                        &&levelmanager.getMap().getCurrentTile(levelmanager.getIndexFromPoint(firepoint)).getType()!=TileType.WALL){
                    double dx = target.getX() - host.getBounds().getCenterX();
                    double dy = target.getY() - host.getBounds().getCenterY();
                    double bulletDirection = Math.atan2(dy, dx);
                    for (int i = 0; i < multishot; i++) {
                        if(host.isFriendly()){
                            gameboard.getBullets().add(new Bullet(damage, accuracy, projectileSpeed, projectileSize, multihit, knockback, 
                            homing, lifetime, reverse, crit, odd, acceleration, true, firepoint, bulletDirection, barrel.getPrefix()));
                        }else{
                            gameboard.getBullets().add(new Bullet(damage, accuracy, projectileSpeed, projectileSize, multihit, knockback, 
                            homing, lifetime, reverse, crit, odd, acceleration, false, firepoint, bulletDirection, barrel.getPrefix()));
                        }
                        
                    }
                }
                if(host.equals(gameboard.getPlayer())){
                    soundengine.playSFX(SFX.FIRE);
                }
                
                firerateTimer = firerate;
                magCounter--;
                recoil=4;
            }
            if (magCounter == 0) {
                reloadTimer = reloadSpeed;
                reload = true;
                if(host.equals(gameboard.getPlayer())){
                    gameboard.getPlayer().setAction(true);
                soundengine.playSFX(SFX.RELOAD);
                }
                
            }
        }
        if (firerateTimer > 0) {
            firerateTimer--;
            
        }
        if (reloadTimer > 0) {
            reloadTimer--;
        }
        if (reloadTimer == 0 && reload) {
            magCounter = magSize;
            reload = false;
            if(host.equals(gameboard.getPlayer())){
                soundengine.playSFX(SFX.RELOAD_END);
            }
            
        }
        if(recoil>0){
            recoil*=0.95;
        }else{
            recoil=0;
        }
    }
    
    public Graphics2D render(Graphics2D g2d, AffineTransform normal, AffineTransform position, Tile location, double anchorX, double anchorY, double rotation){
        AffineTransform offsetPosition= new AffineTransform();
        offsetPosition.translate(position.getTranslateX()+4, position.getTranslateY()-8+recoil);
        g2d.rotate(rotation, anchorX, anchorY);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, location.getLight()));      
        g2d.drawImage(test, offsetPosition, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.setTransform(normal);
        return g2d;
    }
    
    private Point2D getFirePoint(double rotation){
        Point2D fire=new Point2D.Float((float)(host.getBounds().getCenterX()+4), (float)(host.getBounds().getY()-4));
        Point2D result = new Point2D.Double();
        AffineTransform rot = new AffineTransform();
        rot.rotate(rotation, host.getBounds().getCenterX(), host.getBounds().getCenterY());
        rot.transform(fire, result);
        return result;
    }

    public WeaponPart getBarrel() {
        return barrel;
    }

    public WeaponPart getCore() {
        return core;
    }

    public WeaponPart getStock() {
        return stock;
    }

    public WeaponPart getGrip() {
        return grip;
    }

    public WeaponPart getMag() {
        return mag;
    }

    public int getPowerLevel() {
        int powerLevel = weaponLevel * (barrel.getRarityValue() + core.getRarityValue() + stock.getRarityValue() + grip.getRarityValue() + mag.getRarityValue());
        return powerLevel;
    }

    public ArrayList getWeaponStats() {
        return stats;
    }

    public ArrayList getWeaponStatNames() {
        return statNames;
    }

    public String getMagString() {
        String magString = magCounter + "/" + magSize;
        return magString;
    }

    public double getReloadCooldown() {
        double cd = (double) Math.round(((double) (reloadTimer) / UPDATES_PER_SECOND) * 100d) / 100d;
        return cd;
    }
    
    public int getReloadSpeed(){
        return reloadSpeed;
    }
    public int getFireRate(){
        return firerate;
    }

    public boolean getReload() {
        return reload;
    }
    
    public boolean getFire() {
        return fire;
    }

    //Method that forces a reload without needing to empty the mag. 
    //Checks that mag cant be full and that you can't force another reload during reloading
    public void Reload() {
        if (magCounter != magSize && !reload) {
            reloadTimer = reloadSpeed;
            reload = true;
            if(host.equals(gameboard.getPlayer())){
                soundengine.playSFX(SFX.RELOAD); 
            }
            
        }
    }
}
