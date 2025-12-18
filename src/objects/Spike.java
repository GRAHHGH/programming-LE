package objects;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;

// Represents a static spike hazard that damages the player on contact
public class Spike extends GameObject{

    public Spike(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffset;

    }

    public void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
        if(Game.DRAW_HITBOXES){ 
            g.setColor(Color.RED);
            g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
        }
    }

    
}
