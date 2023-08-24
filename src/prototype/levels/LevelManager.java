package prototype.levels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashSet;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.LevelData.*;
import prototype.system.GameLoop;
import static prototype.system.Prototype.camera;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.renderpanel;

/**
 *
 * @author imfai
 */
public class LevelManager {

    public static Rectangle2D bounds;
    private TileMap map = new TileMap(LEVEL_DATA);
    private Tile lastFlowFieldSet;
    private int rayCastId=0;
    private ArrayList<Point2D> shadowPoints=new ArrayList<>();

    public LevelManager() {
        initLevel();
    }

    public void initLevel() {
        bounds = LEVEL_SIZE;
        bounds.setFrame(LEVEL_SIZE);
        lastFlowFieldSet=null;
        
    }

    public Rectangle2D getBounds() {
        return bounds;
    }
    
    public Point2D getBoundsCenterPoint(){
        return new Point2D.Float((float)bounds.getCenterX(), (float)bounds.getCenterY());
    }
    public Point2D getBoundsLocationIndex(){
        return new Point2D.Float((int)(bounds.getCenterX()/TILE_SIZE), (int)(bounds.getCenterY()/TILE_SIZE));
    }

    public void update() {
        setFlowField(999 * 999, gameboard.getPlayer().getLocationIndex());
        
    }

    public Graphics2D render(Graphics2D g2d) {
        //nested for loop that iterates over every tile within camerabounds. no reason to render tiles outside bounds.
        for (int x = (int) camera.getMinLocationIndex().getX(); x < camera.getMaxLocationIndex().getX(); x++)
            for (int y = (int) camera.getMinLocationIndex().getY(); y < camera.getMaxLocationIndex().getY(); y++) {
                if(!camera.getBounds().contains(map.getTiles()[x][y].getBounds())){
                    map.getTiles()[x][y].lightOff();
                }
                if(map.getTiles()[x][y].getLight()>0 || GameLoop.isDebug()){
                    map.getTiles()[x][y].render(g2d);
                }         
            }
        Polygon poly = new Polygon();
        for(int i = 0; i<shadowPoints.size();i++){
            Point2D point = shadowPoints.get(i);
            poly.addPoint((int)point.getX(), (int)point.getY());
        }
        Area area = new Area(poly);
        Area area2 = new Area(bounds);
        area.exclusiveOr(area2);
        g2d.setColor(new Color(0,0,0,255));
        g2d.fill(area);
        
        return g2d;
    }
    
    public void rayCaster(Point2D origin, double rayDistance, int source) {
        /*Outer loop that cast a ray from origin to the edge of camera/level. Once the inner loop of this said ray is done, 
            rotate the line by X amount anchored to origin and run the inner loop again until full rotation has been made.
                basically instead of casting many rays, you just rotate one many times per frame*/
        double lightDistance=rayDistance*0.05;
        ArrayList<Point2D> shadowPoints = new ArrayList<>();
        double rayDirection =0;
        double rayAngle=0.05;
        if(rayCastId==2147483647){
            rayCastId=0;
        }else{
            rayCastId++;
        }    
        while(rayDirection<=360){
            Point2D directionPoint =new Point2D.Double(rayDistance * Math.cos(Math.toRadians(rayDirection)) + origin.getX(), 
            rayDistance * Math.sin(Math.toRadians(rayDirection)) + origin.getY());
            Line2D line = new Line2D.Float(origin, directionPoint);
            rayDirection+=rayAngle;

            Point2D currentPoint=line.getP1();
            Tile currentTile=null;
            Tile previousTile=null;

            double deltax = line.getX2() - line.getX1();
            double deltay = line.getY2() - line.getY1();
            double speed =TILE_SIZE;
            double distanceTravelled=0;
            double direction = Math.atan2(deltay, deltax);
            float light=1.0f;
            float distanceFade=1.0f;
            boolean shadowPointSet = false;
            
            /*inner loop that sends a point among the line. after each move it sets the tile under it visible, 
                until wall has been hit or the respective bounds have been reached*/
            while(bounds.contains(currentPoint)&&distanceTravelled<rayDistance){
                
                currentTile=map.getCurrentTile(getIndexFromPoint(currentPoint));
                if(previousTile==null || currentTile.getID()!=previousTile.getID()){
                    distanceFade=(float)(((rayDistance-lightDistance)-(distanceTravelled-lightDistance))/(rayDistance-lightDistance));
                    if(distanceFade>1.0f){
                        distanceFade=1.0f;
                    }
                    currentTile.castLight(light, rayCastId+source);
                    light=(currentTile.letLightThrough(light)*distanceFade);
                    previousTile=currentTile;
                }
                currentPoint.setLocation(currentPoint.getX()+speed * Math.cos(direction), currentPoint.getY()+speed * Math.sin(direction));
                distanceTravelled+=speed;
                if(!shadowPointSet&&light<=0){
                    shadowPoints.addAll(currentTile.getCornerPoints());
                    shadowPointSet=true;
                }
            }
        }
        this.shadowPoints=shadowPoints;
    }

