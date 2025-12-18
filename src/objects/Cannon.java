package objects;

import main.Game;

// Represents a stationary Cannon obstacle that exists at a specific tile height
public class Cannon extends GameObject {

    private int tileY;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y/ Game.TILES_SIZE;
        initHitbox(40, 26);
        hitbox.x -= (int)(4 * Game.SCALE);
        hitbox.y += (int)(6 * Game.SCALE);
    }

    // Handles the cannon's logic updates, such as cycling through the firing animation
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
    // Getter for the row index, used by managers to check if the player is on the same leve
    public int getTileY(){
        return tileY;
    }
    
}
