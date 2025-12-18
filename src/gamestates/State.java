package gamestates;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

public class State { // A base class that provides common variables and methods for all game states.

    protected Game game;
    public State(Game game){
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb){ // Determines if a mouse event occurred within the rectangular bounds of a button.
		return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame(){
        return game;
    }

    public void setGameState(Gamestate state){ // Updates the global gamestate and automatically switches the music to match.
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }
}
