package simplicity.Model.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * ResourceLoader
 */
public class ResourceLoader {
    
    /**
     * Betölt egy képet a projektmappából
     * @param resName a fájl neve
     * @return a képet tartalmazó Image objektum
     */
    public static Image loadImage(String resName) {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        try{
            return ImageIO.read(url);
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    
}
