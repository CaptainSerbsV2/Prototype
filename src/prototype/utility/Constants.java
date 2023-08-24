package prototype.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import prototype.utility.Constants.Animations.animation;
import static prototype.utility.Constants.Animations.getAnimation;
import static prototype.utility.Constants.Colors.BRUTE_COLOR_PALETTE;
import static prototype.utility.Constants.System.*;
import static prototype.utility.LoadSave.loadSpriteSheet;


/**
 *
 * @author Serbs
 */
public class Constants {
    
    public static class System{
        
        public static final Dimension DEVICE_SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
        
        public static final int DEFAULT_FRAME_WIDTH = 640;
        public static final int DEFAULT_FRAME_HEIGHT = 360;
        
        
        public static final double PIXEL_SIZE=1;
        public static final int TILE_SIZE =16; 
        
        public static final int UPDATES_PER_SECOND = 240;
    }
    
    public static class Fonts{
        public static final Font FONT_MINISCULE = new Font("Impact", Font.ITALIC, (int) (8));
        public static final Font FONT_SMALL = new Font("Impact", Font.ITALIC, (int) (16));
        public static final Font FONT_MEDIUM = new Font("Impact", Font.ITALIC, (int) (32));
        public static final Font FONT_LARGE = new Font("Impact", Font.ITALIC, (int) (48));
        public static final Font FONT_HUGE = new Font("Impact", Font.ITALIC, (int) (64));
    }
    
    public static class Colors{
        public static final Color DARK_TRANSPARENT = new Color(0, 0, 0, 100);
        public static final int[] TILE_COLOR_PALETTE = {7237270  ,5921410, 3289690, 1973830};
        public static final int[] BRUTE_COLOR_PALETTE = {5186629  ,7546721, 10233727, 12525463};
        public static final int[] GUNNER_COLOR_PALETTE = {3480391  ,5254249, 4010134, 8597690};
        public static final int[] CRUSHER_COLOR_PALETTE = {592182  ,2434388, 2960768, 3026608};
        public static final int[] PLAYER_COLOR_PALETTE = {1063239  ,2972523, 3439766, 2595524};
        public static final Color[] DARK_COLOR_PALETTE={new Color((int)(Math.random()*100+20),(int)(Math.random()*100+20) , (int)(Math.random()*100+20)), new Color((int)(Math.random()*100+20),(int)(Math.random()*100+20) , (int)(Math.random()*100+20)),
        new Color((int)(Math.random()*100+20),(int)(Math.random()*100+20) , (int)(Math.random()*100+20)),new Color((int)(Math.random()*100+20),(int)(Math.random()*100+20) , (int)(Math.random()*100+20))};//{1968670  ,1446420, 659225, 0};
        public static final Color[] LIGHT_COLOR_PALETTE={new Color((int)(Math.random()*115+140),(int)(Math.random()*115+140) , (int)(Math.random()*115+140)), new Color((int)(Math.random()*115+140),(int)(Math.random()*115+140) , (int)(Math.random()*115+140)),
        new Color((int)(Math.random()*115+140),(int)(Math.random()*115+140) , (int)(Math.random()*115+140)),new Color((int)(Math.random()*115+140),(int)(Math.random()*115+140) , (int)(Math.random()*115+140))};//{16742655  ,16755400, 9874145, 16777185};
    }
    
