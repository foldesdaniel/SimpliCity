package simplicity;

import com.formdev.flatlaf.FlatDarculaLaf;
import simplicity.Model.GameModel;
import simplicity.Model.Persistence.Persistence;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", GameModel.GAME_TITLE);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(GameModel.CUSTOM_FONT);
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        GameWindow gameWindow = new GameWindow();
        gameWindow.setVisible(true);
    }



}
