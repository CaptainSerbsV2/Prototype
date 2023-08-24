package prototype.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import static prototype.utility.Constants.Colors.DARK_TRANSPARENT;
import static prototype.utility.Constants.Fonts.*;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.UI.BOTTOM_RIGHT_FIELD;
import static prototype.utility.Constants.UI.TOP_LEFT_FIELD;
import static prototype.utility.Constants.UI.TOP_RIGHT_FIELD;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.frame;

/**
 *
 * @author Serbs
 */
public class HeadsUpDisplay {
    public HeadsUpDisplay(){
        
    }
    
    public Graphics2D drawHUD(Graphics2D g2d){
        g2d.setColor(DARK_TRANSPARENT);
        g2d.fill(TOP_LEFT_FIELD);
        g2d.fill(TOP_RIGHT_FIELD);
        g2d.fill(BOTTOM_RIGHT_FIELD);

        g2d.setColor(Color.white);
        g2d.setFont(FONT_SMALL);

        g2d.drawString("Time: " + gameboard.getTime() + "s", 0, TILE_SIZE );
        g2d.drawString("Difficulty: " + gameboard.getDifficulty(),0, TILE_SIZE * 2);
        g2d.drawString("HP: " + gameboard.getPlayer().getHP() + "/" + gameboard.getPlayer().getMaxHP(), (int)(TOP_RIGHT_FIELD.getX()), TILE_SIZE);
        g2d.drawString("Player level:" + gameboard.getPlayer().getLevel(), (int)(TOP_RIGHT_FIELD.getX()), TILE_SIZE*2);
        g2d.drawString("XP:" + gameboard.getPlayer().getXP() + "/" + (int) gameboard.getPlayer().getXpRequired(),(int)(TOP_RIGHT_FIELD.getX()), TILE_SIZE*3);
        
        
        g2d.setFont(FONT_MINISCULE);
        g2d.setColor(gameboard.getPlayer().getWeapon().getBarrel().getPartColor());
        g2d.drawString(gameboard.getPlayer().getWeapon().getBarrel().getPartName(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY())+TILE_SIZE);
        g2d.setColor(gameboard.getPlayer().getWeapon().getCore().getPartColor());
        g2d.drawString(gameboard.getPlayer().getWeapon().getCore().getPartName(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY())+TILE_SIZE*2);
        g2d.setColor(gameboard.getPlayer().getWeapon().getStock().getPartColor());
        g2d.drawString(gameboard.getPlayer().getWeapon().getStock().getPartName(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY())+ TILE_SIZE * 3);
        g2d.setColor(gameboard.getPlayer().getWeapon().getGrip().getPartColor());
        g2d.drawString(gameboard.getPlayer().getWeapon().getGrip().getPartName(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY()) + TILE_SIZE * 4);
        g2d.setColor(gameboard.getPlayer().getWeapon().getMag().getPartColor());
        g2d.drawString(gameboard.getPlayer().getWeapon().getMag().getPartName(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY()) + TILE_SIZE * 5);
        g2d.drawString("Powerlevel: " + gameboard.getPlayer().getWeapon().getPowerLevel(), (int)(BOTTOM_RIGHT_FIELD.getX()), (int)(BOTTOM_RIGHT_FIELD.getY())+ TILE_SIZE*6);
        
        return g2d;
    }
    
}
