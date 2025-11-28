package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprites;
    private Level levelOne;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }
    

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas("res/outside.png");
        levelSprites = new BufferedImage[2];
            for(int i = 0; i < 2; i++){
                levelSprites[i] = img.getSubimage(i*32, 0, 32, 32);
            }
    }


    public void draw(Graphics g, int lvlOffset){
        for(int j = 0 ; j < Game.TILES_IN_HEIGHT; j++)
            for(int i = 0; i < levelOne.getLevelData()[0].length; i++){
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprites[index], Game.TILES_SIZE*i - lvlOffset, Game.TILES_SIZE*j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void update(){

    }
    public Level getCurrentLevel(){
        return levelOne;
    }
}
