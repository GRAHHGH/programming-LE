package objects;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import main.Game;
import static utilz.Constants.Projectiles.*;

// Represents a moving cannonball fired into the game world
public class Projectile { 
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    public Projectile(int x, int y, int dir){
        int xOffset = (int)(-3 * Game.SCALE);
        int yOffset = (int)(5 * Game.SCALE);

        if(dir == 1)
            xOffset = (int)(29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, 18, 18);
        this.dir = dir;
    }

    public void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
        if(Game.DRAW_HITBOXES){ 
            g.setColor(Color.RED);
            g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
        }
    }

    public void updatePos(){ // Updates the horizontal position based on direction and the global SPEED constant
        hitbox.x += dir * SPEED;
    }

    public void setPos(int x, int y){ // Manually sets the projectile position (used during initialization)
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }
}