    public static class UI{
        public static final Rectangle2D TOP_LEFT_FIELD= new Rectangle2D.Double(0, 0, TILE_SIZE * 6, TILE_SIZE * 2);
        public static final Rectangle2D TOP_RIGHT_FIELD= new Rectangle2D.Double(DEFAULT_FRAME_WIDTH-TILE_SIZE * 6, 0, TILE_SIZE * 6, TILE_SIZE * 3);
        public static final Rectangle2D BOTTOM_RIGHT_FIELD= new Rectangle2D.Double(DEFAULT_FRAME_WIDTH-TILE_SIZE * 8, DEFAULT_FRAME_HEIGHT - TILE_SIZE * 7, TILE_SIZE * 8, TILE_SIZE * 7);
        public static final Rectangle2D MENU_FIELD= new Rectangle2D.Double(TILE_SIZE, TILE_SIZE * 6, TILE_SIZE * 18, TILE_SIZE * 6);
        public static final Rectangle2D MENU_FIELD_POS1= new Rectangle2D.Double(MENU_FIELD.getX(),MENU_FIELD.getY(),MENU_FIELD.getWidth(), TILE_SIZE*2);
        public static final Rectangle2D MENU_FIELD_POS2= new Rectangle2D.Double(MENU_FIELD.getX(),MENU_FIELD.getY()+TILE_SIZE*2,MENU_FIELD.getWidth(), TILE_SIZE*2);
        public static final Rectangle2D MENU_FIELD_POS3= new Rectangle2D.Double(MENU_FIELD.getX(),MENU_FIELD.getY()+TILE_SIZE*4,MENU_FIELD.getWidth(), TILE_SIZE*2);
        public static final Rectangle2D STAT_FIELD= new Rectangle2D.Double(DEFAULT_FRAME_WIDTH-TILE_SIZE * 10, 0, TILE_SIZE * 10, TILE_SIZE * 22);
        public static final Rectangle2D SETTINGS_FIELD= new Rectangle2D.Double(DEFAULT_FRAME_WIDTH-TILE_SIZE*18, TILE_SIZE * 6, TILE_SIZE * 18, TILE_SIZE * 12);
        public static final String VERSION_NUMBER = "v0.010.0";
    }
    
    public static class Audio{
       public static enum SFX {
        FIRE,
        DASH,
        RELOAD,
        RELOAD_END,
        HIT,
        HURT,
        DEATH,
        BRUTE_DEATH,
        GUNNER_DEATH,
        CRUSHER_DEATH,
        START
       }
        
    }
    
    public static class Animations{
        public static enum animation{
            PLAYER_IDLE_TEMPLATE,
            PLAYER_MOVE_TEMPLATE,
            PLAYER_SHOOT,
            PLAYER_RELOAD,
            PLAYER_DASH,
            PLAYER_DEATH,
            
            BRUTE_MOVE,
            BRUTE_DEATH,
            BRUTE_ATTACK,
            
            GUNNER_MOVE,
            GUNNER_DEATH,
            GUNNER_TEMPLATE,
            
            CRUSHER_MOVE,
            CRUSHER_IDLE,
            CRUSHER_DEATH,
            
            BULLET_BASIC_TEMPLATE,
            BULLET_MARKSMAN_TEMPLATE,
            BULLET_RAPID_TEMPLATE,
            BULLET_HUGE_TEMPLATE,
            BULLET_ODD_TEMPLATE,
            BULLET_SPLATTER_TEMPLATE,
            BULLET_SWIFT_TEMPLATE,
            BULLET_FRIENDLY,
            BULLET_FRIENDLY_CRIT,
            BULLET_ENEMY,
            
            LEVEL,
            LEVEL_BIG,
            LEVEL_TILES_TEMPLATE,
            
            BLOOD
        }
        
        public static final BufferedImage[] PLAYER_IDLE_ANIM=loadAnimation(animation.PLAYER_IDLE_TEMPLATE);
        public static final BufferedImage[] PLAYER_MOVE_ANIM = loadAnimation(animation.PLAYER_MOVE_TEMPLATE);
        public static final BufferedImage[] PLAYER_SHOOT_ANIM=loadAnimation(animation.PLAYER_SHOOT);
        public static final BufferedImage[] PLAYER_RELOAD_ANIM=loadAnimation(animation.PLAYER_RELOAD);
        public static final BufferedImage[] PLAYER_DASH_ANIM=loadAnimation(animation.PLAYER_DASH);
        public static final BufferedImage[] PLAYER_DEATH_ANIM=loadAnimation(animation.PLAYER_DEATH);
        
        public static final BufferedImage[] BRUTE_MOVE_ANIM=loadAnimation(animation.BRUTE_MOVE);
        public static final BufferedImage[] BRUTE_DEATH_ANIM=loadAnimation(animation.BRUTE_DEATH);
        public static final BufferedImage[] BRUTE_ATTACK_ANIM=loadAnimation(animation.BRUTE_ATTACK);
        
