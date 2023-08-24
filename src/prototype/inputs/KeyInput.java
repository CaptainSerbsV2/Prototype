
package prototype.inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import prototype.system.GameLoop;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.soundengine;

/**
 *
 * @author imfai
 */
public class KeyInput extends KeyAdapter {

        @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (GameLoop.getCurrentGameState()) {
            case IN_GAME -> {
                switch (key) {
                    case KeyEvent.VK_A ->
                        gameboard.getPlayer().setMovementBoolean(1, true);
                    case KeyEvent.VK_D ->
                        gameboard.getPlayer().setMovementBoolean(3, true);
                    case KeyEvent.VK_W ->
                        gameboard.getPlayer().setMovementBoolean(2, true);
                    case KeyEvent.VK_S ->
                        gameboard.getPlayer().setMovementBoolean(4, true);
                    case KeyEvent.VK_R ->
                        gameboard.getPlayer().getWeapon().Reload();
                    case KeyEvent.VK_E ->
                        gameboard.getPlayer().pickUp(true);
                    case KeyEvent.VK_SPACE ->
                        gameboard.getPlayer().Ability(true);
                    case KeyEvent.VK_ESCAPE -> {
                        gameboard.getPlayer().resetInputActions();
                        GameLoop.setGameState(GameLoop.gameState.PAUSE_MENU);
                    }
                    case KeyEvent.VK_ENTER ->
                        System.exit(1);
                    case KeyEvent.VK_F12 -> {
                        if(GameLoop.isDebug()){
                            GameLoop.setDebug(false);
                        }else{
                            GameLoop.setDebug(true);
                        }
                    }
                }
            }
            case PAUSE_MENU ->{
                switch (key) {
                    case KeyEvent.VK_ESCAPE -> 
                        GameLoop.setGameState(GameLoop.gameState.IN_GAME);
                    case KeyEvent.VK_ENTER -> 
                        System.exit(1);
                }
            }
            case GAME_OVER ->{
                switch (key) {
                    case KeyEvent.VK_ESCAPE -> 
                        GameLoop.setGameState(GameLoop.gameState.MAIN_MENU);
                    case KeyEvent.VK_ENTER -> 
                        System.exit(1);
                }
            }
            case MAIN_MENU ->{
                switch (key) {
                    case KeyEvent.VK_ESCAPE -> 
                        System.exit(1);
                    case KeyEvent.VK_ENTER -> {
                        gameboard.Restart();
                        soundengine.setSoundtrackInGame();
                    }
                        
                }
            }
        }
    }
    
    

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (GameLoop.getCurrentGameState()) {
            case IN_GAME -> {
                switch (key) {
                    case KeyEvent.VK_A ->
                        gameboard.getPlayer().setMovementBoolean(1, false);
                    case KeyEvent.VK_D ->
                        gameboard.getPlayer().setMovementBoolean(3, false);
                    case KeyEvent.VK_W ->
                        gameboard.getPlayer().setMovementBoolean(2, false);
                    case KeyEvent.VK_S ->
                        gameboard.getPlayer().setMovementBoolean(4, false);
                    case KeyEvent.VK_E ->
                        gameboard.getPlayer().pickUp(false);
                    case KeyEvent.VK_SPACE ->
                        gameboard.getPlayer().Ability(false);
                }
            }


        }
    }
    }
