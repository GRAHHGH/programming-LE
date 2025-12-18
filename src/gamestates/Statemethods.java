package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Statemethods {
    // Core game loop methods
    public void update();
    public void draw(Graphics g);

    // Mouse input requirements
    public void mouseClicked(MouseEvent e);
    public void mousePressed(MouseEvent e);
    public void mouseReleased(MouseEvent e);
    public void mouseMoved(MouseEvent e);

    // Keyboard input requirements
    public void keyPressed(KeyEvent e);
    public void keyReleased(KeyEvent e);
}

/*
A contract that forces all gamestate classes to implement a standard set
of methods for updates, rendering, and input handling.
*/