        public static final BufferedImage[] GUNNER_MOVE_ANIM=loadAnimation(animation.GUNNER_MOVE);
        public static final BufferedImage[] GUNNER_DEATH_ANIM=loadAnimation(animation.GUNNER_DEATH);
        public static final BufferedImage[] GUNNER_ANIM=loadAnimation(animation.GUNNER_TEMPLATE);
        
        public static final BufferedImage[] CRUSHER_MOVE_ANIM=loadAnimation(animation.CRUSHER_MOVE);
        public static final BufferedImage[] CRUSHER_IDLE_ANIM=loadAnimation(animation.CRUSHER_IDLE);
        public static final BufferedImage[] CRUSHER_DEATH_ANIM=loadAnimation(animation.CRUSHER_DEATH);
        
        public static final BufferedImage[] BULLET_BASIC_ANIM = loadAnimation(animation.BULLET_BASIC_TEMPLATE);
        public static final BufferedImage[] BULLET_MARKSMAN_ANIM = loadAnimation(animation.BULLET_MARKSMAN_TEMPLATE);
        public static final BufferedImage[] BULLET_RAPID_ANIM = loadAnimation(animation.BULLET_RAPID_TEMPLATE);
        public static final BufferedImage[] BULLET_HUGE_ANIM = loadAnimation(animation.BULLET_HUGE_TEMPLATE);
        public static final BufferedImage[] BULLET_ODD_ANIM = loadAnimation(animation.BULLET_ODD_TEMPLATE);
        public static final BufferedImage[] BULLET_SPLATTER_ANIM = loadAnimation(animation.BULLET_SPLATTER_TEMPLATE);
        public static final BufferedImage[] BULLET_SWIFT_ANIM = loadAnimation(animation.BULLET_SWIFT_TEMPLATE);
        public static final BufferedImage[] BULLET_FRIENDLY_ANIM=loadAnimation(animation.BULLET_FRIENDLY);
        public static final BufferedImage[] BULLET_FRIENDLY_CRIT_ANIM=loadAnimation(animation.BULLET_FRIENDLY_CRIT);
        public static final BufferedImage[] BULLET_ENEMY_ANIM=loadAnimation(animation.BULLET_ENEMY);
        
        public static final BufferedImage[] LEVEL_ANIM=loadAnimation(animation.LEVEL);
        public static final BufferedImage[] LEVEL_BIG_ANIM=loadAnimation(animation.LEVEL_BIG);
        public static final BufferedImage[] LEVEL_TILES_ANIM=loadAnimation(animation.LEVEL_TILES_TEMPLATE);
        
