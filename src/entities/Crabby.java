package entities;

import static utilz.HelpMethods.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import main.Game;

public class Crabby extends Enemy {
    private int xDrawOffset = (int) (26 * Game.SCALE);
    private int yDrawOffset = (int) (9 * Game.SCALE);

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
            }
        if (!IsEntityOnFloor(hitbox, lvlData)) 
            inAir = true;
        }
    }

    public int flipX(){
        if(walkDir == right)
            return width;
        else
            return 0;
    }

    public int flipW(){
        if(walkDir == right)
            return -1;
        else    
            return 1;
    }



    public int getXDrawOffset() {
        return xDrawOffset;
    }

    public int getYDrawOffset() {
        return yDrawOffset;
    }
}