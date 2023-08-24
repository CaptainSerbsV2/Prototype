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
package prototype.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static prototype.utility.Constants.Colors.*;

/**
 *
 * @author Sere Kuusitaival
 */
public class SpriteRandomizer {
    private static double blackOrBlank;
    private static double blackOrTexture;
    private static double texture;
    private static double lightor;
    
    public static BufferedImage Randomize(BufferedImage template){
            
        
          // BufferedImage target = getAnimation(Constants.Animations.animation.PLAYER_TEMPLATE)[0];
           BufferedImage image = new BufferedImage(template.getWidth(), template.getHeight(), template.getType());
            Graphics2D g = image.createGraphics();
            g.drawImage(template, 0, 0, null);
            g.dispose();
            for (int i = 0; i < 16 ; i++) {
                for (int j = 0; j < 16; j++) {
                    Color newColor = null;
                    
                    Color templateColor = new Color(template.getRGB(i, j));
                    blackOrBlank = Math.random();
                    blackOrTexture = Math.random();
                    texture = Math.random();
                    lightor = Math.random();
                    boolean skip = false;
                    switch (templateColor.getRGB()) {

                        //RED 
                        case (-65536) -> {
                            if (lightor <= 0.6) {
                                if (texture <= 0.25) {
                                    newColor = DARK_COLOR_PALETTE[2];
                                } else if (texture <= 0.5) {
                                    newColor = DARK_COLOR_PALETTE[3];
                                } else if (texture <= 0.75) {
                                    newColor = DARK_COLOR_PALETTE[0];
                                } else {
                                    newColor = DARK_COLOR_PALETTE[1];
                                }
                            } else {
                                if (texture <= 0.25) {
                                    newColor = LIGHT_COLOR_PALETTE[2];
                                } else if (texture <= 0.5) {
                                    newColor = LIGHT_COLOR_PALETTE[3];
                                } else if (texture <= 0.75) {
                                    newColor = LIGHT_COLOR_PALETTE[0];
                                } else {
                                    newColor = LIGHT_COLOR_PALETTE[1];
                                }
                            }
                        }
                        //DARK RED
                        case (-6946816) -> {                          
                            if (texture <= 0.25) {
                                newColor = DARK_COLOR_PALETTE[2];
                            } else if (texture <= 0.5) {
                                newColor = DARK_COLOR_PALETTE[3];
                            } else if (texture <= 0.75) {
                                newColor = DARK_COLOR_PALETTE[0];
                            } else {
                                newColor = DARK_COLOR_PALETTE[1];
                            }
                        }
                         
                         //BLUE
                         case (-16776961) -> {
                           if(blackOrBlank>=0.7){
                               if (texture <= 0.25) {
                                   newColor = DARK_COLOR_PALETTE[2];
                               } else if (texture <= 0.5) {
                                   newColor = DARK_COLOR_PALETTE[3];
                               } else if (texture <= 0.75) {
                                   newColor = DARK_COLOR_PALETTE[0];
                               } else {
                                   newColor = DARK_COLOR_PALETTE[1];
                               }
                           }else{
                               newColor = new Color(0,0,0,0);
                           }
                           break;

                        }
                        //BLACK
                        case (-16777216) -> {
                                skip=true;
                            break;
                        }
                        default -> {
                            skip=true;
                            break;
                        }
                            
                    }
                    if(!skip){
                        image.setRGB(i, j, newColor.getRGB());
                    }
                    
                } 
            }
            return image;
        }
    
}
