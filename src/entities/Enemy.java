package entities;

// Static imports allow direct access to utility methods and game constants
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import java.awt.geom.Rectangle2D;

import main.Game;

// An abstract class defining the universal behavior, physics, and behavior for all enemies.
public abstract class Enemy extends Entity {
   	protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = left;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height,  int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }
    
    protected void firstUpdateCheck(int[][] lvlData){ // Checks on the first frame if the enemy is starting in the air.
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData){ // Handles falling physics and snapping to the floor upon landing.
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            }
            else{
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                tileY = (int)(hitbox.getY() / Game.TILES_SIZE);
            }
    }

        protected void move(int[][] lvlData){ // Standard patrol movement logic including edge and wall detection
                float xSpeed = 0;

                if(walkDir == left)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;

                if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                    if(isFloor(hitbox, xSpeed, lvlData)){
                        hitbox.x += xSpeed;
                        return;
                    }

                changeWalkDir();
        }

    protected void turnTowardsPlayer(Player player){ // Changes walking direction to face the player's current X position.
        if(player.hitbox.x > hitbox.x)
            walkDir = right;
        else
            walkDir = left;

    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){ // Checks if the player is on the same row and in a clear line of sight.
        int playerTileY = (int)(player.getHitbox().getY() / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player)){
                if(IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }

        return false;
    }

    protected boolean isPlayerInRange(Player player) { // Checks if player is within general detection range.
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player){ //Checks if player is close enough to trigger an attack state.
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }


    protected void newState(int enemyState){ // Transitions the enemy to a new state and resets animation counters.
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount){ // Reduces health and triggers the HIT or DEAD state.
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);
        else    
            newState(HIT);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) { // Checks if the enemy's attack box overlaps with the player's hitbox.
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick(){ // Updates animation frames and handles state resets after animations end
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;

                switch(state){
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }

			}
		}
    }
    
    protected void changeWalkDir() {
        if(walkDir == left)
            walkDir = right;
        else
            walkDir = left;
    }

    public void resetEnemy(){ // Resets the enemy to its initial position and state for level restarts.
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive(){
        return active;
    }



}
