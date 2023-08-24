/*
 * Copyright (C) 2023 Sere Kuusitaival
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package prototype.system;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;
import prototype.inputs.KeyInput;
import prototype.inputs.MouseInput;
import static prototype.utility.Constants.System.*;
import prototype.system.GameLoop.gameState;
import static prototype.system.Prototype.gameboard;

/**
 *
 * @author Serbs
 */
public class GameFrame extends JFrame {
    public static Dimension PREFERRED_SIZE = /*new Dimension(1980,500);//*/new Dimension(DEVICE_SCREEN_SIZE.width, DEVICE_SCREEN_SIZE.height);
    
    public static int frameWidth = PREFERRED_SIZE.width;
    public static int frameHeight= PREFERRED_SIZE.height;
    
    public GameFrame(){
        this.dispose();
        this.setPreferredSize(PREFERRED_SIZE);
        this.setTitle("Prototype");
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(1, 1, 0, 0));
        this.setCursor(CROSSHAIR_CURSOR);
        this.addKeyListener(new KeyInput());
        this.addMouseListener(new MouseInput());
        this.setFocusable(true);
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gameboard.getPlayer().resetInputActions();
                if (GameLoop.getCurrentGameState() == gameState.IN_GAME) {
                    GameLoop.setGameState(gameState.PAUSE_MENU);
                }
            }

        });
        this.pack();
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        
    }
    
    public float getScalingX(){
        frameWidth=this.getSize().width;
        return frameWidth/DEFAULT_FRAME_WIDTH;   
    }
    public float getScalingY(){
        frameHeight=this.getSize().height;
        return frameHeight/DEFAULT_FRAME_HEIGHT;  
    }
    
   
    public void setFrameSize(Dimension d){
        PREFERRED_SIZE = d;
    
        frameWidth = PREFERRED_SIZE.width;
        frameHeight= PREFERRED_SIZE.height;
        this.setSize(d);
        this.setLocationRelativeTo(null);
        
    }
    
    
}
