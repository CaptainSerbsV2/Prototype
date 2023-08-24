package prototype.gameobjects.things;

import prototype.lootengine.WeaponPart;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static prototype.utility.Constants.System.*;
import static prototype.system.Prototype.gameboard;

/**
 *
 * @author Serbs
 */
public class LootDrop {

    public enum partType {
        CORE,
        BARREL,
        GRIP,
        STOCK,
        MAG
    }

    public enum partPrefix {
        BASIC,
        MARKSMAN,
        HUGE,
        RAPID,
        SPLATTER,
        ODD,
        SWIFT
    }

    public enum partRarity {
        COMMON,
        UNCOMMON,
        RARE,
        UNIQUE,
        EPIC,
        LEGENDARY
    }

    private Rectangle2D box;
    private Rectangle2D triggerArea;
    private int lifetime;
    private Color color;
    private WeaponPart weaponPart;

    public LootDrop(Point2D start, WeaponPart part) {
        weaponPart = part;
        box = new Rectangle.Double(start.getX(), start.getY(), TILE_SIZE * 0.75, TILE_SIZE * 0.75);
        triggerArea = new Rectangle2D.Double(start.getX()-TILE_SIZE*0.75, start.getY()-TILE_SIZE*0.75, TILE_SIZE*2.25, TILE_SIZE*2.25);
        lifetime = 20 * UPDATES_PER_SECOND;
        color = weaponPart.getPartColor();

    }

    public Rectangle2D getBox() {
        return box;
    }
    
    public Rectangle2D getTriggerArea() {
        return triggerArea;
    }

    public WeaponPart getPart() {
        return weaponPart;
    }

    public Color getColor() {
        return color;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void updateLootDrop() {
        lifetime--;
    }
    
    public void pickUp(){
        gameboard.getPlayer().getWeapon().setWeaponPart(weaponPart);
            lifetime = 0;
    }

    public Graphics2D drawLootDrop(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(new Font("Impact", Font.ITALIC, (int) (TILE_SIZE * 0.75)));
        g2d.drawString(weaponPart.getPartName(), (int) box.getX(), (int) (box.getY() - TILE_SIZE));
        g2d.fill(box);
        g2d.setColor(Color.black);
        g2d.draw(box);
        return g2d;
    }

    public Graphics2D drawLootDropPaused(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.draw(box);

        return g2d;
    }

}
