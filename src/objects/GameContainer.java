package objects;
import static utilz.Constants.ObjectConstants.*;

import main.Game;

// Represents destructible containers (Boxes and Barrels) in the game world.
public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() { // Sets unique hitbox sizes and drawing offsets depending on the object type.
        if(objType == BOX){
            initHitbox(25, 10);
            xDrawOffset = (int)(7*Game.SCALE);
            xDrawOffset = (int)(12*Game.SCALE);
        }
        else{
            initHitbox(23, 25);
            xDrawOffset = (int)(8*Game.SCALE);
            xDrawOffset = (int)(5*Game.SCALE);
        }

        hitbox.y += yDrawOffset + (int)(Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;
    }
    
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }



}
