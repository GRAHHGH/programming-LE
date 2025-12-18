package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utilz.Constants.ObjectConstants.*;
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

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_WIDTH, CANNON_HEIGHT);
        this.dir = dir;
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
