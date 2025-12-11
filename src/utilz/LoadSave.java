package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {
    
    public static final String PLAYER_ATLAS = "res/finalCharacter.png";
    public static final String LEVEL_ATLAS = "res/outside.png";
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

    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/res/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; i++)
            for(int j = 0; j < files.length; j++){
                if(files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }



}
