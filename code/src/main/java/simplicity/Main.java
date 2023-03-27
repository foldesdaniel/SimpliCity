package simplicity;

import com.formdev.flatlaf.FlatDarculaLaf;
import lombok.Getter;
import lombok.Setter;
import simplicity.Model.GameModel;
import simplicity.View.GameWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", GameModel.GAME_TITLE);
        try{
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        }catch(Exception ex){
            System.err.println("Failed to initialize FlatLaf");
        }
        GameWindow gameWindow = new GameWindow();
        gameWindow.setVisible(true);
    }

}
