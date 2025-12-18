package levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import static utilz.HelpMethods.GetPlayerSpawn;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.ObjectConstants.*;


public class Level { // The Level class parses a level image and stores all relevant data for tiles, entities, and objects

    private BufferedImage img;
    private int[][] lvlData; // Stores tile IDs for rendering and collision

    // Lists to hold instances of entities and interactive objects
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;     
    private Point playerSpawn;

    public Level(BufferedImage img){ 
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        createLists();
        loadLevel();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    // Initializes all ArrayLists to prevent null pointer errors when adding objects
    private void createLists() {
        crabs = new ArrayList<>();
        potions = new ArrayList<>();
        spikes = new ArrayList<>();
        containers = new ArrayList<>();
        cannons = new ArrayList<>();
    }

    // Loops through every pixel of the level image and extracts RGB channels
    private void loadLevel(){
        for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
    }

    // Maps the Red pixel channel to the tile data array
    private void loadLevelData(int redValue, int x, int y){
        if (redValue >= 50)
			lvlData[y][x] = 0;
		else
			lvlData[y][x] = redValue;
    }

    // Checks the Green pixel channel to spawn enemies
    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue){
            case CRABBY -> crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
        }
    }

    // Checks the Blue pixel channel to spawn interactive game objects
    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT -> cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    // Scans the image for the specific player spawn color ID
    private void calcPlayerSpawn() { 
        playerSpawn = GetPlayerSpawn(img);
    }

    // Getters for LevelManager and other classes to access the loaded level data
    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    public int[][] getLevelData(){
        return lvlData;
    }
    public  int getLvlOffset(){
        return maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
    public Point getPlayerSpawn(){
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions(){
        return potions;
    }
    public ArrayList<GameContainer> getContainers(){
        return containers;
    }
    public ArrayList<Spike> getSpikes(){
        return spikes;
    }
    public ArrayList<Cannon> getCannons(){
        return cannons;
    }
}
