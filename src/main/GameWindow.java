package main;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame; 

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){ // GameWindow creates the physical OS window that contains the game's drawing panel.
        jframe = new JFrame();

        jframe.add(gamePanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true); 
        jframe.setAlwaysOnTop(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the application terminates completely when the "X" button is clicked

        
        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                 gamePanel.getGame().windowFocusLost();
            }
            
        });


        }
}
 