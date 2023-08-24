package prototype.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import static prototype.utility.Constants.Fonts.FONT_HUGE;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.UI.*;
import static prototype.system.Prototype.gameboard;

/**
 *
 * @author Serbs
 */
public class GameOverMenu extends Menu{
    
    public GameOverMenu(){
        header="GAME OVER / LEVEL: " + gameboard.getPlayer().getLevel();
        buttons.add(new MenuButton(MENU_FIELD_POS1, "RESTART",0));
        buttons.add(new MenuButton(MENU_FIELD_POS2, "MAINMENU",0));
    }

    @Override
    public Graphics2D drawMenu(Graphics2D g2d) {
        g2d.setColor(Color.white);
                g2d.setFont(FONT_HUGE);
                header = "GAME OVER / LEVEL: " + gameboard.getPlayer().getLevel();
                g2d.drawString(header, (int) (TILE_SIZE), (int) (TILE_SIZE * 6));
                for (int i = 0; i < buttons.size(); i++) {
                    buttons.get(i).drawButton(g2d);
                }
                return g2d;
    }
    
}
