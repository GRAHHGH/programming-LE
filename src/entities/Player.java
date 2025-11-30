package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

import javax.imageio.ImageIO;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
    
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 20;
    private int playerAction = IDLE;
    private boolean moving = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.5f;
    private int[][] lvlData;
    private float xDrawOffsetRight = 21 * Game.SCALE;
    private float xDrawOffsetLeft = 37 * Game.SCALE;
    private float yDrawOffset = 15 * Game.SCALE;
    private boolean attacking = false;

    // jumping and Gravity
    private float airSpeed = 0f;
    private float gravity = 0.02f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.05f * Game.SCALE;
    private boolean inAir = false;

    // status bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY= (int)(10 * Game.SCALE);

    private int healthBarWidth= (int)(150 * Game.SCALE);
    private int healthBarHeight= (int)(4 * Game.SCALE);
    private int healthBarXStart= (int)(34 * Game.SCALE);
    private int healthBarYStart= (int)(14 * Game.SCALE);

    private int maxHealth = 100;
    private int curretHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // attack box
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW =  1;

    public Player(float x, float y, int width, int height) {
        super(x, y, (int)(78 * Game.SCALE), (int)(58 * Game.SCALE));
        loadAnimations();
        initHitbox(x, y, 20*Game.SCALE, 28*Game.SCALE);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(35 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update(){
        updateHealthBar();
        updateAttackBox();

        updatePos();
        updateAnimationTick();
        setAnimation();

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
        healthWidth = (int)((curretHealth / (float)maxHealth) * healthBarWidth);

    }

    public void render(Graphics g, int lvlOffset){

        float drawXOffset = xDrawOffsetRight;
        
        if (flipW == -1) {
            drawXOffset = xDrawOffsetLeft;
        }

        g.drawImage(animations[playerAction][aniIndex], 
                (int) (hitbox.x - drawXOffset) - lvlOffset + flipX, 
                (int) (hitbox.y - yDrawOffset), 
                width * flipW, height, null);

        // 3. Debug
        drawAttackBox(g, lvlOffset);
        drawUI(g);
        drawHitbox(g, lvlOffset); // Uncomment if need to see the pink box
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
            aniTick++;
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(playerAction)) {
                    aniIndex = 0;
                    
                    // If finished the ATTACK animation, stop attacking.
                    if (playerAction == ATTACK) {
                        attacking = false;
                    }
                }
            }
        }

    private void setAnimation() {
            int oldAction = playerAction;

            // 1. CHECK ATTACK FIRST 
            if (attacking) {
                playerAction = ATTACK;
            } 
            // 2. Then check In Air
            else if (inAir) {
                if (airSpeed < 0)
                    playerAction = IN_AIR;
                else
                    playerAction = BEFORE_LANDING;
            } 
            // 3. Then check Movement
            else if (moving) {
                playerAction = RUNNING;
            } 
            // 4. Default to Idle
            else {
                playerAction = IDLE;
            }

        if (oldAction != playerAction) {
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
            xSpeed = -playerSpeed;
            flipX = width;
            flipW = -1;
        } 

        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir)
            if(!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
    

        if (inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += airSpeed;
                    airSpeed += gravity;
                    updateXPos(xSpeed);
                } else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                    if (airSpeed > 0)
                        resetInAir();
                    else
                        airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }

            } else
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

        private void changeHealth(int value){
            curretHealth += value;

            if(curretHealth <= 0){
                curretHealth = 0;
                // gameOver();
            }
            else if(curretHealth >= maxHealth)
                curretHealth = maxHealth;
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
        up = false;
        down = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }

}
