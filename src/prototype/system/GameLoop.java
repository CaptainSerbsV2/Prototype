package prototype.system;

import java.awt.Dimension;
import static prototype.system.Prototype.camera;
import static prototype.system.Prototype.gameovermenu;
import static prototype.system.Prototype.mainmenu;
import static prototype.system.Prototype.pausemenu;
import static prototype.system.Prototype.settingsmenu;
import static prototype.system.Prototype.soundengine;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.soundengine;
import static prototype.system.Prototype.renderpanel;
import static prototype.utility.Constants.System.*;

/**
 *
 * @author Serbs
 */
// 60fps thread, that updates all the necessary things every frame based on gamestate.
public class GameLoop implements Runnable {

    public enum gameState {
        PAUSE_MENU,
        MAIN_MENU,
        START_GAME,
        IN_GAME,
        GAME_OVER,
        SETTINGS
    }
    private static gameState currentGameState = gameState.MAIN_MENU;
    private static boolean debug = false;
    private static int framesPerSecond = 60;
    private static double timePerFrame = 1000000000.0 / framesPerSecond;
    
    private static String counter = "init";

    @Override
    public void run() {

        double timePerUpdate = 1000000000.0 / UPDATES_PER_SECOND;
        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            
            if (deltaF >= 1) {
                renderpanel.repaint();
                
                frames++;
                deltaF--;
            }
            
            if (deltaU >= 1) {
                switch (currentGameState) {
                    case MAIN_MENU -> {
                        mainmenu.updateMenu();
                        break;
                    }

                    case PAUSE_MENU -> {
                        pausemenu.updateMenu();
                        break;
                    }
                    case IN_GAME -> {
                        gameboard.update();
                        levelmanager.update();
                        camera.update();
                        break;
                    }
                    case START_GAME -> {

                        break;
                    }
                    case GAME_OVER -> {
                        gameboard.update();
                        gameovermenu.updateMenu();
                        break;
                    }
                    case SETTINGS -> {
                        settingsmenu.updateMenu();
                        break;
                    }
                }
                soundengine.updateSoundEngine();
                updates++;
                deltaU--;
            }
            
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                counter = "FPS: " + frames + " | UPS: " + updates;
                frames = 0;
                updates = 0;
            }

        }
    }

    public static int getFPS() {
        return framesPerSecond;
    }

    public static void setFPS(int fps) {
        framesPerSecond = fps;
        timePerFrame = 1000000000.0 / framesPerSecond;
    }

    public static String getCounter() {
        return counter;
    }

    public static void setGameState(gameState state) {
        currentGameState = state;
    }

    public static gameState getCurrentGameState() {
        return currentGameState;
    }
    public static boolean isDebug(){
        return debug;
    }
    public static void setDebug(boolean d){
        debug=d;
    }
}
