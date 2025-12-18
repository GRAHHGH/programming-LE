package objects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

 // The abstract foundation for all static and interactive game world objects.
public class GameObject {

    protected int x,y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    // Increments the animation frame based on a global speed constant.
    protected void updateAnimationTick(){
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
                if(objType == BARREL || objType == BOX){
                    doAnimation = false;
                    active = false;
                }
                else if (objType == CANNON_LEFT || objType == CANNON_RIGHT){
                    doAnimation = false;
                }
			}
		}
    }

    // Resets the object to its initial state for level restarts.
    public void reset(){
        aniIndex = 0;
        aniTick = 0;
        active = true;

        if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;
    }

    // Creates a new collision box scaled to the game resolution.
    protected void initHitbox(int width, int height) {
       hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset){ 
       if(Game.DRAW_HITBOXES){ // if you press F5 the hitbox will appear
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
       }  
    }

    // --- Standard Getters and Setters ---
    public int getObjType() {
        return objType;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }
    public int getxDrawOffset() {
        return xDrawOffset;
    }
    public int getyDrawOffset() {
        return yDrawOffset;
    }
    public int getAniIndex(){
        return aniIndex;
    }
    public int getAniTick(){
        return aniTick;
    }
 
}
