package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
    
    // hitbox and basic player stuff like attacking and movement
    private BufferedImage[][] animations;
    private boolean moving = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffsetRight = 21 * Game.SCALE;
    private float xDrawOffsetLeft = 37 * Game.SCALE;
    private float yDrawOffset = 15 * Game.SCALE;
    private boolean attacking = false;

    // jumping and Gravity
    private float jumpSpeed = -2.00f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.20f * Game.SCALE;

    // status bar UI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY= (int)(10 * Game.SCALE);

    // health Bar box
    private int healthBarWidth= (int)(150 * Game.SCALE);
    private int healthBarHeight= (int)(4 * Game.SCALE);
    private int healthBarXStart= (int)(34 * Game.SCALE);
    private int healthBarYStart= (int)(14 * Game.SCALE);

    // for HP
    private int healthWidth = healthBarWidth;

    // for left and right movement of player drawing
    private int flipX = 0;
    private int flipW =  1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, (int)(78 * Game.SCALE), (int)(58 * Game.SCALE));
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE * 0.6f;
        loadAnimations();
        initHitbox(20, 28);
        initAttackBox();
    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(35 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update(){
        updateHealthBar();
        if(currentHealth <= 0){
            if(state != DEAD){
                state = DEAD;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
            }
            else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1){
                playing.setGameOver(true);
            }
            else{
                updateAnimationTick();
            }


            return;
        }

        updateAttackBox();
        updatePos();
        if(moving){
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();

    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }

    private void updateAttackBox() {
        // flipW == 1 means we are visually facing Right
        // flipW == -1 means we are visually facing Left
        int attackGap = (int)(5 * Game.SCALE);
        if (flipW == 1) {
            // RIGHT SIDE
            attackBox.x = hitbox.x + hitbox.width + attackGap;
        } 
        else {
            // LEFT SIDE
            attackBox.x = hitbox.x - attackBox.width- attackGap;
        }
        // Y Position
        attackBox.y = hitbox.y + (Game.SCALE * 7);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);

    }

    public void render(Graphics g, int lvlOffset){

        float drawXOffset = xDrawOffsetRight;
        
        if (flipW == -1) {
            drawXOffset = xDrawOffsetLeft;
        }

        g.drawImage(animations[state][aniIndex], 
                (int) (hitbox.x - drawXOffset) - lvlOffset + flipX, 
                (int) (hitbox.y - yDrawOffset), 
                width * flipW, height, null);

        
        drawAttackBox(g, lvlOffset);
        drawUI(g);
        drawHitbox(g, lvlOffset); 
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
            aniTick++;

            if (aniTick >= ANI_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(state)) {
                    aniIndex = 0;
                    attackChecked = false;
                    
                    // If finished the ATTACK animation, stop attacking.
                    if (state == ATTACK) {
                        attacking = false;
                    }
                }
            }
        }

    private void setAnimation() {
            int oldAction = state;

            // 1. CHECK ATTACK FIRST 
            if (attacking) {
                state = ATTACK;
                if(oldAction != ATTACK){
                    aniIndex = 0;
                    aniTick = 0;
                    return;
                }
            } 
            // 2. Then check In Air
            else if (inAir) {
                if (airSpeed < 0)
                    state = IN_AIR;
                else
                    state = BEFORE_LANDING;
            } 
            // 3. Then check Movement
            else if (moving) {
                state = RUNNING;
            } 
            // 4. Default to Idle
            else {
                state = IDLE;
            }

        if (oldAction != state) {
            aniTick = 0;
            aniIndex = 0;
        }
    }
        private void updatePos(){
            moving = false;

        if(jump){
            jump();
        }
        if(!inAir)
            if((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed = -walkSpeed;
            flipX = width;
            flipW = -1;
        } 

        if (right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
    

        if (inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += airSpeed;
                    airSpeed += GRAVITY;
                    updateXPos(xSpeed);
                } 
                else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                    if (airSpeed > 0)
                        resetInAir();
                    else
                        airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }

            } 
            else
                updateXPos(xSpeed);
            moving = true;
        
        }


        private void jump() {
            if (inAir)
                return;
            inAir = true;
            airSpeed = jumpSpeed;

        }

        public void setAttacking(boolean attacking) {
            this.attacking = attacking;
        }

        private void resetInAir() {
            inAir = false;
            airSpeed = 0;
        }

        private void updateXPos(float xSpeed) {
            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                hitbox.x += xSpeed;
            } else {
                hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            }

        }

        public void changeHealth(int value){
            currentHealth += value;

            if(currentHealth <= 0){
                currentHealth = 0;
                // gameOver();
            }
            else if(currentHealth >= maxHealth)
                currentHealth = maxHealth;
        }

        public void kill() {
            currentHealth = 0;
        }

        public void changePower(int value){
            System.out.println("POWAHHH!!!!!");
        }

        private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

                animations = new BufferedImage[9][11];
                for (int j = 0; j < animations.length; j++)
                    for (int i = 0; i < animations[j].length; i++) {
                        animations[j][i] = img.getSubimage(i * 78, j * 58, 78, 58);
                    }
            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);  
        }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }
         

    public void resetDirBooleans(){
        left = false;
        right = false;

    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public int getTileY(){
        return tileY;
    }



}
