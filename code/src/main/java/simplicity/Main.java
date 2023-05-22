package simplicity;

import com.formdev.flatlaf.FlatDarculaLaf;
import simplicity.Model.GameModel;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", GameModel.GAME_TITLE);
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(GameModel.CUSTOM_FONT);
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        GameWindow gameWindow = new GameWindow();
        gameWindow.setVisible(true);
    }


}
