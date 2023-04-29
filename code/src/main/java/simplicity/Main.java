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
        GameModel gm;
        {
            try {
                gm = (GameModel) Persistence.load("gm4.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(gm.getPeople().size());
        System.out.println(gm.getCityMood());
        for(Placeable[] p : gm.getGrid()){
            for(Placeable pl : p) {
                if(pl != null) {
                    System.out.println(pl);
                }
            }
        }
    }



}
