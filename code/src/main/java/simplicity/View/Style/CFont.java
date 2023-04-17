package simplicity.View.Style;

import simplicity.Model.GameModel;

import java.awt.*;

public class CFont {

    public static Font get(int style, int size){
        return GameModel.CUSTOM_FONT.deriveFont(style, size);
    }

    public static Font get(int style){
        return CFont.get(style, 18);
    }

    public static Font get(){
        return CFont.get(Font.PLAIN);
    }

}
