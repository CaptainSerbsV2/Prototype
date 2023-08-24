package prototype.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import prototype.system.GameLoop;
import prototype.system.Prototype;
import static prototype.utility.Constants.Fonts.*;
import static prototype.utility.Constants.System.*;
import static prototype.system.Prototype.camera;
import static prototype.system.Prototype.frame;
import static prototype.system.Prototype.gameovermenu;
import static prototype.system.Prototype.hud;
import static prototype.system.Prototype.mainmenu;
import static prototype.system.Prototype.pausemenu;
import static prototype.system.Prototype.settingsmenu;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.levelmanager;
import static prototype.system.Prototype.renderpanel;
import static prototype.utility.Constants.Colors.*;

/**
 *
 * @author Serbs
 */
public class RenderPanel extends JPanel {

        private boolean isRendering=false;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        public RenderPanel() {
            rh.add(new RenderingHints(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_ENABLE));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_SPEED));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_RESOLUTION_VARIANT,
                    RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_OFF));
            super.setBackground(Color.black);
            
        }
        
        public void setRenderingHints(RenderingHints renderSpeed, RenderingHints colorRenderSpeed, RenderingHints antiAliasing, 
                                        RenderingHints textAntiAliasing){
            
            rh.add(renderSpeed);
            rh.add(colorRenderSpeed);
            rh.add(antiAliasing);
            rh.add(textAntiAliasing);
            
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            render(g2d);
            
        }
        
        public Graphics2D render(Graphics2D g2d){
            isRendering=true;
            g2d.setRenderingHints(rh);
            
            g2d.scale(frame.getScalingX(), frame.getScalingY());
            AffineTransform still = g2d.getTransform();
            AffineTransform cameraTransform;

            switch (GameLoop.getCurrentGameState()) {
                case MAIN_MENU -> {
                    
                    g2d.setTransform(still);
                    mainmenu.drawMenu(g2d);
                    break;
                }
                case START_GAME -> {
                    
                    break;
                }
                case IN_GAME -> {
//                    super.setBackground(new Color(0,0,40));
                    //super.setBackground(new Color(LIGHT_COLOR_PALETTE[2]));
                    g2d.translate(camera.getX(), camera.getY());
                    cameraTransform = g2d.getTransform();
                    
                    levelmanager.render(g2d);
                    for(var blood:gameboard.getBloods()){
                        blood.drawBlood(g2d, cameraTransform);
                    }
                    
                    for(var bullet:gameboard.getBullets()){
                        bullet.render(g2d, cameraTransform);
                    }
                    for(var bullet:gameboard.getEnemyBullets()){
                        bullet.render(g2d, cameraTransform);
                    }
                    for(var enemy:gameboard.getEnemies()){
                        enemy.render(g2d, cameraTransform);
                    }
                    for(var lootDrop:gameboard.getLootDrops()){
                        lootDrop.drawLootDrop(g2d);
                    }
                    gameboard.getPlayer().render(g2d, cameraTransform);
                    g2d.setTransform(still);
                    hud.drawHUD(g2d);
                    break;
                }
                case GAME_OVER -> {
                    super.setBackground(Color.black);
                    g2d.translate(camera.getX(), camera.getY());
                    cameraTransform = g2d.getTransform();
                    g2d.setTransform(cameraTransform);
                    if(gameboard.getPlayer().isActive()){
                        gameboard.getPlayer().render(g2d, cameraTransform);
                    }
                    
                    for(var enemy:gameboard.getEnemies()){
                        if (enemy.isActive()) {
                            enemy.render(g2d, cameraTransform);
                        }
                    }

                    for(var lootDrop:gameboard.getLootDrops()){
                        lootDrop.drawLootDrop(g2d);
                    }
                    g2d.setTransform(still);
                    gameovermenu.drawMenu(g2d);
                    break;
                }
                case PAUSE_MENU -> {
                    super.setBackground(Color.black);
                    g2d.translate(camera.getX(), camera.getY());
                    cameraTransform = g2d.getTransform();
                    g2d.setTransform(cameraTransform);
                    levelmanager.render(g2d);
                    gameboard.getPlayer().render(g2d, cameraTransform);
                    for(var enemy:gameboard.getEnemies()){
                        if (enemy.isActive()) {
                            enemy.render(g2d, cameraTransform);
                        }
                    }
                    for(var lootDrop:gameboard.getLootDrops()){
                        lootDrop.drawLootDrop(g2d);
                    }
                    g2d.setTransform(still);
                    pausemenu.drawMenu(g2d);
                    break;
                }
                case SETTINGS->{
                    settingsmenu.drawMenu(g2d);
                    break;
                }
            }
            
            //Debug stuff
            if(GameLoop.isDebug()){
                g2d.setTransform(still);
//                g2d.setColor(Color.green);
//                g2d.setFont(FONT_MEDIUM);
//                g2d.drawString(GameLoop.getCounter(),TILE_SIZE , (int) (DEFAULT_FRAME_HEIGHT - (TILE_SIZE*1.25)));
                g2d.setColor(Color.red);
                g2d.drawString(GameLoop.getCurrentGameState()+"", (int) (TILE_SIZE * 15), (int) (DEFAULT_FRAME_HEIGHT - (TILE_SIZE*1.25)));
            }
            g2d.setColor(Color.green);
                g2d.setFont(FONT_MINISCULE);
                g2d.drawString(GameLoop.getCounter(),TILE_SIZE , (int) (DEFAULT_FRAME_HEIGHT - (TILE_SIZE*1.25)));
                isRendering=false;
            return g2d;
        }

        public Point getCurrentMouseLocation() {
            Point location;
            switch (GameLoop.getCurrentGameState()) {
                case IN_GAME ->{
                    location = new Point((int) ((MouseInfo.getPointerInfo().getLocation().getX()/frame.getScalingX() + camera.getX() * -1)),
                            (int) ((MouseInfo.getPointerInfo().getLocation().getY()/frame.getScalingY() + camera.getY() * -1)));
                }
                default ->
                    location = new Point((int)(MouseInfo.getPointerInfo().getLocation().getX()/frame.getScalingX()), (int)(MouseInfo.getPointerInfo().getLocation().getY()/frame.getScalingY()));
            }

            SwingUtilities.convertPointFromScreen(location, Prototype.frame);
            return location;
        }
        
        public Point getDistanceFromCenterScreen() {
            Point cursor = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(cursor, renderpanel);
            return new Point((int)(renderpanel.getBounds().getCenterX()-cursor.getX()), (int)(renderpanel.getBounds().getCenterY()-cursor.getY()));
        }
        
        public boolean isRendering(){
            return isRendering;
        }

    }
