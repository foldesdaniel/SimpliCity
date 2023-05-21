package simplicity.View.Menu;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.Model.Persistence.SaveEntries;
import simplicity.Model.Persistence.SaveEntry;
import simplicity.View.GameWindow;
import simplicity.View.Style.CFont;
import simplicity.View.Style.MenuScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class MainMenu extends JPanel {

    private final ArrayList<MenuEventListener> menuEventListeners;
    private final ArrayList<StartStopGameListener> startGameListeners;

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

    public void addMenuEventListener(MenuEventListener menuEventListener) {
        this.menuEventListeners.add(menuEventListener);
    }

    public void addStartGameListener(StartStopGameListener startGameListener) {
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
        int gap = windowHeight / 32;

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 32)));

        // Title
        JLabel title = new JLabel();
        title.setFont(CFont.get(Font.BOLD, 72));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setIcon(new ImageIcon(GameModel.LOGO_IMG));
        mainMenuPanel.add(title);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //New game
        MenuButton newGame_btn = new MenuButton("NEW GAME");
        newGame_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame_btn.addActionListener(e -> displayInputCityName());
        mainMenuPanel.add(newGame_btn);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Load game
        MenuButton loadGame_btn = new MenuButton("LOAD GAME");
        loadGame_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadGame_btn.addActionListener(e -> displayLoad());
        mainMenuPanel.add(loadGame_btn);
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
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 32)));

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
        int gap = windowHeight / 40;

        JPanel newGamePanel = new JPanel();
        newGamePanel.setLayout(new BoxLayout(newGamePanel, BoxLayout.Y_AXIS));
        newGamePanel.setOpaque(false);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, 32)));

        //Label
        MenuLabel cityName_lbl = new MenuLabel("Please enter your city name");
        cityName_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGamePanel.add(cityName_lbl);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Input field
        MenuTextField input = new MenuTextField();
        input.setAlignmentX(Component.CENTER_ALIGNMENT);
        input.addActionListener((ActionEvent) -> startGame(input.getText()));
        newGamePanel.add(input);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Start
        MenuButton start_btn = new MenuButton("START");
        start_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        start_btn.addActionListener((ActionEvent) -> startGame(input.getText()));
        newGamePanel.add(start_btn);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        back_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_btn.addActionListener(e -> displayButtons());
        newGamePanel.add(back_btn);
        newGamePanel.add(Box.createRigidArea(new Dimension(0, 32)));

        this.add(Box.createVerticalGlue());
        this.add(newGamePanel, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Displays the load menu.
     */
    public void displayLoad() {
        this.removeAll();
        this.revalidate();
        this.repaint();

        int windowHeight = this.getHeight();
        int gap = windowHeight / 32;

        JPanel loadMenuPanel = new JPanel();
        loadMenuPanel.setLayout(new BoxLayout(loadMenuPanel, BoxLayout.Y_AXIS));
        loadMenuPanel.setOpaque(false);
        loadMenuPanel.add(Box.createRigidArea(new Dimension(0, 32)));

        //Label
        MenuLabel load_lbl = new MenuLabel("Load a saved game");
        load_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadMenuPanel.add(load_lbl);
        loadMenuPanel.add(Box.createRigidArea(new Dimension(0, gap)));

        //List
        ArrayList<SaveEntry> entries = SaveEntries.getInstance().getSaveEntries();
        entries.sort(new Comparator<SaveEntry>() {
            @Override
            public int compare(SaveEntry o1, SaveEntry o2) {
                return (int) (o2.getModifyDate() - o1.getModifyDate());
            }
        });
        if (entries.size() > 0) {
            JPanel saveBoxes = new JPanel();
            saveBoxes.setOpaque(false);
            saveBoxes.setLayout(new BoxLayout(saveBoxes, BoxLayout.Y_AXIS));
            for (SaveEntry entry : entries) {
                JPanel saveBox = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics _g) {
                        super.paintComponent(_g);
                        Graphics2D g = (Graphics2D) _g;
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(new Color(0, 0, 0, 128));
                        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    }
                };
                saveBox.add(Box.createVerticalGlue());
                saveBox.setOpaque(false);
                saveBox.setLayout(new BoxLayout(saveBox, BoxLayout.Y_AXIS));
                Dimension saveBoxSize = new Dimension(Math.min((int) Math.round(GameWindow.getWindowWidth() * 0.7), 672), 50);
                saveBox.setSize(saveBoxSize);
                saveBox.setMinimumSize(saveBoxSize);
                saveBox.setMaximumSize(new Dimension(saveBoxSize.width, 200));
                saveBox.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel saveLabel = new JLabel("===== " + entry.getCityName() + " =====");
                saveLabel.setFont(CFont.get(Font.BOLD, 18));
                saveLabel.setVerticalAlignment(SwingConstants.CENTER);
                saveLabel.setHorizontalAlignment(SwingConstants.CENTER);
                saveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Font smallFont = CFont.get(Font.BOLD, 16);

                JLabel modifiedLabel = new JLabel("Modified: " + df.format(new Date(entry.getModifyDate())));
                modifiedLabel.setFont(smallFont);
                modifiedLabel.setVerticalAlignment(SwingConstants.CENTER);
                modifiedLabel.setHorizontalAlignment(SwingConstants.CENTER);
                modifiedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel createdLabel = new JLabel("Created: " + df.format(new Date(entry.getSaveDate())));
                createdLabel.setFont(smallFont);
                createdLabel.setVerticalAlignment(SwingConstants.CENTER);
                createdLabel.setHorizontalAlignment(SwingConstants.CENTER);
                createdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel saveNumber = new JLabel("Number of saves: " + entry.getNumberOfSaves());
                saveNumber.setFont(smallFont);
                saveNumber.setVerticalAlignment(SwingConstants.CENTER);
                saveNumber.setHorizontalAlignment(SwingConstants.CENTER);
                saveNumber.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel btnPanel = new JPanel();
                btnPanel.setOpaque(false);
                JButton loadButton = new JButton("Load");
                loadButton.setFont(CFont.get(Font.BOLD, 20));
                loadButton.addActionListener(e -> {
                    GameModel.loadGame(entry.getFileName());
                    startGame(false);
                });
                loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton deleteButton = new JButton("Delete");
                deleteButton.setBackground(new Color(150, 70, 70));
                deleteButton.setFont(CFont.get(Font.BOLD, 20));
                deleteButton.addActionListener(e -> {
                    switch (GameModel.showDialog("Delete?", "Are you sure you want to delete " + entry.getCityName())) {
                        case JOptionPane.YES_OPTION -> {
                            SaveEntry.removeEntry(entry.getCityName());
                            displayLoad();
                        }
                        default -> {
                        }
                    }
                });
                deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnPanel.add(loadButton);
                btnPanel.add(deleteButton);

                saveBox.add(Box.createRigidArea(new Dimension(0, 16)));
                saveBox.add(saveLabel);
                saveBox.add(Box.createRigidArea(new Dimension(0, 8)));
                saveBox.add(modifiedLabel);
                saveBox.add(Box.createRigidArea(new Dimension(0, 8)));
                saveBox.add(createdLabel);
                saveBox.add(Box.createRigidArea(new Dimension(0, 8)));
                saveBox.add(saveNumber);
                saveBox.add(Box.createRigidArea(new Dimension(0, 8)));
                saveBox.add(btnPanel);
                saveBox.add(Box.createRigidArea(new Dimension(0, 16)));
                saveBox.add(Box.createVerticalGlue());

                saveBoxes.add(saveBox);
                saveBoxes.add(Box.createRigidArea(new Dimension(0, gap)));
            }
            JScrollPane scrollPane = new JScrollPane(saveBoxes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUI(new MenuScrollBarUI());
            scrollPane.getVerticalScrollBar().setUnitIncrement(8);
            loadMenuPanel.add(scrollPane);
        } else {
            JLabel noSaveLabel = new JLabel("There are no saved games yet");
            noSaveLabel.setFont(CFont.get(Font.BOLD, 22));
            noSaveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            loadMenuPanel.add(noSaveLabel);
        }

        //Exit
        MenuButton back_btn = new MenuButton("BACK");
        back_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_btn.addActionListener(e -> displayButtons());
        loadMenuPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        loadMenuPanel.add(back_btn);
        loadMenuPanel.add(Box.createRigidArea(new Dimension(0, 32)));

        this.add(Box.createVerticalGlue());
        this.add(loadMenuPanel, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Displays the actual game panel
     */
    private void startGame(String _cityName) {
        String cityName = _cityName.trim();
        if (cityName.length() > 0) {
            if (SaveEntry.findEntry(cityName) == null) {
                GameModel.reset().setCityName(cityName);
                startGame(true);
            } else {
                GameModel.showError("Input error", "This name is already taken");
            }
        } else {
            GameModel.showError("Input error", "Your city name cannot be empty");
        }
    }

    private void startGame(boolean newGame) {
        for (StartStopGameListener l : startGameListeners) l.onGameStart(newGame);
    }

    /**
     * Displays the settings page after selecting Settings.
     *
     * @param displayResolutions If the window is in Windowed Fullscreen state at the moment
     *                           then it will not display the pickable resolutions, instead it
     *                           will disable that ComboBox.
     * @param firstTime          Will be true if we called this method from the simplicity.Main Menu page,
     *                           so we can show the currently used display type and resolution.
     *                           Will be false if we called this method inside this method,
     *                           so we can show the changes we made previously (not the currently
     *                           used display type and resolution).
     */
    private void displaySettings(boolean displayResolutions, boolean firstTime) {
        this.removeAll();
        this.revalidate();
        this.repaint();

        int windowWidth = this.getWidth();
        int windowHeight = this.getHeight();
        int gap = windowHeight / 40;

        JPanel settingsMenu = new JPanel();
        settingsMenu.setLayout(new BoxLayout(settingsMenu, BoxLayout.Y_AXIS));
        settingsMenu.setOpaque(false);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, 32)));

        //Label
        MenuLabel res_lbl = new MenuLabel("Resolution");
        res_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsMenu.add(res_lbl);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Resolution type
        String[] options = {"Windowed Fullscreen", "Windowed"};
        MenuComboBox res_type = new MenuComboBox(options);
        res_type.setAlignmentX(Component.CENTER_ALIGNMENT);
        String display;
        if (!firstTime) {
            display = displayResolutions ? "Windowed" : "Windowed Fullscreen";
            res_type.setSelectedItem(display);
        } else {
            int fullScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            display = (windowWidth == fullScreenWidth) ? "Windowed Fullscreen" : "Windowed";
            res_type.setSelectedItem(display);
        }
        res_type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object selected = res_type.getSelectedItem();
                    displaySettings("Windowed".equals(selected), false);
                }
            }
        });
        settingsMenu.add(res_type);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Resolutions
        options = new String[]{"960x540", "1280x720", "1600x900"};
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
        } else apply_btn.addActionListener(e -> this.setWindowSize(windowWidth, windowHeight, true));
        settingsMenu.add(apply_btn);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, gap)));

        //Back
        MenuButton back_btn = new MenuButton("BACK");
        back_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_btn.addActionListener(e -> displayButtons());
        settingsMenu.add(back_btn);
        settingsMenu.add(Box.createRigidArea(new Dimension(0, 32)));

        this.add(Box.createVerticalGlue());
        this.add(settingsMenu, BorderLayout.CENTER);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Sets simplicity.Main Menu window size.
     *
     * @param width  simplicity.Main Menu window will be resized to this width.
     * @param height simplicity.Main Menu window will be resized to this height.
     */
    private void setWindowSize(int width, int height, boolean fullscreen) {
        if (fullscreen) {
            for (MenuEventListener l : menuEventListeners) l.changedFullscreen();
        } else {
            for (MenuEventListener l : menuEventListeners) l.changedWindowed(width, height);
        }
        updateMenuSize();
        displayButtons();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = GameModel.BACKGROUND_IMG;
        Dimension originalImgSize = new Dimension(img.getWidth(null), img.getHeight(null));
        double imgRatio = originalImgSize.width / (double) originalImgSize.height;
        double ratio = this.getWidth() / (double) this.getHeight();
        int imgWidth = (int) Math.ceil(
                (imgRatio > ratio) ? this.getHeight() * imgRatio : this.getWidth()
        );
        int imgHeight = (int) Math.ceil(
                (imgRatio > ratio) ? this.getHeight() : this.getWidth() / imgRatio
        );
        // background image will always be centered and cover the panel
        g.drawImage(img, (this.getWidth() / 2) - (imgWidth / 2), (this.getHeight() / 2) - (imgHeight / 2), imgWidth, imgHeight, null);
    }

    private void updateMenuSize() {
        int windowWidth = GameWindow.getWindowWidth();
        int windowHeight = GameWindow.getWindowHeight();
        this.setSize(new Dimension(windowWidth, windowHeight));
    }

}
