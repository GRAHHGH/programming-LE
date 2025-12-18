package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.Keyboardinputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

// GamePanel is the "canvas" where the game is actually drawn and where inputs are captured.
public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game){
        mouseInputs = new MouseInputs(this); // Handles all mouse-related events (clicks, movement)
        this.game = game;

        setPanelSize();
        addKeyListener(new Keyboardinputs(this)); // for keyboard presses
        addMouseListener(mouseInputs); // for mouse clicks and release
        addMouseMotionListener(mouseInputs); // for mouse movement and dragging 
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT); // Creates a dimension object using the global GAME_WIDTH and GAME_HEIGHT constants
        setPreferredSize(size);
        System.out.println(GAME_WIDTH + " " + GAME_HEIGHT);
    }

    // This is a standard Swing method that the system calls whenever the panel needs to be redrawn.
    public void paintComponent(Graphics g){
        super.paintComponent(g); // Clears the screen so the next frame can be drawn over it
        game.render(g); // Calls the main game render loop to draw the level, players, and UI
    
    }

    public Game getGame(){
        return game; // Allows input listeners to access the game state for logic processing
    }



}

