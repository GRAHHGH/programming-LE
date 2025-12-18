package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

// Represents the circular control buttons (Unpause, Replay, Menu) used in overlays
// Extends PauseButton to inherit standard positioning and collision logic
public class UrmButton extends PauseButton {
    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) { // to load the Urm Buttons
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() { // same thing, to get the buttons from res folder
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i*URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
    }

    public void update(){
    index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    public void draw(Graphics g){ // to draw the button inside the game 
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

}
