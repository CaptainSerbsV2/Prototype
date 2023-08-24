package prototype.lootengine;

import java.awt.Color;
import prototype.gameobjects.things.LootDrop.*;

/**
 *
 * @author Serbs
 */
public class WeaponPart {

    private partRarity rarity;
    private int rarityValue;
    private partPrefix prefix;
    private partType type;
    private String partName;
    private Color partColor;

    private float damageMod;
    private float accuracyMod;
    private float firerateMod;
    private float reloadSpeedMod;
    private int magSizeMod;
    private float projectileSizeMod;
    private float projectileSpeedMod;
    private int multihitMod;
    private int multishotMod;
    private float homingMod;
    private float lifetimeMod;
    private int reverseMod;
    private float critMod;
    private float speedMod;
    private float oddMod;
    private float accelerationMod;
    private float cooldownMod;
    private float knockbackMod;
    private int dashMod;

    private int level;

    public WeaponPart(partType t, partPrefix pre, partRarity rar, int lvl) {
        type = t;
        prefix = pre;
        rarity = rar;

        switch (rarity) {
            case COMMON -> {
                partColor = Color.white;
                rarityValue = 0;
                break;
            }
            case UNCOMMON -> {
                partColor = Color.green;
                rarityValue = 1;
                break;
            }
            case RARE -> {
                partColor = Color.blue;
                rarityValue = 2;
                break;
            }
            case UNIQUE -> {
                partColor = Color.blue;
                rarityValue = 3;
                break;
            }
            case EPIC -> {
                partColor = Color.magenta;
                rarityValue = 4;
                break;
            }
            case LEGENDARY -> {
                partColor = Color.orange;
                rarityValue = 5;
                break;
            }
        }
        level = lvl;
        partName = type + " / " + prefix + " " + rarity + " / lvl" + level;
        
       //Default values, AKA Basic non Unique/Legendary (hence that switch only has a break)
       damageMod = 1 + (0.05f * rarityValue) + (0.01f * (level - 1));
        accuracyMod = 1 + (0.01f * rarityValue) + (0.005f * (level - 1));
        firerateMod = (1 - (0.01f * rarityValue) - (0.005f * (level - 1)));
        reloadSpeedMod = (1 - (0.01f * rarityValue) - (0.005f * (level - 1)));
        magSizeMod = 3;
        projectileSizeMod = 1;
        projectileSpeedMod = 1;
        multihitMod = 0;
        multishotMod = 0;
        knockbackMod = 0;
        homingMod = 0;
        lifetimeMod = 1;
        reverseMod = 0;
        critMod = 0;
        speedMod = 0;
        oddMod = 0;
        accelerationMod = 0;
        cooldownMod = 0;
        dashMod = 0;
       
        switch (prefix) {
            case BASIC -> { // No prefix bonuses
                switch (rarity) {
                    case UNIQUE -> { // Bullet spawns at cursor and flies towards player
                        reverseMod = 1; //SPECIAL
                        break;
                    }
                    case LEGENDARY -> { // Flat 5% increase on most stats.
                        damageMod = damageMod * 1.05f; //SPECIAL
                        accuracyMod = accuracyMod * 1.05f; //SPECIAL
                        firerateMod = firerateMod * 0.95f; //SPECIAL
                        reloadSpeedMod = reloadSpeedMod * 0.95f; //SPECIAL
                        projectileSizeMod = 1.05f; //SPECIAL
                        projectileSpeedMod = 1.05f; //SPECIAL
                        knockbackMod = 0.05f; //SPECIAL
                        lifetimeMod = 1.05f; //SPECIAL
                        critMod = 0.05f; //SPECIAL
                        speedMod = 0.05f; //SPECIAL
                        cooldownMod = 0.05f; //SPECIAL
                        break;
                    }
                    default -> {
                        break;
                    }
                }
                break;
            }
            case MARKSMAN -> { // damage +20%, accuracy +20%, firerate -20%, projectile size -20%, projectile speed +20%, lifetime +20%, knockback 20%
                switch (rarity) {
                    case UNIQUE -> { // +100% projectile speed
                        damageMod = damageMod * 1.2f;
                        accuracyMod = accuracyMod * 1.2f;
                        firerateMod = firerateMod * 1.2f;
                        projectileSizeMod = 0.8f;
                        projectileSpeedMod = 2.0f; //SPECIAL
                        lifetimeMod = 1.2f;
                        knockbackMod = 0.2f; //SPECIAL
                        break;
                    }
                    case LEGENDARY -> { // 15% crit chance
                        damageMod = damageMod * 1.2f;
                        accuracyMod = accuracyMod * 1.2f;
                        firerateMod = firerateMod * 1.2f;
                        projectileSizeMod = 0.8f;
                        projectileSpeedMod = 1.2f;
                        lifetimeMod = 1.2f;
                        knockbackMod = 0.2f;
                        critMod = 0.15f; //SPECIAL
                        break;
                    }
                    default -> {
                        damageMod = damageMod * 1.2f;
                        accuracyMod = accuracyMod * 1.2f;
                        firerateMod = firerateMod * 1.2f;
                        projectileSizeMod = 0.8f;
                        projectileSpeedMod = 1.2f;
                        lifetimeMod = 1.2f;
                        knockbackMod = 0.2f;
                        break;
                    }
                }
                break;
            }
            case HUGE -> { //  firerate -50%, reload speed -20%, mag size -1, projectile size +20%, projectile speed -20%, knockback 10%, Multihit +2 (essentially damage +200%)
                switch (rarity) {
                    case UNIQUE -> { // Projectile size +100%
                        firerateMod = firerateMod * 1.5f;
                        reloadSpeedMod = reloadSpeedMod * 1.2f;
                        magSizeMod = 2;
                        projectileSizeMod = 2; //SPECIAL
                        projectileSpeedMod = 0.8f;
                        multihitMod = 2;
                        knockbackMod = 0.1f;
                        break;
                    }
                    case LEGENDARY -> { // Almost stationary bullets, Multihit +10(essentially damage +1000%), lifetime +50%, removes knockback from the whole weapon;
                        firerateMod = firerateMod * 1.5f;
                        reloadSpeedMod = reloadSpeedMod * 1.2f;
                        magSizeMod = 1;
                        projectileSizeMod = 1.2f;
                        projectileSpeedMod = 0.01f; //SPECIAL
                        multihitMod = 10; //SPECIAL
                        knockbackMod = -10000f; //SPECIAL
                        lifetimeMod = 1.5f; //SPECIAL
                        break;
                    }
                    default -> {
                        firerateMod = firerateMod * 1.5f;
                        reloadSpeedMod = reloadSpeedMod * 1.2f;
                        magSizeMod = 2;
                        projectileSizeMod = 1.2f;
                        projectileSpeedMod = 0.8f;
                        multihitMod = 2;
                        knockbackMod = 0.1f;
                        break;
                    }
                }
                break;
            }
            case RAPID -> { // damage -20%, accuracy -10%, firerate +30%
                switch (rarity) {
                    case UNIQUE -> { // about 360 degree accuracy spread
                        damageMod = damageMod*0.8f;
                        accuracyMod = -3; //SPECIAL *note: not sure why -3 but it works*
                        firerateMod = firerateMod * 0.25f; //SPECIAL
                        magSizeMod = 4;
                        break;
                    }
                    case LEGENDARY -> { // Mag size +50
                        damageMod = damageMod* 0.8f;
                        accuracyMod = accuracyMod* 0.9f;
                        firerateMod = firerateMod * 0.75f;
                        magSizeMod = 50; //SPECIAL
                        break;
                    }
                    default -> {
                        damageMod = damageMod* 0.8f;
                        accuracyMod = accuracyMod* 0.9f;
                        firerateMod = firerateMod * 0.75f;
                        magSizeMod = 4;
                        break;
                    }
                }
                break;
            }
            case SPLATTER -> {
                switch (rarity) { // damage -60%, accuracy -30%, mag size -2, projectile speed -10%, multishot +2, lifetime -20%, knockback +5% (damage + multishot essentially damage +120%)
                    case UNIQUE -> { // Bullets follow cursor
                        damageMod = damageMod * 0.4f;
                        accuracyMod = accuracyMod * 0.7f;
                        magSizeMod = 1;
                        projectileSpeedMod = 0.9f;
                        multishotMod = 2;
                        homingMod = 0.4f; // SPECIAL
                        lifetimeMod = 0.8f;
                        knockbackMod = 0.05f;
                        break;
                    }
                    case LEGENDARY -> {// damage -80%, multishot +10 (combined with damage, essentially damage +200%)
                        damageMod = damageMod * 0.2f; // SPECIAL
                        accuracyMod = accuracyMod * 0.7f;
                        magSizeMod = 1;
                        projectileSpeedMod = 0.9f;
                        multishotMod = 10; // SPECIAL
                        lifetimeMod = 0.8f;
                        knockbackMod = 0.05f;
                        break;
                    }
                    default -> {
                        damageMod = damageMod * 0.4f;
                        accuracyMod = accuracyMod * 0.7f;
                        magSizeMod = 1;
                        projectileSpeedMod = 0.9f;
                        multishotMod = 2;
                        lifetimeMod = 0.8f;
                        knockbackMod = 0.05f;
                        break;
                    }
                }
                break;
            }
            case ODD -> { // damage +25%, mag size -1, lifetime +25%, odd +20%(bullets curve randomly. % increases the magnitude of the curves)
                switch (rarity) {
                    case UNIQUE -> { // damage + 50%, lifetime +50%, odd +300%
                        damageMod = damageMod * 1.5f; // SPECIAL
                        magSizeMod = 2;
                        lifetimeMod = 1.5f; // SPECIAL
                        oddMod = 3; // SPECIAL
                        break;
                    }
                    case LEGENDARY -> { // acceleration +0.05% (per game update)
                        damageMod = damageMod * 1.25f;
                        magSizeMod = 2;
                        lifetimeMod = 1.25f;
                        oddMod = 0.2f;
                        accelerationMod = 0.005f; // SPECIAL
                        break;
                    }
                    default -> {
                        damageMod = damageMod * 1.25f;
                        magSizeMod = 2;
                        lifetimeMod = 1.25f;
                        oddMod = 0.2f;
                        break;
                    }
                }
                break;
            }
            case SWIFT -> { // damage -10%, reload speed -20%, mag size -1, projectile speed +10%, player speed +15%
                switch (rarity) {
                    case UNIQUE -> { // dash cooldown -20%
                        damageMod = damageMod * 0.9f;
                        reloadSpeedMod = reloadSpeedMod * 0.8f;
                        magSizeMod = 2;
                        projectileSpeedMod = 1.1f;
                        speedMod = 0.15f;
                        cooldownMod = 0.2f; // SPECIAL
                        break;
                    }
                    case LEGENDARY -> { // dash modifier +1 (on dash activation, releases 8 player level scaled crit bullets around you)
                        damageMod = damageMod * 0.9f;
                        reloadSpeedMod = reloadSpeedMod * 0.8f;
                        magSizeMod = 2;
                        projectileSpeedMod = 1.1f;
                        speedMod = 0.15f;
                        dashMod = 1; // SPECIAL
                        break;
                    }
                    default -> {
                        damageMod = damageMod * 0.9f;
                        reloadSpeedMod = reloadSpeedMod * 0.8f;
                        magSizeMod = 2;
                        projectileSpeedMod = 1.1f;
                        speedMod = 0.15f;
                        break;
                    }
                }
                break;
            }
        }

    }

