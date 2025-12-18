package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

// Captures mouse hardware inputs and delegates them to the appropriate gamestate classes.
// basically here contains the function of the mouse and responsible to trigger the animation in buttons
public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) { // triggers when mouse is clicked
        switch(Gamestate.state){
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:
                break;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) { // triggers when mouse is pressed
        switch(Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mousePressed(e);
                break;
            default:
                break;
        }

        if (Gamestate.state == Gamestate.PLAYING) {
            // 2. Check if the user clicked the LEFT mouse button (Button 1)
            if (e.getButton() == MouseEvent.BUTTON1) {
                
                // 3. Trigger the attack
                // Chain: GamePanel -> Game -> Playing -> Player
                gamePanel.getGame().getPlaying().getPlayer().setAttacking(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) { // triggers when mouse is released
        switch(Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseReleased(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {  // triggers when mouse is dragged
        switch(Gamestate.state){
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseDragged(e);
                break;
            default:
                break;
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) { // triggers when mouse is moved
        switch(Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseMoved(e);
                break;
            default:
                break;
        }
    }
    
}
