package prototype.levels;

import prototype.utility.TileTextureRandomizer;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import prototype.gameobjects.Entity2D;
import prototype.utility.Constants.Animations.animation;
import static prototype.utility.Constants.Animations.getAnimation;
import static prototype.utility.Constants.Fonts.FONT_MINISCULE;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.LevelData.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.levelmanager;
import prototype.gameobjects.things.Bullet;
import static prototype.utility.Constants.Colors.BRUTE_COLOR_PALETTE;

/**
 *
 * @author Serbs
 */
public class Tile {
    private final int ID;
    private int animTicker, animPosition,animSpeed;
    private final Rectangle2D bounds;
    private final int xIndex, yIndex;
    private final TileType type;
    private ArrayList<BufferedImage> tileTexture=new ArrayList<>();
    private ArrayList<Point2D> cornerPoints=new ArrayList<>();
    private HashSet<Entity2D> objectsOnTile=new HashSet<>();
    private HashSet<Tile> neighboringTiles=new HashSet<>();
    private HashSet<Tile> surroundingTiles=new HashSet<>();
    private boolean solid;
    private double direction;
    private double bestCost;
    private int lastRayCastId = -1000;
    private float passableLight;
    private float light;
    private float bestLight=0.0f;
    private float targetLight;

    public Tile(int xIndex, int yIndex, TileType type, boolean visible, int ID, int textureID, int passLight){
        this.ID=ID;
        this.xIndex=xIndex;
        this.yIndex=yIndex;
        this.type=type;
        passableLight=(float)passLight/255;
        bounds=new Rectangle2D.Double(xIndex*TILE_SIZE, yIndex*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        direction=0;
        bestCost=9999999;
        light=0.0f;
        targetLight=0.0f;
        
        for(int i=0; i<12; i++){
        tileTexture.add(TileTextureRandomizer.Randomize(textureID/10));
        }
        
        
        switch(type){
            case FLOOR, FLOOR_BESIDE_WALL, CRACKED_WALL, CRACKED_WALL_BESIDE_WALL ->{
                solid=false;
            }
            case WALL ->{
                solid=true;
                cornerPoints.add(new Point2D.Double(bounds.getX(),bounds.getY()));
                cornerPoints.add(new Point2D.Double(bounds.getMaxX(),bounds.getY()));
                cornerPoints.add(new Point2D.Double(bounds.getMaxX(),bounds.getMaxY()));
                cornerPoints.add(new Point2D.Double(bounds.getX(),bounds.getMaxY()));
                
            }

        }
    }
    public boolean isSolid(){
        return solid;
    }
    public void update(){
        
    }
    public Graphics2D render(Graphics2D g2d) {
            calculateLight();
            switch(type){
                case CRACKED_WALL,CRACKED_WALL_BESIDE_WALL->{
                    updateAnimation();
                }
            }
            
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, light));
            g2d.drawImage(tileTexture.get(animPosition), (int) (bounds.getX()), (int) (bounds.getY()), TILE_SIZE, TILE_SIZE, 
                    null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        //DEBUG STUFF
        if(GameLoop.isDebug()){
            AffineTransform normal = g2d.getTransform();
            switch(type){
                case CRACKED_WALL,CRACKED_WALL_BESIDE_WALL,WALL->g2d.setColor(new Color(255,255,120,255));
                default->g2d.setColor(new Color(60,0,60,255));
            }
            g2d.setFont(FONT_MINISCULE);
                g2d.drawString(""+bestCost, (float)bounds.getX(), (float)bounds.getCenterY());

//            if(type!=TileType.WALL){
                g2d.rotate((direction + Math.toRadians(90)), bounds.getCenterX(), bounds.getCenterY());
                g2d.drawLine((int)bounds.getCenterX(), (int)bounds.getCenterY(), (int)bounds.getCenterX(), (int)bounds.getY());
                g2d.setTransform(normal);
                g2d.draw(bounds);
//            }
                
            
            
        }
        
        
        
        return g2d;
    }
    
    protected void updateAnimation() {
        animSpeed=((int)(0.06*GameLoop.getFPS()));
        if (animTicker >= animSpeed) {
            animPosition++;
            if (animPosition >= tileTexture.size()) {
                animPosition = 0;
            }
            animTicker = 0;
        }
        animTicker++;
    }
    
    public void calculateLight(){
        float valueFromTarget=targetLight-light;
        if(valueFromTarget<0){
            valueFromTarget=valueFromTarget*-1;
        }
        if(light<targetLight){
            light+=valueFromTarget*0.2f;
        }else if(light>targetLight){
            light-=valueFromTarget*0.2f;
        }
        if(light<0){
            light=0.0f;
        }
        if(light>1){
            light=1.0f;
        }
    }