    public TileMap getMap() {
        return map;
    }
    
    public boolean checkLOS(Rectangle2D boundsA, Rectangle2D boundsB, float range){
            double dx = boundsB.getCenterX() - boundsA.getCenterX();
            double dy = boundsB.getCenterY() - boundsA.getCenterY();
            double losDirection = Math.atan2(dy, dx);
            float pointSpeed = TILE_SIZE*0.5f;
            double distanceTravelled=0;
            Point2D directionPoint =new Point2D.Double(range * Math.cos(losDirection) + boundsA.getCenterX(), 
                range * Math.sin(losDirection) + boundsA.getCenterY());
            Line2D losLine = new Line2D.Double(boundsA.getCenterX(), boundsA.getCenterY(), directionPoint.getX(), directionPoint.getY());
            Point2D currentPoint = losLine.getP1();

            while (currentPoint.distance(losLine.getP1())<range){
                if(map.getCurrentTile(getIndexFromPoint(currentPoint)).getType()==TileType.WALL){
                    return false;
                }
                if(boundsB.contains(currentPoint)){
                    return true;
                }
                currentPoint.setLocation(currentPoint.getX()+pointSpeed * Math.cos(losDirection), currentPoint.getY()+pointSpeed * Math.sin(losDirection));
            }
        
        return false;
    }

    public boolean collidesWithWall(Area hitbox, Point2D index) {
        ArrayList<Tile>adjacent=new ArrayList<>();
        adjacent.add(map.getCurrentTile(index));
        adjacent.addAll(map.getCurrentTile(index).getSurroundingTiles());
        boolean collision=false;
        for(Tile tile : adjacent){
            if(hitbox.intersects(tile.getBounds())&&tile.isSolid()){
                collision=true;
                break;
            }
        }
        return collision;
    }
    public boolean collidesWithWall(Rectangle2D hitbox, Point2D index, boolean ignoreCrackedWalls) {
        ArrayList<Tile>adjacent=new ArrayList<>();
        adjacent.add(map.getCurrentTile(index));
        adjacent.addAll(map.getCurrentTile(index).getSurroundingTiles());
        boolean collision=false;
        for(Tile tile : adjacent){
            if(hitbox.intersects(tile.getBounds())&&tile.isSolid()){
                if(ignoreCrackedWalls&&tile.getType()==TileType.CRACKED_WALL){
                    break;
                }
                collision=true;
                break;
            }
        }
        return collision;
    }

    public Point2D getIndexFromPoint(Point2D point) {
        return new Point2D.Float((int) (point.getX() / TILE_SIZE), (int) (point.getY() / TILE_SIZE));
    }

    public Point2D getRandomSpawnPoint(boolean hasToBeDark) {
        ArrayList<Tile> spawntiles = new ArrayList<>();
        for(Tile[] tiles : map.getTiles())
                    for(Tile tile : tiles){
                        if (tile.getType() == TileType.FLOOR) {
                            if(hasToBeDark){
                                if(tile.getLight()<=0){
                                    spawntiles.add(tile);
                                }
                            }else{
                                spawntiles.add(tile);
                            }
                            
                        }
                        
                    }

        int random = (int) (Math.random() * spawntiles.size());
        return spawntiles.get(random).getCenterPoint();
    }
    
    public double getRoamingDirection(Point2D index){
        HashSet<Tile> adjacent = map.getCurrentTile(index).getSurroundingTiles();
        int random = (int) (Math.random() * adjacent.size());
        int randomIterator=0;
        Tile roamingTile=null;
        for(var tile: adjacent){
            roamingTile=tile;
            randomIterator++;
            if(randomIterator>=random){
                break;
            }
        }
        double dx = roamingTile.getLocationIndex().getX() - index.getX();
        double dy = roamingTile.getLocationIndex().getY() - index.getY();
        return Math.atan2(dy, dx);
    }

    public void setFlowField(int range, Point2D index) {
        Tile goalTile = map.getCurrentTile(index);
        if(lastFlowFieldSet!=goalTile){
            lastFlowFieldSet=goalTile;
            ArrayList<Tile> open = new ArrayList<>();
            for(Tile[] tiles : map.getTiles())
                for(Tile tile : tiles){
//                    if (tile.getType() != TileType.WALL) {
                        tile.setCost(9999);
//                    }
                }
            goalTile.setCost(0);
            open.add(0, goalTile);
            while (!open.isEmpty() && range > 0) {
                Tile currentTile = open.get(0);
                open.remove(currentTile);
                for (Tile tile : currentTile.getNeighboringTiles()) {
//                    if(tile.getType()!=TileType.WALL){
                        if (currentTile.getGCost()+getDefaultTileTypeCost(tile.getType())<tile.getGCost()) {
                        tile.setCost(currentTile.getGCost()+getDefaultTileTypeCost(tile.getType()));
                        if(tile.getType()!=TileType.WALL){
                        open.add(tile);
                        }
                        }
                        tile.setDirection();
//                    }   
                }
                range--;
            }
        }  
    }
}
