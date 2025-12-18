package objects;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;

public class Potion extends GameObject { // Represents collectible potions with animated frames and a floating hover effect

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    public Potion(int x, int y, int objType) {
        super(x, y, objType); // Initializes position and type
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffset = (int)(3*Game.SCALE);
        yDrawOffset = (int)(2*Game.SCALE);

        maxHoverOffset = (int)(10 * Game.SCALE);
    }

    // Updates both the sprite animation frames and the vertical hover position.
    public void update(){
        updateAnimationTick();
        updateHover();
    }

    // Calculates a smooth up-and-down floating motion for the potion.
    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);

        if(hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if(hoverOffset < 0)
            hoverDir = 1;

        hitbox.y = y + hoverOffset;
    }

    public void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
        if(Game.DRAW_HITBOXES){ 
            g.setColor(Color.RED);
            g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
        }
    }

    
}