    public partType getType() {
        return type;
    }

    public partPrefix getPrefix() {
        return prefix;
    }

    public partRarity getRarity() {
        return rarity;
    }

    public int getRarityValue() {
        return rarityValue;
    }

    public String getPartName() {
        return partName;
    }

    public Color getPartColor() {
        return partColor;
    }

    public float getDamageMod() {
        return damageMod;
    }

    public float getFirerateMod() {
        return firerateMod;
    }

    public float getAccuracyMod() {
        return accuracyMod;
    }

    public float getReloadSpeedMod() {
        return reloadSpeedMod;
    }

    public int getMagSizeMod() {
        return magSizeMod;
    }

    public float getProjectileSizeMod() {
        return projectileSizeMod;
    }

    public float getProjectileSpeedMod() {
        return projectileSpeedMod;
    }

    public int getMultihitMod() {
        return multihitMod;
    }

    public int getMultishotMod() {
        return multishotMod;
    }
    public float getKnockbackMod(){
        return knockbackMod;
    }

    public int getLevel() {
        return level;
    }

    public float getHomingMod() {
        return homingMod;
    }

    public int getReverseMod() {
        return reverseMod;
    }

    public float getLifetimeMod() {
        return lifetimeMod;
    }

    public float getCritMod() {
        return critMod;
    }

    public float getSpeedMod() {
        return speedMod;
    }

    public float getOddMod() {
        return oddMod;
    }

    public float getAccelerationMod() {
        return accelerationMod;
    }

    public float getCooldownMod() {
        return cooldownMod;
    }

    public int getDashMod() {
        return dashMod;
    }
}