        public static final BufferedImage[] BLOOD_ANIM=loadAnimation(animation.BLOOD);
        
        
        private static BufferedImage[] loadAnimation(animation ani){
            int aniLenght;
            switch(ani){
                case CRUSHER_IDLE, LEVEL, LEVEL_BIG -> aniLenght=1;
                case PLAYER_SHOOT -> aniLenght=4;
                case BULLET_ENEMY, BULLET_FRIENDLY_CRIT, BRUTE_DEATH, GUNNER_DEATH, CRUSHER_DEATH, PLAYER_DEATH,BRUTE_ATTACK-> aniLenght=6;
                case LEVEL_TILES_TEMPLATE -> aniLenght =20;
                case BRUTE_MOVE, GUNNER_MOVE, CRUSHER_MOVE -> aniLenght=8;
                case PLAYER_DASH -> aniLenght=11;
                case PLAYER_RELOAD, BLOOD ,PLAYER_IDLE_TEMPLATE,PLAYER_MOVE_TEMPLATE,BULLET_BASIC_TEMPLATE,BULLET_MARKSMAN_TEMPLATE,BULLET_RAPID_TEMPLATE,BULLET_HUGE_TEMPLATE,
                        BULLET_ODD_TEMPLATE,BULLET_SPLATTER_TEMPLATE,BULLET_SWIFT_TEMPLATE,GUNNER_TEMPLATE-> aniLenght=12;
                default -> aniLenght=1;
            }
            
            BufferedImage[] animFrames = new BufferedImage[aniLenght];
            switch(ani){
                case LEVEL->{
                    for(int i=0; i<animFrames.length; i++){
                        animFrames[i]= loadSpriteSheet(ani.toString()).getSubimage(i*80, 0, 80, 80);
                    }
                }
                case LEVEL_TILES_TEMPLATE, BULLET_FRIENDLY,BLOOD->{
                    for(int i=0; i<animFrames.length; i++){
                        animFrames[i]= loadSpriteSheet(ani.toString()).getSubimage(i*16, 0, 16, 16);
                    }
                }
                case LEVEL_BIG->{
                    for(int i=0; i<animFrames.length; i++){
                        animFrames[i]= loadSpriteSheet(ani.toString()).getSubimage(i*160, 0, 160, 160);
                    }
                }
                case PLAYER_IDLE_TEMPLATE,PLAYER_MOVE_TEMPLATE,BULLET_BASIC_TEMPLATE,BULLET_MARKSMAN_TEMPLATE,BULLET_RAPID_TEMPLATE,BULLET_HUGE_TEMPLATE,
                        BULLET_ODD_TEMPLATE,BULLET_SPLATTER_TEMPLATE,BULLET_SWIFT_TEMPLATE,GUNNER_TEMPLATE->{
                    for(int i=0; i<animFrames.length; i++){
                        animFrames[i]= SpriteRandomizer.Randomize(loadSpriteSheet(ani.toString()).getSubimage(i*16, 0, 16, 16));
                    }
                }
                default->{
                    for(int i=0; i<animFrames.length; i++){
                        animFrames[i]= loadSpriteSheet(ani.toString()).getSubimage(i*425, 0, 425, 550);
                    }
                }
            }
            
            return animFrames;
            }
        
        
        
        public static BufferedImage[] getAnimation(animation ani){
            BufferedImage[] givenAni;
            switch( ani){
                case PLAYER_IDLE_TEMPLATE -> givenAni=PLAYER_IDLE_ANIM;
                case PLAYER_MOVE_TEMPLATE -> givenAni=PLAYER_MOVE_ANIM;
                case PLAYER_SHOOT -> givenAni=PLAYER_SHOOT_ANIM;
                case PLAYER_RELOAD -> givenAni=PLAYER_RELOAD_ANIM;
                case PLAYER_DASH -> givenAni=PLAYER_DASH_ANIM;
                case PLAYER_DEATH -> givenAni=PLAYER_DEATH_ANIM;
                
                case BRUTE_MOVE -> givenAni=BRUTE_MOVE_ANIM;
                case BRUTE_DEATH -> givenAni=BRUTE_DEATH_ANIM;
                case BRUTE_ATTACK -> givenAni=BRUTE_ATTACK_ANIM;
                
                case GUNNER_MOVE -> givenAni=GUNNER_MOVE_ANIM;
                case GUNNER_DEATH -> givenAni=GUNNER_DEATH_ANIM;
                case GUNNER_TEMPLATE->givenAni=GUNNER_ANIM;
                
                case CRUSHER_MOVE -> givenAni=CRUSHER_MOVE_ANIM;
                case CRUSHER_IDLE -> givenAni=CRUSHER_IDLE_ANIM;
                case CRUSHER_DEATH -> givenAni=CRUSHER_DEATH_ANIM;
                
                case BULLET_BASIC_TEMPLATE -> givenAni=BULLET_BASIC_ANIM;
                case BULLET_MARKSMAN_TEMPLATE -> givenAni=BULLET_MARKSMAN_ANIM;
                case BULLET_RAPID_TEMPLATE->givenAni=BULLET_RAPID_ANIM;
                case BULLET_HUGE_TEMPLATE->givenAni=BULLET_HUGE_ANIM;
                case BULLET_ODD_TEMPLATE->givenAni=BULLET_ODD_ANIM;
                case BULLET_SPLATTER_TEMPLATE->givenAni=BULLET_SPLATTER_ANIM;
                case BULLET_SWIFT_TEMPLATE-> givenAni=BULLET_SWIFT_ANIM;
                case BULLET_FRIENDLY -> givenAni=BULLET_FRIENDLY_ANIM;
                case BULLET_FRIENDLY_CRIT -> givenAni=BULLET_FRIENDLY_CRIT_ANIM;
                case BULLET_ENEMY -> givenAni=BULLET_ENEMY_ANIM;
                
                case LEVEL -> givenAni=LEVEL_ANIM;
                case LEVEL_BIG -> givenAni=LEVEL_BIG_ANIM;
                case LEVEL_TILES_TEMPLATE -> givenAni=LEVEL_TILES_ANIM;
                
                case BLOOD -> givenAni=BLOOD_ANIM;
                default -> givenAni=PLAYER_IDLE_ANIM;
            }
            
            return givenAni;
        }
    }
    
