package prototype.levels;

import java.awt.Color;
import java.awt.geom.Point2D;
import static prototype.utility.Constants.LevelData.*;

/**
 *
 * @author Serbs
 */
public class TileMap {

    private Tile[][] tiles = new Tile[LEVEL_INDEX_WIDTH+1][LEVEL_INDEX_HEIGHT+1];

    public TileMap(Color[][] data) {
        setTiles(data);
    }

    private void setTiles(Color[][] data) {
        int idSetter=0;
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                switch (data[x][y].getRed()) {
                    case 10,0 ->
                        tiles[x][y] = new Tile(x, y, TileType.WALL, false, idSetter,data[x][y].getGreen(),data[x][y].getBlue());
                    case 40 ->
                        tiles[x][y] = new Tile(x, y, TileType.CRACKED_WALL, false, idSetter,data[x][y].getGreen(),data[x][y].getBlue());
                    case 20 ->
                        tiles[x][y] = new Tile(x, y, TileType.FLOOR_BESIDE_WALL, false, idSetter,data[x][y].getGreen(),data[x][y].getBlue());
                    case 30 ->
                        tiles[x][y] = new Tile(x, y, TileType.FLOOR, false, idSetter,data[x][y].getGreen(),data[x][y].getBlue());
                    case 50 ->
                        tiles[x][y] = new Tile(x, y, TileType.CRACKED_WALL_BESIDE_WALL, false, idSetter,data[x][y].getGreen(),data[x][y].getBlue());

                }
                idSetter++;
            }
        }

        
                
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getCurrentTile(Point2D index) {
        return tiles[(int) index.getX()][(int) index.getY()];
    }
    
    public void setStartingTileValues(){
        for (var alltiles : tiles) {
            for (Tile tile : alltiles) {
                tile.setNeighboringAndSurroundingTiles();
            }
        }
    }

}
