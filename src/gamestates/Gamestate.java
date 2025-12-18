package gamestates;

public enum Gamestate { // An enumeration representing the different high-level states of the game application.

    PLAYING, MENU, OPTIONS, QUIT;

    // The global variable that tracks the current state, initialized to the MENU
    public static Gamestate state = MENU;
    
}

// this allows any class to change the entire behavior of the game by simply updating the gamestate.state
 