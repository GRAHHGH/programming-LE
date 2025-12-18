package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {
    
    // --- Resource Path Constants ---
    public static final String PLAYER_ATLAS = "res/finalCharacter.png";
    public static final String LEVEL_ATLAS = "res/outside_sprites.png";
    public static final String LEVEL_DESIGN_ATLAS = "res/design_blocks.png";
    public static final String MENU_BUTTONS = "res/Buttons.png";
    public static final String MENU_BACKGROUND = "res/menubackground.png";
    public static final String PAUSE_BACKGROUND = "res/pause_menu.png";
    public static final String SOUND_BUTTONS = "res/sound_button.png";
    public static final String URM_BUTTONS = "res/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "res/volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "res/background_menu.png";
    public static final String PLAYING_BACKGROUND_IMG = "res/playing_bg_img.png";
    public static final String BIG_CLOUDS = "res/big_clouds.png";
    public static final String SMALL_CLOUDS = "res/small_clouds.png";
    public static final String CRABBY_SPRITE = "res/crabby_sprite.png";
    public static final String STATUS_BAR = "res/health_power_bar.png";
    public static final String COMPLETED_IMG = "res/completed_sprite.png";
    public static final String POTION_ATLAS = "res/potions_sprites.png";
    public static final String CONTAINER_ATLAS = "res/objects_sprites.png";
    public static final String TRAP_ATLAS = "res/trap_atlas.png";
    public static final String CANNON_ATLAS = "res/cannon_atlas.png";
    public static final String CANNON_BALL = "res/ball.png";
    public static final String DEATH_SCREEN = "res/death_screen.png";
    public static final String OPTIONS_MENU = "res/options_background.png";

    public static BufferedImage GetSpriteAtlas(String fileName){
    BufferedImage img = null;
    InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            if (is == null) {
                System.err.println("Could not find image: " + fileName);
            } else {
                img = ImageIO.read(is);
        
            }
        } 
        catch (IOException e) { 
            e.printStackTrace();
        } 
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }
        return img;
    }
    
    // Finds and loads all level images from the 'res/lvls' folder.
    // it sorts them numerically (1.png, 2.png, etc.) so levels play in the correct order.
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/res/lvls");
        File file = null;

        try {
            file = new File(url.toURI()); // Convert URL to URI to handle spaces or special characters in folder names
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; i++) // Ensure the directory isn't empty before sorting
            for(int j = 0; j < files.length; j++){
                if(files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i = 0; i < imgs.length; i++) // Read each sorted file into the BufferedImage array
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }



}
