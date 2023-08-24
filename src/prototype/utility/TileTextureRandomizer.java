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
import java.awt.image.BufferedImage;
import static prototype.utility.Constants.Animations.getAnimation;
import static prototype.utility.Constants.Colors.*;

/**
 *
 * @author Sere Kuusitaival
 */
public class TileTextureRandomizer {
    private static double blackOrFloor;
    private static double blackOrWall;
    private static double wall;
    private static double floor;
    private static double lightor;
    
    public static BufferedImage Randomize(int textureID){
            
           BufferedImage target = getAnimation(Constants.Animations.animation.LEVEL_TILES_TEMPLATE)[textureID];
            BufferedImage image = new BufferedImage(16 , 16, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < 16 ; i++) {
                for (int j = 0; j < 16; j++) {
                    Color newColor = null;
                    
                    Color templateColor = new Color(target.getRGB(i, j));
                    blackOrFloor = Math.random();
                    blackOrWall = Math.random();
                    wall = Math.random();
                    lightor = Math.random();
                    floor = Math.random();
                    switch (templateColor.getRGB()) {

                        //RED 
                        case (-65536) -> {
                            if(lightor<=0.9){
                                if (wall <= 0.25) {
                                    newColor = DARK_COLOR_PALETTE[2];
                                } else if (wall<=0.5){
                                    newColor = DARK_COLOR_PALETTE[3];
                                }else if(wall<=0.75){
                                    newColor = DARK_COLOR_PALETTE[0];
                                }else{
                                    newColor = DARK_COLOR_PALETTE[1];
                                }
                            }else{
                                if (wall <= 0.25) {
                                    newColor = LIGHT_COLOR_PALETTE[2];
                                } else if (wall <= 0.5) {
                                    newColor = LIGHT_COLOR_PALETTE[3];
                                } else if (wall <= 0.75) {
                                    newColor = LIGHT_COLOR_PALETTE[0];
                                } else {
                                    newColor = LIGHT_COLOR_PALETTE[1];
                                }
                            }
                            

                        }
                        //DARK RED
                         case (-6946816) -> {
                            
                           if(blackOrWall>=0.5){
                                if(lightor<=0.9){
                                    if (wall <= 0.25) {
                                        newColor = LIGHT_COLOR_PALETTE[2];
                                    } else if (wall <= 0.5) {
                                        newColor = LIGHT_COLOR_PALETTE[3];
                                    } else if (wall <= 0.75) {
                                        newColor = LIGHT_COLOR_PALETTE[0];
                                    } else {
                                        newColor = LIGHT_COLOR_PALETTE[1];
                                    }
                                }else{
                                    if (wall <= 0.25) {
                                        newColor = DARK_COLOR_PALETTE[2];
                                    } else if (wall <= 0.5) {
                                        newColor = DARK_COLOR_PALETTE[3];
                                    } else if (wall <= 0.75) {
                                        newColor = DARK_COLOR_PALETTE[0];
                                    } else {
                                        newColor = DARK_COLOR_PALETTE[1];
                                    }
                                }
                           }else{
                               newColor = Color.black;
                           }

                        }
                        //GREEN
                        case (-16711936) -> {
                            if (lightor <= 0.1) {
                                if (floor <= 0.25) {
                                    newColor = DARK_COLOR_PALETTE[2];
                                } else if (floor <= 0.5) {
                                    newColor = DARK_COLOR_PALETTE[3];
                                } else if (floor <= 0.75) {
                                    newColor = DARK_COLOR_PALETTE[0];
                                } else {
                                    newColor = DARK_COLOR_PALETTE[1];
                                }
                            } else {
                                if (floor <= 0.25) {
                                    newColor = LIGHT_COLOR_PALETTE[2];
                                } else if (floor <= 0.5) {
                                    newColor = LIGHT_COLOR_PALETTE[3];
                                } else if (floor <= 0.75) {
                                    newColor = LIGHT_COLOR_PALETTE[0];
                                } else {
                                    newColor = LIGHT_COLOR_PALETTE[1];
                                }
                            }
                            
                        }
                        //DARK GREEN
                        case (-16738816) -> {
                            
                           if(blackOrFloor>=0.5){
                               if (lightor <= 0.1) {
                                   if (floor <= 0.25) {
                                       newColor = DARK_COLOR_PALETTE[2];
                                   } else if (floor <= 0.5) {
                                       newColor = DARK_COLOR_PALETTE[3];
                                   } else if (floor <= 0.75) {
                                       newColor = DARK_COLOR_PALETTE[0];
                                   } else {
                                       newColor = DARK_COLOR_PALETTE[1];
                                   }
                               } else {
                                   if (floor <= 0.25) {
                                       newColor = LIGHT_COLOR_PALETTE[2];
                                   } else if (floor <= 0.5) {
                                       newColor = LIGHT_COLOR_PALETTE[3];
                                   } else if (floor <= 0.75) {
                                       newColor = LIGHT_COLOR_PALETTE[0];
                                   } else {
                                       newColor = LIGHT_COLOR_PALETTE[1];
                                   }
                               }
                           }else{
                               newColor = Color.black;
                           }

                        }
                        //BLACK
                        case (-16777216) -> {
                            newColor = new Color(0, 0, 0);
                        }
                        default -> {
                            newColor = new Color(255, 255, 255);
                        }

                    }
                    image.setRGB(i, j, newColor.getRGB());
                } 
            }
            return image;
        }
    
}
