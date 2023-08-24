package prototype.inputs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import prototype.system.GameLoop;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.gameovermenu;
import static prototype.system.Prototype.mainmenu;
import static prototype.system.Prototype.pausemenu;
import static prototype.system.Prototype.settingsmenu;

/**
 *
 * @author Serbs
 */
public class MouseInput extends MouseAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        int mbutton = e.getButton();
        switch (GameLoop.getCurrentGameState()) {
            case IN_GAME -> {
                if (mbutton == MouseEvent.BUTTON1) {
                    gameboard.getPlayer().getWeapon().Fire(true);
                }
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int mbutton = e.getButton();
        switch (GameLoop.getCurrentGameState()) {
            case IN_GAME -> {
                if (mbutton == MouseEvent.BUTTON1) {
                    gameboard.getPlayer().getWeapon().Fire(false);
                }
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mbutton = e.getButton();
        switch (GameLoop.getCurrentGameState()) {
            case MAIN_MENU->{
                for (int i = 0; i < mainmenu.getButtons().size(); i++) {
                    if (mainmenu.getButtons().get(i).getMouseEntered()) {
                        mainmenu.getButtons().get(i).doAction();
                        break;
                    }
                }
                    
            }
            case PAUSE_MENU ->{
                for (int i = 0; i < pausemenu.getButtons().size(); i++) {
                    if (pausemenu.getButtons().get(i).getMouseEntered()) {
                        pausemenu.getButtons().get(i).doAction();
                        break;
                    }
                }
                    
            }
            case GAME_OVER ->{
                for (int i = 0; i < gameovermenu.getButtons().size(); i++) {
                    if (gameovermenu.getButtons().get(i).getMouseEntered()) {
                        gameovermenu.getButtons().get(i).doAction();
                        break;
                    }
                }
                    
            }
            case SETTINGS ->{
                for (int i = 0; i < settingsmenu.getButtons().size(); i++) {
                    if (settingsmenu.getButtons().get(i).getMouseEntered()) {
                        settingsmenu.getButtons().get(i).doAction();
                        break;
                    }
                }
                    
            }
        }
        
    }

}
