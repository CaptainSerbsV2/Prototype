package prototype.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import prototype.utility.Constants;
import static prototype.utility.Constants.System.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.settingsmenu;
import static prototype.system.Prototype.soundengine;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.renderpanel;

/**
 *
 * @author Serbs
 */
public class MenuButton {
    public enum buttonAction{
        CONTINUE,
        RESTART,
        MAINMENU,
        START,
        EXIT,
        SETTINGS,
        APPLY,
        CANCEL,
        ARROWLEFT,
        ARROWRIGHT
    }
    private final Rectangle2D bounds;
    private final buttonAction action;
    private final String name;
    private boolean mouseEntered;
    private Color color;
    private int optional;
    
    public MenuButton(Rectangle2D bounds, String n, int optional){
        this.bounds = bounds;
        this.optional = optional;
        name = n;
        action = buttonAction.valueOf(name);
        color = Color.white;
    }
    
    public void updateButton(){
        mouseEntered = bounds.contains(renderpanel.getCurrentMouseLocation());
        if(mouseEntered){
            color = Color.gray;
        }else{
            color = Color.white;
        }
    }
    
    public Graphics2D drawButton(Graphics2D g2d){
        g2d.setColor(color);
        g2d.setFont(Constants.Fonts.FONT_MEDIUM);
        switch(action){
            case ARROWLEFT->{
                g2d.setFont(Constants.Fonts.FONT_SMALL);
                g2d.drawString("<", (float)bounds.getX(), (float)bounds.getMaxY());
            }
            case ARROWRIGHT->{
                g2d.setFont(Constants.Fonts.FONT_SMALL);
                g2d.drawString(">", (float)bounds.getX(), (float)bounds.getMaxY());
            }
            default->g2d.drawString(name, (float)bounds.getX(), (float)bounds.getMaxY());
        }

        return g2d;
    }
    
    public boolean getMouseEntered(){
        return mouseEntered;
    }
    public void doAction(){
        switch(action){
            case CONTINUE -> GameLoop.setGameState(GameLoop.gameState.IN_GAME);
            case RESTART -> {
                gameboard.Restart();
            }
            case MAINMENU -> {
                GameLoop.setGameState(GameLoop.gameState.MAIN_MENU);
                soundengine.setSoundtrackMainMenu();
            }
                    
            case START -> {
                gameboard.Restart();
                soundengine.setSoundtrackInGame();
                
            }
                
            case EXIT -> System.exit(0);
            case SETTINGS -> GameLoop.setGameState(GameLoop.gameState.SETTINGS);
            case APPLY ->{
                settingsmenu.applySettings();
                GameLoop.setGameState(GameLoop.gameState.MAIN_MENU);
                break;
            }
            case CANCEL -> GameLoop.setGameState(GameLoop.gameState.MAIN_MENU);
            case ARROWLEFT ->{
                settingsmenu.setSettings(0, optional);
            }
            case ARROWRIGHT ->{
                settingsmenu.setSettings(1, optional);
            }
            
            default -> System.out.println("RIP");
        }
        mouseEntered=false;
    }
    
}
