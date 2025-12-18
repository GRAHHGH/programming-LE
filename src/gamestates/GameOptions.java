package gamestates;

import static utilz.Constants.UI.URMButtons.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;

// Manages the Options menu state, including background rendering and audio settings 
public class GameOptions extends State implements Statemethods{

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgrondImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;

    public GameOptions(Game game) { // imports all necessary graphic assests for the buttons
        super(game);
        loadImgs();
        loadButton();
        audioOptions = game.getAudioOptions(); // if in game there some changes, it will save in menu
    }

    private void loadButton() {
        int menuX = (int)(387 * Game.SCALE);
        int menuY = (int)(325 * Game.SCALE);
        // Initializes the UrmButton with a specific type index for visuals
        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgrondImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
        // Scales background box dimensions and centers it horizontally
        bgW = (int)(optionsBackgrondImg.getWidth() * Game.SCALE);
        bgH = (int)(optionsBackgrondImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(33 * Game.SCALE);
    }

    @Override
    public void update() { //updated the animation, hoverstates, and audio silder logic
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) { // renders the full screen background, menu, navigation button, and audio controls
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgrondImg, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }


    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)){
            menuB.setMousePressed(true);
        }else
            audioOptions.mousePressed(e);
            
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)){
            if(menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        }
        else{
            audioOptions.mouseReleased(e);
        }
        menuB.resetBools();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if(isIn(e, menuB))
            menuB.setMouseOver(true);
        else    
            audioOptions.mouseMoved(e);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(), e.getY());
    }
    
}
