package prototype.ui;
import java.awt.Color;
import java.awt.Graphics2D;
import static prototype.utility.Constants.Fonts.*;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.UI.*;
import static prototype.utility.Constants.System.DEFAULT_FRAME_WIDTH;
import static prototype.system.Prototype.frame;

/**
 *
 * @author Serbs
 */
public class MainMenu extends Menu{
    
    public MainMenu(){
        header="PROTOTYPE.EXE";
        buttons.add(new MenuButton(MENU_FIELD_POS1, "START",0));
        buttons.add(new MenuButton(MENU_FIELD_POS2, "SETTINGS",0));
        buttons.add(new MenuButton(MENU_FIELD_POS3, "EXIT",0));
    }
    
    @Override
    public Graphics2D drawMenu(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.setFont(FONT_HUGE);
        g2d.drawString(header, TILE_SIZE, TILE_SIZE * 5);
        g2d.setFont(FONT_MINISCULE);
        g2d.drawString("WASD = MOVEMENT", DEFAULT_FRAME_WIDTH-TILE_SIZE*10,TILE_SIZE);
        g2d.drawString("SPACE = DODGE", DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*2);
        g2d.drawString("R = RELOAD", DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*3);
        g2d.drawString("E = PICK UP",DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*4);
        g2d.drawString("MOUSE1 = SHOOT", DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*5);
        g2d.drawString("ESC (INGAME) = PAUSE", DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*6);
        g2d.drawString("ENTER = START(MAIN MENU) / FORCE PROGRAM SHUTDOWN :D", DEFAULT_FRAME_WIDTH-TILE_SIZE*10, TILE_SIZE*7);
        g2d.setFont(FONT_SMALL);
        g2d.drawString(VERSION_NUMBER, TILE_SIZE *25, TILE_SIZE * 5);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).drawButton(g2d);
        }
        return g2d;
    }
    
}
