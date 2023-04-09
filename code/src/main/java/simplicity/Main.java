package simplicity;

import com.formdev.flatlaf.FlatDarculaLaf;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Zones.Residential;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", GameModel.GAME_TITLE);
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
//        GameModel gameModel = new GameModel();
        GameWindow gameWindow = new GameWindow();
        gameWindow.setVisible(true);
    }

}
