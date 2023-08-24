package prototype.system;

import prototype.display.Camera;
import prototype.display.RenderPanel;
import prototype.utility.Constants;
import prototype.levels.LevelManager;
import prototype.ui.*;

/**
 *
 * @author Serbs
 */
public class Prototype {

    public static GameFrame frame;
    public static Thread gameloop;
    public static RenderPanel renderpanel;

    public static Camera camera;
    public static GameBoard gameboard;
    public static LevelManager levelmanager;
    public static SoundEngine soundengine;
    public static MainMenu mainmenu;
    public static PauseMenu pausemenu;
    public static GameOverMenu gameovermenu;
    public static SettingsMenu settingsmenu;
    public static HeadsUpDisplay hud;

    //Initialize program.
    public Prototype() { 
        frame = new GameFrame();
        gameloop = new Thread(new GameLoop());
        levelmanager = new LevelManager();
        camera = new Camera();
        gameboard = new GameBoard();
        renderpanel = new RenderPanel();
        frame.add(renderpanel);
        mainmenu = new MainMenu();
        pausemenu = new PauseMenu();
        gameovermenu = new GameOverMenu();
        settingsmenu = new SettingsMenu();
        hud = new HeadsUpDisplay();
        soundengine = new SoundEngine();
        soundengine.playSoundtrack();
        soundengine.playSFX(Constants.Audio.SFX.START);
        soundengine.playSFX(Constants.Audio.SFX.HIT);
        soundengine.setSFXVolume(-10.0f);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Prototype prototype = new Prototype();
        gameloop.start();
        

    }
}
