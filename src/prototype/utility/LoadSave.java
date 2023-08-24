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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import prototype.gameobjects.enemies.Brute;
import prototype.system.SoundEngine;
import prototype.utility.Constants.Audio.SFX;

/**
 *
 * @author Sere Kuusitaival
 */
public class LoadSave {
    
    public static BufferedImage loadSpriteSheet(String name){
            BufferedImage sheet=null;
            InputStream is = ClassLoader.getSystemResourceAsStream("resources/spritesheets/" + name +"_Spritesheet.png");

            try {
                sheet=ImageIO.read(is);
            }catch(Throwable throwable) { //Failsafe that loads a default texture anim if the above path fails.
                is = ClassLoader.getSystemResourceAsStream("resources/spritesheets/DEFAULT_TEXTURE_Spritesheet.png");
                    try {
                        sheet=ImageIO.read(is);
                    }catch (IOException ex) {               
                        Logger.getLogger(Brute.class.getName()).log(Level.SEVERE, null, ex);
                    }               
                Logger.getLogger(Brute.class.getName()).log(Level.SEVERE, null, throwable);
            }finally{
                try{
                    is.close();
                }catch(IOException ex){
                    
                    Logger.getLogger(Brute.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return sheet;
        }
    
    public static AudioInputStream loadAudioInputStream(SFX sfx){
            AudioInputStream sfxStream = null;
           try {
               sfxStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource("resources/"+sfx+"_SFX.wav"));
               
           }catch(UnsupportedAudioFileException | IOException e) {
               Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, e);
           }
           return sfxStream;
        }
    
}
