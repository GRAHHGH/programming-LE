package ui;

import static utilz.Constants.UI.URMButtons.*;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

// Manages the UI components and interactions when the game is paused
public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundimg;
    private int bgX, bgY, bgW, bgH;
    private AudioOptions audioOptions;
    private UrmButton menuB, replayB, unpauseB;

    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground();
        // Pulls audio options from the main game class for state persistence
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons(); // Initializes navigation buttons
    }

    // Creates and positions the Menu, Replay, and Unpause buttons 
    private void createUrmButtons() {
        int menuX = (int)(313*Game.SCALE);
        int replayY = (int)(387*Game.SCALE);
        int unpauseX = (int)(462*Game.SCALE);
        int bY = (int)(325 * Game.SCALE);
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayY, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    // Logic for button interaction on mouse release
    private void loadBackground() {
        backgroundimg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundimg.getWidth() * Game.SCALE);
        bgH = (int)(backgroundimg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(25 * Game.SCALE);
    }

    public void update(){
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void draw(Graphics g){
        //BackGround
        g.drawImage(backgroundimg, bgX, bgY, bgW, bgH, null);

        // urm buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // volume buttons
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB))
            menuB.setMousePressed(true);
        else if(isIn(e, replayB))
            replayB.setMousePressed(true);
        else if(isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else 
            audioOptions.mousePressed(e);
    }

    // Logic for button interaction on mouse release
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)){
            if(menuB.isMousePressed()){
                playing.resetAll();
                playing.setGameState(Gamestate.MENU);
                playing.unpauseGame();
            }
        }
        else if(isIn(e, replayB)){
            if(replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }
             
        }
        else if(isIn(e, unpauseB)){
            if(unpauseB.isMousePressed())
                playing.unpauseGame();
        }
        else{
            audioOptions.mouseReleased(e);
        }

        menuB.resetBools();    
        replayB.resetBools(); 
        unpauseB.resetBools(); 
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);   
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if(isIn(e, menuB))
            menuB.setMouseOver(true);
        else if(isIn(e, replayB))
            replayB.setMouseOver(true);
        else if(isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else 
            audioOptions.mouseMoved(e);
    }


    // Helper to detect if the mouse is over a button based on its bounds
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    
}
