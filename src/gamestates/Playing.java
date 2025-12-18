package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

// The primary gamestate for active gameplay, managing entities, levels, and camera offsets
public class Playing extends State implements Statemethods {
    public Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;

    // UI Overlays for different game situations
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverLay;
    private LevelCompletedOverlay levelCompletedOverlay;

    // Camera scrolling variables
    private int xLvlOffset;
    private int yLvlOffset;
    private int maxLvlOffsetY;
    private int leftBorder = (int)(0.5*Game.GAME_WIDTH);
    private int rightBorder = (int)(0.5*Game.GAME_WIDTH);
    private int topBorder = (int) (0.4 * Game.GAME_HEIGHT);
    private int bottomBorder = (int) (0.6 * Game.GAME_HEIGHT);
    private int maxLvlOffsetX; 

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    // State flags to determine which update/draw logic to run
    private boolean paused = false;
    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean playerDying;

    public Playing(Game game){
        super(game);
        initClasses(); // Instantiate all sub-managers and overlays

        // Randomize cloud positions for a dynamic sky background
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for(int i = 0; i <smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int)(90*Game.SCALE) + rnd.nextInt((int) (100*Game.SCALE));
        calcLvlOffsets();
        loadStartLevel();
    } 

    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        calcLvlOffsets();
        xLvlOffset = 0;
        yLvlOffset = 0;
    }

    private void loadStartLevel() {
        enemyManager.LoadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffsets() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
        
        int[][] lvlData = levelManager.getCurrentLevel().getLevelData();
        
        // 1. Get the height of the level in TILES 
        int lvlHeightInTiles = lvlData.length;
        
        // 2. Convert tiles to PIXELS (Rows * Tile Size)
        int lvlHeightInPixels = lvlHeightInTiles * Game.TILES_SIZE;

        // 3. Calculate max offset
        maxLvlOffsetY = lvlHeightInPixels - Game.GAME_HEIGHT;

        // 4. Safety Check: If the level is smaller than the screen, don't scroll!
        if (maxLvlOffsetY < 0)
            maxLvlOffsetY = 0;
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(200, 200, (int) (25 * Game.SCALE), (int) (25 * Game.SCALE), this);        
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverLay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }


    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        }
        else if(lvlCompleted){
            levelCompletedOverlay.update();
        }
        else if(gameOver){
            gameOverOverLay.update();
        }
        else if(playerDying){
            player.update();
        }
        else{ // Standard gameplay update cycle
            levelManager.update();
            player.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();           
        }

    }

    private void checkCloseToBorder() { // Calculates the camera's x-offset to keep the player centered as they move through the level.
        int playerX = (int) player.getHitbox().x;
        int playerY = (int) player.getHitbox().y;

        int diff = playerX - xLvlOffset;
        int diffY = playerY - yLvlOffset;

        // --- Horizontal Checks ---
        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX) xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0) xLvlOffset = 0;

        // --- Vertical Checks (This is the part you care about) ---
        // If you go BELOW the bottom line, move camera DOWN
        if (diffY > bottomBorder)
            yLvlOffset += diffY - bottomBorder;
        // If you go ABOVE the top line, move camera UP
        else if (diffY < topBorder)
            yLvlOffset += diffY - topBorder;

        if (yLvlOffset > maxLvlOffsetY) yLvlOffset = maxLvlOffsetY;
        else if (yLvlOffset < 0) yLvlOffset = 0;
        }

    @Override
    public void draw(Graphics g) { // Render order: Background -> Clouds -> Level -> Entities -> UI Overlays
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawCLouds(g);

        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
        enemyManager.draw(g, xLvlOffset, yLvlOffset);
        objectManager.draw(g, xLvlOffset, yLvlOffset);
        
        if (Game.DRAW_HITBOXES) 
            objectManager.drawAllHitboxes(g, xLvlOffset, yLvlOffset);

        if(paused){ // Render situational UI based on state flags
            g.setColor(new Color(0,0,0, 150));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if(gameOver)
            gameOverOverLay.draw(g);
        else if(lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawCLouds(Graphics g) { // Renders big and small clouds with "Parallax Scrolling" for a depth effect.

        // --- 1. Big Clouds (Infinite Loop) ---
        // Calculate total width covered by your 10 big clouds
        int bigCloudLoopWidth = BIG_CLOUD_WIDTH * 10; 

        for (int i = 0; i < 10; i++) {
            // A. Calculate standard parallax position
            int xPos = (i * BIG_CLOUD_WIDTH) - (int)(xLvlOffset * 0.4f);
            
            // B. Use Modulo (%) to cycle the position
            // This ensures the value never gets endlessly negative
            xPos = xPos % bigCloudLoopWidth;

            // C. Wrap-around fix: 
            // If the cloud is fully off-screen to the left, move it to the right
            if (xPos < -BIG_CLOUD_WIDTH)
                xPos += bigCloudLoopWidth;
                
            g.drawImage(bigCloud, xPos, (int)(204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        // --- 2. Small Clouds (Infinite Loop) ---
        // Calculate total width covered by your small clouds array
        int smallCloudGap = SMALL_CLOUD_WIDTH * 4;
        int smallCloudLoopWidth = smallCloudsPos.length * smallCloudGap;

        for (int i = 0; i < smallCloudsPos.length; i++) {
            // A. Calculate standard parallax position
            int xPos = (smallCloudGap * i) - (int)(xLvlOffset * 0.6f);
            
            // B. Modulo cycle
            xPos = xPos % smallCloudLoopWidth;
            
            // C. Wrap-around fix
            if (xPos < -SMALL_CLOUD_WIDTH) 
                xPos += smallCloudLoopWidth;

            g.drawImage(smallCloud, xPos, smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void resetAll() { // Resets all game managers and player stats when restarting a level.
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObject();
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float attackBox){
        objectManager.checkObjectHit(attackBox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    // --- Input and Event Handling ---
    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                // 1. If PAUSED, send click to the menu
                pauseOverlay.mousePressed(e);
            } 
            else if(lvlCompleted)
                    levelCompletedOverlay.mousePressed(e);
            else {
                // 2. If NOT PAUSED, allow player to attack
                if (e.getButton() == MouseEvent.BUTTON1) {
                    player.setAttacking(true);
                }
            }
        }
        else{
            gameOverOverLay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                // Buttons generally need a 'Release' event to trigger
                pauseOverlay.mouseReleased(e);
            }
            else if(lvlCompleted)
                    levelCompletedOverlay.mouseReleased(e);
        }
        else{
            gameOverOverLay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                // Hover effects
                pauseOverlay.mouseMoved(e);
            }
            else if(lvlCompleted)
                    levelCompletedOverlay.mouseMoved(e);
        }
        else{
            gameOverOverLay.mouseMoved(e);
        }
    }

    public void setLevelCompleted(boolean levelCompleted){
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame(){
        paused = false;

    }


    @Override
    public void keyPressed(KeyEvent e) {
    if(gameOver)
        gameOverOverLay.keyPressed(e);
    else
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;    
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;     
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void mouseDragged(MouseEvent e){
        if (!gameOver) {
            if (paused) {
                // Volume slider needs dragging
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    // --- Getters and Setters for Sub-Managers ---
    public void windowFocusLost(){
        player.resetDirBooleans();
    }
    public Player getPlayer(){
        return player;
    }
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }
    public ObjectManager getObjectManager(){
        return objectManager;
    }
    public LevelManager getLevelManager(){
        return levelManager;
    }
    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

}
