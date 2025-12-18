package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

// Manages the loading, switching, and drawing of all game levels
public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprites;
    private ArrayList<Level> levels;
    private  int lvlIndex = 0; // Tracks the current active level

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites(); // Slices the spritesheet into individual tiles
        levels = new ArrayList<>();
        buildAlllevels(); // Initializes all Level objects from the res/lvls folder
    }
    
    // Advances the game to the next level and resets entities/objects for the new map.
    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("NO more levels! Congratulations");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        // Update all managers with the new level's specific data
        game.getPlaying().getEnemyManager().LoadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    // Fetches all level images and creates a new Level object for each one
    private void buildAlllevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    // Slices the master tile atlas into sub-images for easy index-based drawing
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprites = new BufferedImage[48];
		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprites[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
	}

    // Renders the tilemap to the screen, applying the camera offset for scrolling
	public void draw(Graphics g, int lvlOffset) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				g.drawImage(levelSprites[index], Game.TILES_SIZE * i - lvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}

    // Getters for accessing level state and count
    public void update(){
        // reserved for future updates (plan was keep updating after break)
    }
    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }
    public int getAmountOfLevels(){
        return levels.size();
    }
    public int getLevelIndex(){
        return lvlIndex;
    }
}
