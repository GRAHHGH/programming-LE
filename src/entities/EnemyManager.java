package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import levels.Level;
import static utilz.Constants.EnemyConstants.*;

// Manages all enemy instances, handling their animations, updates, and interactions with the player.
public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] crabbyArr;
	private ArrayList<Crabby> crabbies = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void LoadEnemies(Level level) { // Populates the enemy list with data parsed from the current level image
        crabbies = level.getCrabs();
    }

    public void update(int[][] lvlData, Player player) { // Updates all active enemies and checks if the level has been cleared
		boolean isAnyActive = false;
		for (Crabby c : crabbies)
			if(c.isActive()){
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if(!isAnyActive) // If no enemies remain alive, signal that the level is complete
			playing.setLevelCompleted(true);
			
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
	}



	private void drawCrabs(Graphics g, int xLvlOffset) { // Renders each active crab, applying offsets for camera scrolling and direction flipping
		for (Crabby c : crabbies) 
			if(c.isActive()){
			g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().getX() - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(), (int) c.getHitbox().getY()- CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
			c.drawAttackBox(g, xLvlOffset);
		}
	}

	public void checkEnemyHit(Rectangle2D.Float AttackBox){ // Checks if the player's attack box intersects with any living enemy hitboxes
		for(Crabby c : crabbies)
			if(c.isActive())
				if(c.getEnemyState() != DEAD)
					if(AttackBox.intersects(c.getHitbox())){
						c.hurt(10);
						return;
			}
	}

	// Slices the Crabby spritesheet into a 2D array based on states (rows) and frames (columns).
	private void loadEnemyImgs() {
		crabbyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		for (int j = 0; j < crabbyArr.length; j++)
			for (int i = 0; i < crabbyArr[j].length; i++)
				crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
	}

	// Resets all enemies to their spawn points and full health for level restarts.
    public void resetAllEnemies() {
		for(Crabby c : crabbies)
			c.resetEnemy();
    }
}