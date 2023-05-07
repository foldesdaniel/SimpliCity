package simplicity.View.Menu;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class MainMenu extends JPanel {

    private ArrayList<MenuEventListener> menuEventListeners;
    private ArrayList<StartStopGameListener> startGameListeners;

    /**
     * Constructor
     */
    public MainMenu() {
        this.menuEventListeners = new ArrayList<>();
        this.startGameListeners = new ArrayList<>();

        this.updateMenuSize();
        //this.setLayout(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(100, 100, 100));
        this.setVisible(true);

        displayButtons();
    }

    public void addMenuEventListener(MenuEventListener menuEventListener){
        this.menuEventListeners.add(menuEventListener);
    }

    public void addStartGameListener(StartStopGameListener startGameListener){
        this.startGameListeners.add(startGameListener);
    }

    /**
     * Displays the simplicity.Main Menu buttons.
     */
    public void displayButtons() {
        this.removeAll();
        this.revalidate();
        this.repaint();

        int windowHeight = this.getHeight();
        int gap = windowHeight/32;

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.setOpaque(false);

        //Load game
        MenuButton loadGame_btn = new MenuButton("LOAD GAME");
        loadGame_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        //TODO -> implement actionlistener to display loading list
        //mainMenuPanel.add(loadGame_btn);
        //mainMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //New game
        MenuButton newGame_btn = new MenuButton("NEW GAME");
        newGame_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame_btn.addActionListener(e -> displayInputCityName());
        mainMenuPanel.add(newGame_btn);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Settings
        MenuButton settings_btn = new MenuButton("SETTINGS");
        settings_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        settings_btn.addActionListener(e -> displaySettings(false, true));
        mainMenuPanel.add(settings_btn);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Exit
        MenuButton exit_btn = new MenuButton("EXIT");
        exit_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_btn.addActionListener(e -> System.exit(0));
        mainMenuPanel.add(exit_btn);

        this.add(Box.createVerticalGlue());
        this.add(mainMenuPanel, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Displays the Input city name page after selecting New Game.
     */
    private void displayInputCityName() {
        this.removeAll();
        this.revalidate();
        this.repaint();

        int windowHeight = this.getHeight();
        int gap = windowHeight/40;

        JPanel newGamePanel = new JPanel();
        newGamePanel.setLayout(new BoxLayout(newGamePanel, BoxLayout.Y_AXIS));
        newGamePanel.setOpaque(false);

        //Label
        MenuLabel cityName_lbl = new MenuLabel("Please enter your city name");
        cityName_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGamePanel.add(cityName_lbl);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Input field
        MenuTextField input = new MenuTextField();
        input.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGamePanel.add(input);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Start
        MenuButton start_btn = new MenuButton("START");
        start_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        start_btn.addActionListener((ActionEvent) -> startGame());
        newGamePanel.add(start_btn);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        back_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_btn.addActionListener(e -> displayButtons());
        newGamePanel.add(back_btn);

        this.add(Box.createVerticalGlue());
        this.add(newGamePanel, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Displays the actual game panel
     */
    private void startGame(){
        for(StartStopGameListener l : startGameListeners) l.onGameStart();
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

        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        int gap = windowHeight/40;

        JPanel settingsMenu = new JPanel();
        settingsMenu.setLayout(new BoxLayout(settingsMenu, BoxLayout.Y_AXIS));
        settingsMenu.setOpaque(false);

        //Label
        MenuLabel res_lbl = new MenuLabel("Resolution");
        res_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsMenu.add(res_lbl);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Resolution type
        String options[] = {"Windowed Fullscreen", "Windowed"};
        MenuComboBox res_type = new MenuComboBox(options);
        res_type.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        res_type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    Object selected = res_type.getSelectedItem();
                    displaySettings("Windowed".equals(selected), false);
                }
            }
        });
        settingsMenu.add(res_type);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Resolutions
        options = new String[] {"960x540", "1280x720", "1600x900"};
        MenuComboBox resolutions = new MenuComboBox(display.equals("Windowed") ? options : new String[]{});
        resolutions.setAlignmentX(Component.CENTER_ALIGNMENT);
        resolutions.setEnabled(display.equals("Windowed"));
        if (display.equals("Windowed")) {
            for (String res : options) {
                if (parseInt(res.split("x")[0]) == windowWidth) {
                    resolutions.setSelectedItem(res);
                }
            }
        }
        settingsMenu.add(resolutions);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Apply
        MenuButton apply_btn = new MenuButton("APPLY");
        apply_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (display.equals("Windowed")) {
            apply_btn.addActionListener(e -> {
                String res = String.valueOf(resolutions.getSelectedItem());
                int newWidth = parseInt(res.split("x")[0]);
                int newHeight = parseInt(res.split("x")[1]);
                this.setWindowSize(newWidth, newHeight, false);
            });
        }
        else apply_btn.addActionListener(e -> this.setWindowSize(windowWidth, windowHeight, true));
        settingsMenu.add(apply_btn);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        back_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_btn.addActionListener(e -> displayButtons());
        settingsMenu.add(back_btn);

        this.add(Box.createVerticalGlue());
        this.add(settingsMenu, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
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
        updateMenuSize();
        displayButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = GameModel.BACKGROUND_IMG;
        Dimension originalImgSize = new Dimension(img.getWidth(null),img.getHeight(null));
        double imgRatio = originalImgSize.width / (double) originalImgSize.height;
        double ratio = this.getWidth() / (double) this.getHeight();
        int imgWidth = (int)Math.ceil(
            (imgRatio > ratio) ? this.getHeight()*imgRatio : this.getWidth()
        );
        int imgHeight = (int)Math.ceil(
            (imgRatio > ratio) ? this.getHeight() : this.getWidth()/imgRatio
        );
        // background image will always be centered and cover the panel
        g.drawImage(img, (this.getWidth()/2)-(imgWidth/2), (this.getHeight()/2)-(imgHeight/2), imgWidth, imgHeight, null);
    }

    private void updateMenuSize(){
        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        this.setSize(new Dimension(windowWidth, windowHeight));
    }

}