    public static class hitboxData{
        private static final int[] PLAYER_HITBOX_X = {1, 6, 12, 17, 17, 13, 3, 1};
        private static final int[] PLAYER_HITBOX_Y = {8, 3, 3, 7, 14, 16, 16, 14};
        public static final Polygon PLAYER_HITBOX = new Polygon(PLAYER_HITBOX_X,PLAYER_HITBOX_Y,PLAYER_HITBOX_X.length);
        private static final int[] BRUTE_ATTACK_HITBOX_X = {-3, 19, 8};
        private static final int[] BRUTE_ATTACK_HITBOX_Y = {-5, -5, 8};
        public static final Polygon BRUTE_ATTACK_HITBOX = new Polygon(BRUTE_ATTACK_HITBOX_X,BRUTE_ATTACK_HITBOX_Y,BRUTE_ATTACK_HITBOX_Y.length);
        
        private static final int[] BULLET_BASIC_HITBOX_X= {6, 8, 9, 11, 11,6};
        private static final int[] BULLET_BASIC_HITBOX_Y= {7, 5, 5, 7, 11,11};
        public static final Polygon BULLET_BASIC_HITBOX=new Polygon(BULLET_BASIC_HITBOX_X,BULLET_BASIC_HITBOX_Y,BULLET_BASIC_HITBOX_X.length);
        
        private static final int[] BULLET_MARKSMAN_HITBOX_X= {7, 10, 10, 11, 11,6,6,7};
        private static final int[] BULLET_MARKSMAN_HITBOX_Y= {3, 3, 8, 8, 13,13,9,9};
        public static final Polygon BULLET_MARKSMAN_HITBOX=new Polygon(BULLET_MARKSMAN_HITBOX_X,BULLET_MARKSMAN_HITBOX_Y,BULLET_MARKSMAN_HITBOX_X.length);
        
        private static final int[] BULLET_RAPID_HITBOX_X= {6, 8, 9, 11, 11,6};
        private static final int[] BULLET_RAPID_HITBOX_Y= {7, 5, 5, 7, 10,10};
        public static final Polygon BULLET_RAPID_HITBOX=new Polygon(BULLET_RAPID_HITBOX_X,BULLET_RAPID_HITBOX_Y,BULLET_RAPID_HITBOX_X.length);
        
        private static final int[] BULLET_HUGE_HITBOX_X= {3, 7, 10, 14, 14,10,7,3};
        private static final int[] BULLET_HUGE_HITBOX_Y= {8, 4, 4, 8, 11,15,15,11};
        public static final Polygon BULLET_HUGE_HITBOX=new Polygon(BULLET_HUGE_HITBOX_X,BULLET_HUGE_HITBOX_Y,BULLET_HUGE_HITBOX_X.length);
        
        private static final int[] BULLET_ODD_HITBOX_X = {5, 8, 9, 12, 12, 9, 5};
        private static final int[] BULLET_ODD_HITBOX_Y = {8, 4, 4, 6, 8, 12, 9};
        public static final Polygon BULLET_ODD_HITBOX = new Polygon(BULLET_ODD_HITBOX_X, BULLET_ODD_HITBOX_Y, BULLET_ODD_HITBOX_X.length);
        
