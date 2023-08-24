package prototype.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Serbs
 */
public abstract class Menu {
    protected ArrayList<MenuButton> buttons  = new ArrayList<>();
    protected String header;
    
    public Menu(){
        header = "init";    
    }
    
    public ArrayList<MenuButton> getButtons(){
        return buttons;
    }
    
    public void updateMenu(){
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).updateButton();
        }
    }
    
    protected abstract Graphics2D drawMenu(Graphics2D g2d);
    
}
