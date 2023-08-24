package prototype.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import static prototype.utility.Constants.Fonts.*;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.Colors.*;
import static prototype.utility.Constants.UI.*;
import static prototype.system.Prototype.frame;
import static prototype.system.Prototype.gameboard;

/**
 *
 * @author Serbs
 */
public class PauseMenu extends Menu{
    public PauseMenu(){
        header="PAUSE";
        buttons.add(new MenuButton(MENU_FIELD_POS1, "CONTINUE",0));
        buttons.add(new MenuButton(MENU_FIELD_POS2, "RESTART",0));
        buttons.add(new MenuButton(MENU_FIELD_POS3, "MAINMENU",0));
    }
            

    @Override
    public Graphics2D drawMenu(Graphics2D g2d) {
        g2d.setColor(DARK_TRANSPARENT);
        g2d.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g2d.setColor(Color.white);
        g2d.setFont(FONT_HUGE);
        g2d.drawString(header, TILE_SIZE, TILE_SIZE * 5);
        g2d.setFont(FONT_SMALL);
        g2d.drawString("WEAPON STATS:", (int) STAT_FIELD.getX(), (int) (STAT_FIELD.getMinY() + TILE_SIZE));
        g2d.setFont(FONT_MINISCULE);
        double y = STAT_FIELD.getMinY() + TILE_SIZE * 2;
        for (int i = 0; i < gameboard.getPlayer().getWeapon().getWeaponStats().size(); i++) {
            g2d.drawString(gameboard.getPlayer().getWeapon().getWeaponStatNames().get(i) + ": " + gameboard.getPlayer().getWeapon().getWeaponStats().get(i),
                    (int) STAT_FIELD.getX(), (int) y);
            y += TILE_SIZE;
        }
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).drawButton(g2d);
        }
        return g2d;
    }
    
}
