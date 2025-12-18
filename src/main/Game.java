package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Playing;
import ui.AudioOptions;
import gamestates.Menu;
public class Game implements Runnable {

    // --- Core Window and Threading Components ---
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120; // Target frames per second (rendering frequency)
    private final int UPS_SET = 200; // Target updates per second (physics frequency)

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 32; // Base size of a single tile in pixels
    public final static float SCALE = 1.5f; // Universal scaling factor for graphics/hitboxes
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT= 14;
    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    public static boolean DRAW_HITBOXES = false;

    public Game(){
        initClasses(); // Initialize all game components and states
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop(); // Begin the main game thread
    }

    private void initClasses() { // this class initializes audio, sound engine, main menu, the game logic, and menu logic
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){
        // Update game logic based on the currently active stat
        switch(Gamestate.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
                System.exit(0);
            default:
                break;         
        }
        
    }

    public void render(Graphics g){
        // Render visuals based on the currently active state
            switch(Gamestate.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;         
        }
        
    }

    @Override
    public void run() {
        // Calculate the nanosecond interval per frame and per update
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0; // Accumulator for physics updates
        double deltaF = 0; // Accumulator for frame rendering

        while(true){
            long currentTime = System.nanoTime();

            // Track time passed and add to update/frame accumulators
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1){ // Perform logic updates when the UPS threshold is met
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){ // Repaint the screen when the FPS threshold is met
                gamePanel.repaint();
                frames++;
                deltaF--;
            }


        if(System.currentTimeMillis() - lastCheck >= 1000){ // Print FPS and UPS performance to console every second
            lastCheck = System.currentTimeMillis();
            System.out.println("FPS: " + frames + " | UPS: " + updates);
            frames = 0;
            updates = 0;
        }

        }
    }

    public void windowFocusLost(){
        if(Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }

    // --- Getters for specialized managers and states ---
    public Menu getMenu(){
        return menu;
    }
     public Playing getPlaying(){
        return playing;
    }
    public GameOptions getGameOptions(){
        return gameOptions;
    }
    public AudioOptions getAudioOptions(){
        return audioOptions;
    }
    public AudioPlayer getAudioPlayer(){
        return audioPlayer;
    }
}
  