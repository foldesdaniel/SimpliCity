package simplicity.Model.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * ResourceLoader
 */
public class ResourceLoader {

    /**
     * Loads an image from the project folder
     *
     * @param resName file name
     * @return the Image object
     */
    public static Image loadImage(String resName) {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        try {
            return ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Rotates an Image by a given amount
     *
     * @param img     Image object
     * @param degrees degrees to be rotated by
     * @return rotated Image
     */
    public static BufferedImage rotateImage(Image img, int degrees) {
        if (img == null) return null;
        double rotationRequired = Math.toRadians(degrees);
        double locationX = img.getWidth(null) / 2.0;
        double locationY = img.getHeight(null) / 2.0;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter((BufferedImage) img, null);
    }

    /**
     * Loads a font type from the project folder
     *
     * @param fileName font path
     * @return the loaded font
     */
    public static Font loadFont(String fileName) {
        try {
            InputStream fontRes = ResourceLoader.class.getClassLoader().getResourceAsStream(fileName);
            return Font.createFont(Font.TRUETYPE_FONT, fontRes);
        } catch (IOException | FontFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