    public ArrayList<Point2D> getCornerPoints(){
        return cornerPoints;
    }
    public int getID(){
        return ID;
    }
    public int getLastRayCastId(){
        return lastRayCastId;
    }
    public void setLastRayCastId(int lastRayCastId){
        this.lastRayCastId=lastRayCastId;
    }
    public float getLight(){
        return light;
    }
    public void castLight(float light, int lastRayCastId){
//        if(this.lastRayCastId!=lastRayCastId){
//           bestLight=light;
//            this.lastRayCastId=lastRayCastId;
//        }
        if(light>bestLight){
            bestLight=light;
        }
        if(bestLight>1){
            bestLight=1;
        }
        targetLight=bestLight;
        if(targetLight>0&&this.light<=0){
            this.light=0.01f;
        }
    }
    public float passableLight(){
        return passableLight;
    }
    public float letLightThrough(float light){
        return light*passableLight;
    }
    public void resetLight(){
        bestLight=0.0f;
        targetLight=0.0f;
    }
    public void lightOff(){
        bestLight=0.0f;
        targetLight=0.0f;
        light=0;
    }
    public Rectangle2D getBounds(){
        return bounds;
    }
    public Point2D getCenterPoint(){
        return new Point2D.Float((int)bounds.getCenterX(), (int)bounds.getCenterY());
    }
    public Point2D getLocationIndex(){
        return new Point2D.Float(xIndex, yIndex);
    }
    public double getDirection(){
        return direction;
    }
    public void setCost(double bestCost){
        this.bestCost=bestCost;
    }
    public double getGCost(){
        return bestCost;
    }

    public void setDirection(){
//        double sumOfCost=0;
//        double deltax=0;
//        double deltay =0;
//        for(Tile tile :getSurroundingTiles()){
//                deltax += (tile.getCenterPoint().getX() - bounds.getCenterX())*(1/tile.getGCost());
//                deltay += (tile.getCenterPoint().getY() - bounds.getCenterY())*(1/tile.getGCost());   
//        }
//        direction=Math.atan2(deltay, deltax);
        Tile lowestcost=getAdjacentLowestCostTile();
        double deltax = lowestcost.getCenterPoint().getX() - bounds.getCenterX();
        double deltay = lowestcost.getCenterPoint().getY() - bounds.getCenterY();
        direction = Math.atan2(deltay, deltax);
        
    }
    
    public TileType getType(){
        return type;
    }
    
    public Tile getAdjacentLowestCostTile(){
        Tile lowestCostTile=this;
        double lowestCost=1000000000;
        for(Tile tile :getSurroundingTiles()){
            if(tile.bestCost<lowestCost){
                lowestCostTile=tile;
                lowestCost=tile.bestCost;
            }
        }
        return lowestCostTile;
    }
    
    public HashSet<Tile> getNeighboringTiles(){
        return neighboringTiles;
    }
    public HashSet<Tile> getSurroundingTiles(){
        return surroundingTiles;
    }
    
    public void setNeighboringAndSurroundingTiles(){
        if(xIndex>0){
            Tile tileleft = levelmanager.getMap().getTiles()[xIndex-1][yIndex];
            neighboringTiles.add(tileleft);
            surroundingTiles.add(tileleft);
        }
        if(xIndex<LEVEL_INDEX_WIDTH){
            Tile tileright = levelmanager.getMap().getTiles()[xIndex+1][yIndex];
            neighboringTiles.add(tileright);
            surroundingTiles.add(tileright);
        }
        if(yIndex>0){
            Tile tileup = levelmanager.getMap().getTiles()[xIndex][yIndex-1];
            neighboringTiles.add(tileup);
            surroundingTiles.add(tileup);
        }
        if(yIndex<LEVEL_INDEX_HEIGHT){
            Tile tiledown = levelmanager.getMap().getTiles()[xIndex][yIndex+1];
            neighboringTiles.add(tiledown);
            surroundingTiles.add(tiledown);
        }
        if(xIndex>0&&yIndex>0){
            Tile tileupleft = levelmanager.getMap().getTiles()[xIndex-1][yIndex-1];
            surroundingTiles.add(tileupleft);
        }
        if(xIndex<LEVEL_INDEX_WIDTH&&yIndex>0){
            Tile tileupright = levelmanager.getMap().getTiles()[xIndex+1][yIndex-1];
            surroundingTiles.add(tileupright);
        }
        if(xIndex<LEVEL_INDEX_WIDTH&&yIndex<LEVEL_INDEX_HEIGHT){
            Tile tiledownright = levelmanager.getMap().getTiles()[xIndex+1][yIndex+1];
            surroundingTiles.add(tiledownright);
        }
        if(xIndex>0&&yIndex<LEVEL_INDEX_HEIGHT){
            Tile tiledownleft = levelmanager.getMap().getTiles()[xIndex-1][yIndex+1];
            surroundingTiles.add(tiledownleft);
        }
    }
    
    
    public void setObjectOnTile(Entity2D object){
            objectsOnTile.add(object);
    }
    public void removeObjectFromTile(Entity2D object){
            objectsOnTile.remove(object);
    }
    public HashSet getObjectsOnTile(){
        return objectsOnTile;
    }
    
}