        private static final int[] BULLET_SPLATTER_HITBOX_X = {7, 11, 11, 7};
        private static final int[] BULLET_SPLATTER_HITBOX_Y = {5, 5, 11, 11};
        public static final Polygon BULLET_SPLATTER_HITBOX = new Polygon(BULLET_SPLATTER_HITBOX_X, BULLET_SPLATTER_HITBOX_Y, BULLET_SPLATTER_HITBOX_X.length);
        
        private static final int[] BULLET_SWIFT_HITBOX_X = {6, 8, 9, 11, 11, 6};
        private static final int[] BULLET_SWIFT_HITBOX_Y = {7, 5, 5, 7,9,9};
        public static final Polygon BULLET_SWIFT_HITBOX = new Polygon(BULLET_SWIFT_HITBOX_X, BULLET_SWIFT_HITBOX_Y, BULLET_SWIFT_HITBOX_X.length);
        
        
    }
    
    public static class LevelData{
        
        
        public static final Color[][] LEVEL_DATA=loadLevelData();
        public static final int LEVEL_INDEX_WIDTH=LEVEL_DATA.length-1;
        public static final int LEVEL_INDEX_HEIGHT=LEVEL_DATA[0].length-1;
        
        public static final Rectangle2D LEVEL_SIZE=new Rectangle2D.Float(0, 0, TILE_SIZE*(LEVEL_DATA.length), TILE_SIZE*(LEVEL_DATA[0].length));
        public static enum TileType{
            WALL,
            FLOOR_BESIDE_WALL,
            FLOOR,
            CRACKED_WALL,
            CRACKED_WALL_BESIDE_WALL,
            
        }
        public static enum LevelType{
            DEFAULT,
            BIG
        }
        public static int getDefaultTileTypeCost(TileType type){
            int tCost=1;
            switch(type){
                case FLOOR -> tCost = 1;
                case FLOOR_BESIDE_WALL -> tCost = 2;
                case CRACKED_WALL -> tCost =3;
                case CRACKED_WALL_BESIDE_WALL -> tCost = 4;
                case WALL-> tCost=9999;
            }
            return tCost;
        }
        
        private static Color[][] loadLevelData(){
             BufferedImage img = getAnimation(animation.LEVEL)[0];
            Color[][]data= new Color[img.getWidth()][img.getHeight()];
           
        for(int x = 0; x< img.getWidth(); x++)
            for(int y = 0; y<img.getHeight(); y++){
                data[x][y] = new Color(img.getRGB(x, y));
            }
        return data;
        }
        
        
    }
    
    public static class Settings{
        public static final RenderingHints[] SETTINGS_RENDER_SPEED ={new RenderingHints(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_DEFAULT), new RenderingHints(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY), new RenderingHints(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED)};
        
        public static final RenderingHints[] SETTINGS_COLOR_RENDER_SPEED ={new RenderingHints(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_DEFAULT), new RenderingHints(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY), new RenderingHints(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED)};
        
        public static final RenderingHints[] SETTINGS_ANTIALIASING ={new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON),new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF)};
        
        public static final RenderingHints[] SETTINGS_TEXT_ANTIALIASING ={new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON),new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)}; 
        
        public static final RenderingHints[] SETTINGS_INTERPOLATION ={new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC), new RenderingHints(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR)};
        
        public static final String[] SETTINGS_RENDER_SPEED_NAME ={"Quality","Speed"};
        public static final String[] SETTINGS_ANTIALIASING_NAME ={"On","Off"};
        public static final String[] SETTINGS_INTERPOLATION_NAME ={"Nearest neighbor","Bicubic","Bilinear"};
        
        public static final int[] SETTINGS_FPS={30,60,120,240};
        public static final Dimension[] SETTINGS_RESOLUTION={new Dimension(800,600),new Dimension(1024,768),new Dimension(1152,864),
        new Dimension(1280,600), new Dimension(1280,720), new Dimension(1280,768),new Dimension(1280,800),
        new Dimension(1280,960), new Dimension(1280,1024), new Dimension(1360,768),new Dimension(1366,768),
        new Dimension(1400,1050),new Dimension(1440,900),new Dimension(1600,900), new Dimension(1680,1050), new Dimension(1980,1080)  };

    }
    
}
