package prototype.system;

import prototype.gameobjects.things.LootDrop;
import prototype.lootengine.WeaponPart;
import prototype.gameobjects.things.Bullet;
import prototype.gameobjects.enemies.Brute;
import prototype.gameobjects.enemies.Crusher;
import prototype.gameobjects.enemies.Gunner;
import prototype.gameobjects.player.Player;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import prototype.gameobjects.Entity2D;
import prototype.gameobjects.Entity2D.State;
import static prototype.system.Prototype.soundengine;
import prototype.gameobjects.things.LootDrop.partPrefix;
import prototype.gameobjects.things.LootDrop.partRarity;
import prototype.gameobjects.things.LootDrop.partType;
import prototype.utility.Constants.Audio.*;
import prototype.system.GameLoop.gameState;
import prototype.gameobjects.things.Blood;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.renderpanel;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.hitboxData.*;
import prototype.lootengine.LootEngine;

/**
 *
 * @author Serbs
 */
public class GameBoard {

    private Player player;
    private final Set<Bullet> bullets = Collections.synchronizedSet(new HashSet<>());
    private final Set<Bullet> enemyBullets = Collections.synchronizedSet(new HashSet<>());
    private final Set<Entity2D> enemies =Collections.synchronizedSet(new HashSet<>());
    private final Set<LootDrop> lootDrops = Collections.synchronizedSet(new HashSet<>());
    private final Set<Blood> bloods = Collections.synchronizedSet(new HashSet<>());

    private int frame;
    private int time;
    private float difficulty;

    private double enemyCooldown;
    private int enemyTimer;
    private int lootDropRateBonus;

    public GameBoard() {
        initGameControl();
    }

    private void initGameControl() {
        frame = 0;
        time = 0;
        difficulty = 1;
        enemyCooldown = (8 * UPDATES_PER_SECOND) * Math.pow(0.9, (difficulty - 1));
        Point2D playerSpawn = levelmanager.getRandomSpawnPoint(false);
        player = new Player(playerSpawn.getX(),playerSpawn.getY(),PLAYER_HITBOX,1);        
        lootDropRateBonus = 0;
        levelmanager.getMap().setStartingTileValues();
    }

    public void update() {
        switch (GameLoop.getCurrentGameState()) {
            case IN_GAME -> {
                player.update();
                for(var bullet : bullets){
                    bullet.update();
                }
                for(var bullet : enemyBullets){
                    bullet.update();
                }
                for(var enemy : enemies){
                    enemy.update();
                }
                for(var lootDrop : lootDrops){
                    lootDrop.updateLootDrop();
                }
                for(var blood : bloods){
                    blood.updateBlood();
                }
                time = (frame / UPDATES_PER_SECOND);
                if (difficulty < 70 && player.getLevel() > 1) {
                    difficulty = player.getLevel() * 1.20f;
                }
                if (player.getState()==State.DEAD) {
                    GameLoop.setGameState(gameState.GAME_OVER);
                    soundengine.playSFX(SFX.DEATH);
                    soundengine.setSoundtrackGameOver();
                    player.resetInputActions();
                }
//                enemySpawner();
                despawner();
                frame++;
                break;
            }
            case GAME_OVER -> {
                player.update();

                for(var enemy : enemies){
                    if (enemy.getHP() > 0) {
                        enemy.hurt(9999999);
                    }
                    enemy.update();
                }
                despawner();
            }
        }

    }

    public int getTime() {
        return time;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Bullet> getBullets() {
        return bullets;
    }
    public Set<Bullet> getEnemyBullets() {
        return enemyBullets;
    }

    public Set<Entity2D> getEnemies() {
        return enemies;
    }

    public Set<LootDrop> getLootDrops() {
        return lootDrops;
    }
    public Set<Blood> getBloods() {
        return bloods;
    }

    private void enemySpawner() {
        if (enemyTimer <= 0) {
            if (enemyCooldown > 1 * UPDATES_PER_SECOND) {
                enemyCooldown = (8 * UPDATES_PER_SECOND) * Math.pow(0.9, (difficulty - 1));
            }
            if (enemies.size() < 50 ) {
                for(int i = 0; i<2; i++){
                    int randomMob = (int) ((Math.random() * 6) + 1);
//                    int randomMob = 6;
                    Point2D spawn=levelmanager.getRandomSpawnPoint(true);
                    switch (randomMob) {
                        case 1, 2, 3,4 ->
                            enemies.add(new Brute(spawn.getX(),spawn.getY(),PLAYER_HITBOX,1));
                        case 5 ->
                            enemies.add(new Gunner(spawn.getX(),spawn.getY(),PLAYER_HITBOX,1));
                        case 6 ->
                            enemies.add(new Crusher(spawn.getX(),spawn.getY(),PLAYER_HITBOX,1));
                    }
                }
                    
                enemyTimer = (int) enemyCooldown;             
            }
        }
        enemyTimer--;
    }

    public void lootDropSpawner(Point2D spawn) {
        double spawnChance = Math.random();
        if (spawnChance < (0.1 + lootDropRateBonus)) {
            WeaponPart part = LootEngine.getRandomWeaponPart(player.getLevel());
            lootDrops.add(new LootDrop(spawn, part));
            lootDropRateBonus = 0;
        } else {
            lootDropRateBonus += 0.02;
        }
    }

    private void despawner() {
        if(!renderpanel.isRendering()){
            ArrayList<Entity2D>enemiesToDespawn=new ArrayList<>();
            for(var enemy:enemies){
                if(!enemy.isActive()){
                    enemiesToDespawn.add(enemy);
                }
            }
            player.setXP(15*enemiesToDespawn.size());
            enemies.removeAll(enemiesToDespawn);

            ArrayList<Bullet>bulletsToDespawn=new ArrayList<>();
            for(var bullet:bullets){
                if(!bullet.isActive()){
                    bulletsToDespawn.add(bullet);
                }
            }
            bullets.removeAll(bulletsToDespawn);

            ArrayList<Bullet>enemyBulletsToDespawn=new ArrayList<>();
            for(var bullet:enemyBullets){
                if(!bullet.isActive()){
                    bulletsToDespawn.add(bullet);
                }
            }
            enemyBullets.removeAll(bulletsToDespawn);

            ArrayList<LootDrop>lootDropsToDespawn=new ArrayList<>();
            for(var lootDrop:lootDrops){
                if(lootDrop.getLifetime()<=0){
                    lootDropsToDespawn.add(lootDrop);
                }
            }
            lootDrops.removeAll(lootDropsToDespawn);

            ArrayList<Blood>BloodToDespawn=new ArrayList<>();
            for(var blood:bloods){
                if(!blood.getActive()){
                    BloodToDespawn.add(blood);
                }
            }
            bloods.removeAll(BloodToDespawn);
        }
    }

    public void Restart() {
        enemies.clear();
        lootDrops.clear();
        bullets.clear();
        bloods.clear();
        initGameControl();
        if(GameLoop.getCurrentGameState()==gameState.GAME_OVER){
            soundengine.setSoundtrackInGame();
        }
        GameLoop.setGameState(gameState.IN_GAME);
        levelmanager.setFlowField(999*999, player.getLocationIndex());

    }

}
