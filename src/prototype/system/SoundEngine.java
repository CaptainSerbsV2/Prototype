package prototype.system;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;
import static prototype.system.GameLoop.*;
import static prototype.utility.LoadSave.*;
import static prototype.utility.Constants.Audio.*;
import static prototype.utility.Constants.System.UPDATES_PER_SECOND;

/**
 *
 * @author Serbs
 */
public class SoundEngine {

    
    private static float sfxVolumeValue;
    private static float musicVolumeValue;
    
    private static int hitBuffer;
    
    private AudioInputStream soundtrackStream;
    private Clip soundtrackClip;

    public SoundEngine() {
        initSoundEngine();
    }

    private void initSoundEngine() {
        sfxVolumeValue = -1000.0f;
        musicVolumeValue = -10.0f;
        hitBuffer=0;
    }
    
    public void updateSoundEngine(){
        if(hitBuffer>0){
            hitBuffer--;
        }
    }

    public void playSoundtrack() {   
        try {
            soundtrackClip = AudioSystem.getClip();
            soundtrackStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource("resources/AmbientElectricSoundtrack.wav"));
            soundtrackClip.open(soundtrackStream);
            
            FloatControl testVolume = (FloatControl) soundtrackClip.getControl(FloatControl.Type.MASTER_GAIN);
            testVolume.setValue(musicVolumeValue);
                    
            soundtrackClip.setLoopPoints(0, 635039);
            soundtrackClip.loop(LOOP_CONTINUOUSLY);
            
            
            soundtrackClip.start();
            soundtrackClip.drain();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, e);
            
        }
        
    }
    public void setSoundtrackGameOver(){
        soundtrackClip.setLoopPoints(5397840, 6032879);                   
        soundtrackClip.setFramePosition(4445281);
    }
    
    public void setSoundtrackInGame(){
        soundtrackClip.setFramePosition(635040);
        soundtrackClip.setLoopPoints(635040, 4445280);                  
    }
    public void setSoundtrackMainMenu(){
        soundtrackClip.setLoopPoints(0, 635039);   
        soundtrackClip.setFramePosition(0);
    }

    public void playSFX(SFX sfx) {
        switch(sfx){
            // Separate method for HIT SFX, so you won't get blasted by sfx possibly happening every tick and multiple at a time.
            case HIT ->{
                if(hitBuffer<=0){
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(loadAudioInputStream(sfx));

                        FloatControl testVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        testVolume.setValue(sfxVolumeValue);
                        clip.start();
                        clip.addLineListener(new LineListener() {
                            @Override
                            public void update(LineEvent event) {
                                if (event.getType() == LineEvent.Type.CLOSE) {
                                    clip.close();
                                }
                            }
                        });
                        clip.drain();
                        hitBuffer=(int) (0.2*UPDATES_PER_SECOND);
                    } catch (LineUnavailableException | IOException e) {
                        Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }default ->{
                try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(loadAudioInputStream(sfx));

                        FloatControl testVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        testVolume.setValue(sfxVolumeValue);
                        clip.start();
                        clip.addLineListener(new LineListener() {
                            @Override
                            public void update(LineEvent event) {
                                if (event.getType() == LineEvent.Type.CLOSE) {
                                    clip.close();
                                }
                            }
                        });
                        clip.drain();

                    } catch (LineUnavailableException | IOException e) {
                        Logger.getLogger(SoundEngine.class.getName()).log(Level.SEVERE, null, e);
                    }
            }
        }
    }

    public void setSFXVolume(float volume) {
        sfxVolumeValue = volume;
    }
}
