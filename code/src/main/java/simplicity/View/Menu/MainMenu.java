package simplicity.View.Menu;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.Model.Listeners.StartGameListener;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class MainMenu extends JPanel {

    private ArrayList<MenuEventListener> menuEventListeners;
    private ArrayList<StartGameListener> startGameListeners;

    /**
     * Constructor
     */
    public MainMenu() {
        this.menuEventListeners = new ArrayList<>();
        this.startGameListeners = new ArrayList<>();

        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        this.setLayout(null);
        this.setSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(new Color(100, 100, 100));
        this.setVisible(true);

        displayButtons();
    }

    public void addMenuEventListener(MenuEventListener menuEventListener){
        this.menuEventListeners.add(menuEventListener);
    }

    public void addStartGameListener(StartGameListener startGameListener){
        this.startGameListeners.add(startGameListener);
    }

    /**
     * Displays the simplicity.Main Menu buttons.
     */
    private void displayButtons() {
        this.removeAll();
        this.repaint();

        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        int gap = windowHeight/8;
        int width = windowWidth/5;
        int height = windowHeight/12;
        int x, y;

        //Load game
        MenuButton loadGame_btn = new MenuButton("LOAD GAME");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - gap/2 - height/2;
        loadGame_btn.setBounds(x, y, width, height);
        //TODO -> implement actionlistener to display loading list
        this.add(loadGame_btn);

        //New game
        MenuButton newGame_btn = new MenuButton("NEW GAME");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - gap/2 - gap - height/2;
        newGame_btn.setBounds(x, y, width, height);
        newGame_btn.addActionListener(e -> displayInputCityName());
        this.add(newGame_btn);

        //Settings
        MenuButton settings_btn = new MenuButton("SETTINGS");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 + gap/2 - height/2;
        settings_btn.setBounds(x, y, width, height);
        settings_btn.addActionListener(e -> displaySettings(false, true));
        this.add(settings_btn);

        //Exit
        MenuButton exit_btn = new MenuButton("EXIT");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 + gap/2 + gap - height/2;
        exit_btn.setBounds(x, y, width, height);
        exit_btn.addActionListener(e -> System.exit(0));
        this.add(exit_btn);
    }

    /**
     * Displays the Input city name page after selecting New Game.
     */
    private void displayInputCityName() {
        this.removeAll();
        this.repaint();

        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        int gap = windowHeight/11;
        int width;
        int height = windowHeight/14;

        int x, y;

        //Label
        MenuLabel cityName_lbl = new MenuLabel("Please enter your city name");
        width = windowWidth/5;
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - (gap*2) - height/2;
        cityName_lbl.setBounds(x, y, width, height);
        this.add(cityName_lbl);

        //Input field
        MenuTextField input = new MenuTextField();
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - height/2 - gap;
        input.setBounds(x, y, width, height/2);
        this.add(input);

        //Start
        MenuButton start_btn = new MenuButton("START");
        width = windowWidth/8;
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - height/2;
        start_btn.setBounds(x, y, width, height);
        start_btn.addActionListener((ActionEvent) -> startGame());
        this.add(start_btn);

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 + gap - height/2;
        back_btn.setBounds(x, y, width, height);
        back_btn.addActionListener(e -> displayButtons());
        this.add(back_btn);
    }

    /**
     * Displays the actual game panel
     */
    private void startGame(){
        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        for(StartGameListener l : startGameListeners) l.onGameStart();
    }

    /**
     * Displays the settings page after selecting Settings.
     *
     * @param displayResolutions
     *            If the window is in Windowed Fullscreen state at the moment
     *            then it will not display the pickable resolutions, instead it
     *            will disable that ComboBox.
     * @param firstTime
     *            Will be true if we called this method from the simplicity.Main Menu page,
     *            so we can show the currently used display type and resolution.
     *            Will be false if we called this method inside this method,
     *            so we can show the changes we made previously (not the currently
     *            used display type and resolution).
     */
    private void displaySettings(boolean displayResolutions, boolean firstTime) {
        this.removeAll();
        this.revalidate();
        this.repaint();

        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        int gap = windowHeight/8;
        int width = windowWidth/5;
        int height = windowHeight/12;
        int x, y;

        //Label
        MenuLabel res_lbl = new MenuLabel("Resolution");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - gap/2 - gap - gap/2 - height/2;
        res_lbl.setBounds(x, y, width, height);
        this.add(res_lbl);

        //Resolution type
        String options[] = {"Windowed Fullscreen", "Windowed"};
        MenuComboBox res_type = new MenuComboBox(options);
        String display;
        if (!firstTime) {
            display = displayResolutions ? "Windowed" : "Windowed Fullscreen";
            res_type.setSelectedItem(display);
        }
        else {
            int fullScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            display = (windowWidth == fullScreenWidth) ? "Windowed Fullscreen" : "Windowed";
            res_type.setSelectedItem(display);
        }
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - gap/2 - gap - height/2;
        res_type.setBounds(x, y, width, height);
        res_type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    Object selected = res_type.getSelectedItem();
                    displaySettings("Windowed".equals(selected), false);
                }
            }
        });
        this.add(res_type);

        //Resolutions
        options = new String[] {"960x540", "1280x720", "1600x900"};
        MenuComboBox resolutions = new MenuComboBox(display.equals("Windowed") ? options : new String[]{});
        x = windowWidth/2 - width/2;
        y = windowHeight/2 - gap/2 - height/2;
        resolutions.setBounds(x, y, width, height);
        resolutions.setEnabled(display.equals("Windowed"));
        if (display.equals("Windowed")) {
            for (String res : options) {
                if (parseInt(res.split("x")[0]) == windowWidth) {
                    resolutions.setSelectedItem(res);
                }
            }
        }
        this.add(resolutions);

        //Apply
        MenuButton apply_btn = new MenuButton("APPLY");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 + gap/2 - height/2;
        apply_btn.setBounds(x, y, width, height);

        if (display.equals("Windowed")) {
            apply_btn.addActionListener(e -> {
                String res = String.valueOf(resolutions.getSelectedItem());
                int newWidth = parseInt(res.split("x")[0]);
                int newHeight = parseInt(res.split("x")[1]);
                this.setWindowSize(newWidth, newHeight, false);
            });
        }
        else apply_btn.addActionListener(e -> this.setWindowSize(windowWidth, windowHeight, true));
        this.add(apply_btn);

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        x = windowWidth/2 - width/2;
        y = windowHeight/2 + gap/2 + gap - height/2;
        back_btn.setBounds(x, y, width, height);
        back_btn.addActionListener(e -> displayButtons());
        this.add(back_btn);
    }

    /**
     * Sets simplicity.Main Menu window size.
     *
     * @param width
     *            simplicity.Main Menu window will be resized to this width.
     * @param height
     *            simplicity.Main Menu window will be resized to this height.
     */
    private void setWindowSize(int width, int height, boolean fullscreen) {
        if(fullscreen){
            for(MenuEventListener l : menuEventListeners) l.changedFullscreen();
        }else{
            for(MenuEventListener l : menuEventListeners) l.changedWindowed(width, height);
        }
        displayButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(GameModel.BACKGROUND_IMG, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}