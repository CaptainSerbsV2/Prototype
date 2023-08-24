package prototype.display;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static prototype.utility.Constants.System.*;
import static prototype.utility.Constants.LevelData.*;
import static prototype.system.Prototype.frame;
import static prototype.system.Prototype.gameboard;
import static prototype.system.Prototype.renderpanel;

/**
 *
 * @author Serbs
 */
public class Camera {
    
    private float paddingX;
    private float paddingY;
    private float cameraX;
    private float cameraY;
    private float targetOffsetX;
    private float offsetX;
    private float targetOffsetY;
    private float offsetY;
    private float maxOffsetX = TILE_SIZE * 10;
    private float maxOffsetY = TILE_SIZE * 5;
    private float accelerationX;
    private float accelerationY;
    private Rectangle2D bounds;

    public Camera() {
        initCamera();
    }

    private void initCamera() {
        cameraX = 0;
        cameraY = 0;
        targetOffsetX = 0;
        targetOffsetY = 0;
        accelerationX = 0.1f;
        accelerationY = 0.1f;
        paddingX=TILE_SIZE*frame.getScalingX();
            paddingY=TILE_SIZE*frame.getScalingX();
        bounds=new Rectangle2D.Float(-paddingX, -paddingY, (frame.getWidth())+paddingX*2, (frame.getHeight())+paddingY*2);
    }

    public double getX() {
        return cameraX;
    }

    public double getY() {
        return cameraY;
    }
    public Rectangle2D getBounds(){
        return bounds;
    }
    public Point2D getMinLocationIndex(){
        int minX=(int)(bounds.getX()/TILE_SIZE);
        if(minX<0){
            minX=0;
        }
        int minY=(int)(bounds.getY()/TILE_SIZE);
        if(minY<0){
            minY=0;
        }
        return new Point2D.Float(minX, minY);
    }
    
    public Point2D getMaxLocationIndex(){
        int maxX=(int)(bounds.getMaxX()/TILE_SIZE);
        if(maxX>LEVEL_INDEX_WIDTH+1){
            maxX=LEVEL_INDEX_WIDTH+1;
        }
        int maxY=(int)(bounds.getMaxY()/TILE_SIZE);
        if(maxY>LEVEL_INDEX_HEIGHT+1){
            maxY=LEVEL_INDEX_HEIGHT+1;
        }
        return new Point2D.Float(maxX, maxY);
    }

    public void update() {

            targetOffsetX=(float)(gameboard.getPlayer().getCenterPoint().getX()-renderpanel.getCurrentMouseLocation().getX());

            targetOffsetY=(float)(gameboard.getPlayer().getCenterPoint().getY()-renderpanel.getCurrentMouseLocation().getY());
            
            accelerationX=((targetOffsetX-offsetX)/maxOffsetX)*0.3f;
            if(accelerationX<0){
                accelerationX=accelerationX*-1;
            }
            accelerationY=((targetOffsetY-offsetY)/maxOffsetY)*0.3f;
            if(accelerationY<0){
                accelerationY=accelerationY*-1;
            }
            
            if (offsetX > targetOffsetX&&offsetX>-maxOffsetX) {
            offsetX -= accelerationX;
            } else if (offsetX < targetOffsetX&&offsetX<maxOffsetX) {
                offsetX += accelerationX;
            }
        
            if (offsetY > targetOffsetY&&offsetY>-maxOffsetY) {
            offsetY -=  accelerationY;
            } else if (offsetY < targetOffsetY&&offsetY<maxOffsetY) {
                offsetY += accelerationY;
            }
            paddingX=TILE_SIZE*frame.getScalingX();
            paddingY=TILE_SIZE*frame.getScalingX();

        cameraX = (float)((gameboard.getPlayer().getCenterPoint().getX()-(frame.getWidth()/frame.getScalingX()* 0.5)-offsetX)*-1);//((gameboard.getPlayer().getHitbox().getCenterX() - (PREFERRED_SIZE.width * 0.5) - offsetX) * -1);
        cameraY = (float) ((gameboard.getPlayer().getCenterPoint().getY()- (frame.getHeight()/frame.getScalingY() * 0.5)-offsetY)*-1);//((gameboard.getPlayer().getHitbox().getCenterY() - (PREFERRED_SIZE.height * 0.5) - offsetY) * -1);
        bounds.setRect((cameraX+paddingX)*-1, (cameraY+paddingY)*-1, bounds.getWidth(), bounds.getHeight());

    }
    

}
