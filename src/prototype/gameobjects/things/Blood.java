package prototype.gameobjects.things;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import static prototype.utility.Constants.Animations.*;
import static prototype.utility.Constants.System.*;
import prototype.system.GameLoop;
import static prototype.system.GameLoop.getFPS;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;

/**
 *
 * @author Serbs
 */
public class Blood {
    private Point2D spawn;
    private final BufferedImage[] currentAnim;
    private int animTicker, animPosition, animSpeed = (int) (0.5 * getFPS());
    private boolean active;
    private double rotation;
    private float size;
    
    public Blood(Point2D spawn, float size){
        this.spawn=spawn;
        active=true;
        this.size=TILE_SIZE*size;
        currentAnim = getAnimation(animation.BLOOD);
        rotation=(int) (Math.random() * 360);
    }
    
    public Point2D getLocationIndex() {
        return new Point2D.Float((int) (spawn.getX() / TILE_SIZE), (int) (spawn.getY() / TILE_SIZE));
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void updateBlood(){
        if(active){
            updateAnimation();
        }
    }
    private void updateAnimation(){
        if (animTicker >= animSpeed) {
            animPosition++;
            if (animPosition >= getAnimation(animation.BLOOD).length-1) {
                active=false;
            }
            animTicker = 0;
        }
        animTicker++;
    }
    public Graphics2D drawBlood(Graphics2D g2d, AffineTransform normal) {
            g2d.rotate((rotation), (spawn.getX()), (spawn.getY()));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, levelmanager.getMap().getCurrentTile(getLocationIndex()).getLight()));
            g2d.drawImage(currentAnim[animPosition], (int) (spawn.getX()-(size/2)), (int) (spawn.getY()-(size/2)), (int) (size), (int) (size), null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.setTransform(normal);
        return g2d;
    }
}
