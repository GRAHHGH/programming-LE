package entities;

import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import main.Game;

public class Crabby extends Enemy {

    // attack box
    private int attackBoxOffsetX;

    private int xDrawOffset = (int) (26 * Game.SCALE);
    private int yDrawOffset = (int) (9 * Game.SCALE);

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19 );
        initAttackBox();
    }

    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE) );
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }


    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)){
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0)
                        attackChecked = false;

                    
                    if(aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
                case HIT:
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