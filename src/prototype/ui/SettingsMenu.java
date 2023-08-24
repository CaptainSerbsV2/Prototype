package prototype.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import static prototype.utility.Constants.Fonts.*;

import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.UI.*;
import static prototype.utility.Constants.Settings.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.frame;
import static prototype.system.Prototype.renderpanel;


/**
 *
 * @author imfai
 */
public class SettingsMenu extends Menu{
    
    private int renderSpeed=1;
    private int antiAliasing=0;
    private int resolution=15;
    private int fps=1;
    private String renderSpeedName;
    private String antiAliasingName;
    private String fpsName;
    private String resolutionName;
    

    public SettingsMenu(){
        header="SETTINGS";
        buttons.add(new MenuButton(MENU_FIELD_POS1, "APPLY",0));
        buttons.add(new MenuButton(MENU_FIELD_POS2, "CANCEL",0));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*2,(float)SETTINGS_FIELD.getY(),TILE_SIZE, TILE_SIZE), "ARROWLEFT", 1));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*5,(float)SETTINGS_FIELD.getY(),TILE_SIZE, TILE_SIZE), "ARROWRIGHT", 1));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*8,(float)SETTINGS_FIELD.getY()+TILE_SIZE,TILE_SIZE, TILE_SIZE), "ARROWLEFT", 2));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*12,(float)SETTINGS_FIELD.getY()+TILE_SIZE,TILE_SIZE, TILE_SIZE), "ARROWRIGHT", 2));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*6,(float)SETTINGS_FIELD.getY()+TILE_SIZE*2,TILE_SIZE, TILE_SIZE), "ARROWLEFT", 3));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*9,(float)SETTINGS_FIELD.getY()+TILE_SIZE*2,TILE_SIZE, TILE_SIZE), "ARROWRIGHT", 3));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*5,(float)SETTINGS_FIELD.getY()+TILE_SIZE*3,TILE_SIZE, TILE_SIZE), "ARROWLEFT", 4));
        buttons.add(new MenuButton(new Rectangle2D.Double((float)SETTINGS_FIELD.getX()+TILE_SIZE*11,(float)SETTINGS_FIELD.getY()+TILE_SIZE*3,TILE_SIZE, TILE_SIZE), "ARROWRIGHT", 4));
        antiAliasingName=SETTINGS_ANTIALIASING_NAME[antiAliasing];
        renderSpeedName=SETTINGS_RENDER_SPEED_NAME[renderSpeed];
        fpsName=""+SETTINGS_FPS[fps];
        resolutionName=""+SETTINGS_RESOLUTION[resolution].getWidth()+"x"+SETTINGS_RESOLUTION[resolution].getHeight();
    }
    @Override
    public void updateMenu(){
        renderSpeedName=SETTINGS_RENDER_SPEED_NAME[renderSpeed];
        antiAliasingName=SETTINGS_ANTIALIASING_NAME[antiAliasing];
        resolutionName=""+(int)SETTINGS_RESOLUTION[resolution].getWidth()+"x"+(int)SETTINGS_RESOLUTION[resolution].getHeight();
        fpsName=""+SETTINGS_FPS[fps];
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).updateButton();
        }
    }
    
    @Override
    public Graphics2D drawMenu(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setFont(FONT_HUGE);
        g2d.drawString(header, TILE_SIZE, TILE_SIZE * 5);
        g2d.setFont(FONT_SMALL);
        g2d.drawString("FPS:", (float)SETTINGS_FIELD.getX(), (float)SETTINGS_FIELD.getY()+TILE_SIZE);
        g2d.drawString(fpsName, (float)SETTINGS_FIELD.getX()+TILE_SIZE*3, (float)SETTINGS_FIELD.getY()+TILE_SIZE);
        g2d.drawString("Render speed pref:", (float)SETTINGS_FIELD.getX(), (float)SETTINGS_FIELD.getY()+TILE_SIZE*2);
        g2d.drawString(renderSpeedName, (float)SETTINGS_FIELD.getX()+TILE_SIZE*9, (float)SETTINGS_FIELD.getY()+TILE_SIZE*2);
        g2d.drawString("Antialiasing:", (float)SETTINGS_FIELD.getX(), (float)SETTINGS_FIELD.getY()+TILE_SIZE*3);
        g2d.drawString(antiAliasingName, (float)SETTINGS_FIELD.getX()+TILE_SIZE*7, (float)SETTINGS_FIELD.getY()+TILE_SIZE*3);
        g2d.drawString("Resolution:", (float)SETTINGS_FIELD.getX(), (float)SETTINGS_FIELD.getY()+TILE_SIZE*4);
        g2d.drawString(resolutionName, (float)SETTINGS_FIELD.getX()+TILE_SIZE*6, (float)SETTINGS_FIELD.getY()+TILE_SIZE*4);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).drawButton(g2d);
        }
        return g2d;
    }
    public void applySettings(){
        GameLoop.setFPS(SETTINGS_FPS[fps]);
        renderpanel.setRenderingHints(SETTINGS_RENDER_SPEED[renderSpeed], SETTINGS_COLOR_RENDER_SPEED[renderSpeed], 
                                        SETTINGS_ANTIALIASING[antiAliasing], SETTINGS_TEXT_ANTIALIASING[antiAliasing]);
        frame.setFrameSize(SETTINGS_RESOLUTION[resolution]);
    }
    public void setSettings(int leftright, int settings){
        switch(leftright){
            case 0->{
                switch (settings){
                    case 1 -> {
                        if(fps>0){
                            fps--;
                        }
                    }
                    case 2 -> {
                        if(renderSpeed>0){
                            renderSpeed--;
                        }
                    }
                    case 3 -> {
                        if(antiAliasing>0){
                            antiAliasing--;
                        }
                    }
                    case 4 -> {
                        if(resolution>0){
                            resolution--;
                        }
                    }
                }
            }
            case 1->{
                switch (settings){
                    case 1 -> {
                        if(fps<3){
                            fps++;
                        }      
                    }
                    case 2 -> {
                        if(renderSpeed<1){
                            renderSpeed++;
                        }      
                    }
                    case 3 -> {
                        if(antiAliasing<1){
                            antiAliasing++;
                        }      
                    }
                    case 4 -> {
                        if(resolution<15){
                            resolution++;
                        }      
                    }
                }
            }
            
        }
    }
    
}
