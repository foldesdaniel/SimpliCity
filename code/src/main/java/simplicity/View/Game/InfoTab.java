package simplicity.View.Game;

import simplicity.Model.Education.Education;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Forest;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.PlaceableTemp;
import simplicity.Model.Placeables.Workplace;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Zone;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A panel displaying all the details
 * about a given Placeable
 */
public class InfoTab extends JPanel implements PeopleChangeListener, MoralChangeListener {

    private final JLabel emptyLabel;
    private final Component boxGap = Box.createRigidArea(new Dimension(0, 16));
    private Placeable lastInfo = null;

    public InfoTab() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.emptyLabel = new JLabel("Empty selection");
        this.emptyLabel.setFont(CFont.get(Font.PLAIN, 20));
        this.init();
        GameModel.getInstance().addPeopleChangeListener(this);
    }

    /**
     * Initializes the info tab (displaying no info)
     */
    public void init() {
        this.removeAll();
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.boxGap);
        this.add(this.emptyLabel);
    }

    /**
     * Updates the Placeable and displays its details
     *
     * @param _f Placeable's info to update
     */
    public void updateInfo(Placeable _f) {
        this.removeAll();
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (this.lastInfo != null && this.lastInfo instanceof Zone z) {
            z.removePeopleChangeListener(this);
        }
        Placeable f = _f instanceof PlaceableTemp ff ? ff.getPlaceable() : _f;
        this.lastInfo = f;
        if (this.lastInfo != null && this.lastInfo instanceof Zone z) {
            z.addPeopleChangeListener(this);
        }
        if (f == null) {
            this.init();
        } else {
            this.add(this.boxGap);
            InfoIcon icon = new InfoIcon(f);
            icon.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(icon);
            this.add(Box.createRigidArea(new Dimension(0, 4)));
            Point position = f.getPosition();
            String name = f.getDisplayName();
            Dimension size = f.getDisplaySize();
            int tax = f.calculateTax();
            int buildPrice = f.getBuildPrice();
            StringBuilder infoText = new StringBuilder("<html>");
            infoText.append("<b>").append(name).append("</b><br>");
            infoText.append("Position: (").append(position.x).append(",").append(position.y).append(")<br>");
            infoText.append("Size: ").append(size.width).append("x").append(size.height).append("<br>");
            infoText.append("Tax: ").append(tax).append("<br>");
            infoText.append("Build price: ").append(buildPrice).append("<br>");
            if (f instanceof Zone ff) {
                infoText.append("Capacity: ").append(ff.getPeople().size()).append("/").append(ff.getMaxPeople()).append("<br>");
                ArrayList<Person> people = ff.getPeople();
                if (ff instanceof Residential fff)
                    infoText.append("Mood: ").append(fff.calculateZoneMood()).append("<br>");
                infoText.append("People:<br><ul style=\"padding:0;\">");
                if (people.size() == 0) {
                    infoText.append("There are no people here");
                } else {
                    for (int i = 0; i < people.size(); i++) {
                        Person person = people.get(i);
                        infoText.append("<li>").append(person.getAge().getYear()).append("yo (mood: ").append(person.getMood()).append(")");
                        if (ff instanceof Residential fff) {
                            infoText.append("<ul><li>");
                            if (person.getEducation() != null) {
                                infoText.append("School: ").append(person.getEducation());
                            } else if (person.getWorkplace() != null) {
                                infoText.append("Workplace: ").append(person.getWorkplace());
                            } else {
                                infoText.append("No school or workplace");
                            }
                            infoText.append("<li>Education level: ").append(person.getEducationLevel()).append("</li>");
                            infoText.append("</li></ul>");
                        } else if (ff instanceof Workplace || ff instanceof Education) {
                            infoText.append("<ul><li>");
                            infoText.append("Home: ").append(person.getHome());
                            infoText.append("</li></ul>");
                        }
                        infoText.append("</li>");
                    }
                }
                infoText.append("</ul>");
            } else if (f instanceof Forest ff) {
                int age = ff.getAge();
                infoText.append("Age: ").append(age).append(age == 1 ? " year" : " years");
            }
            infoText.append("</html>");
            JLabel infoLabel = new JLabel(infoText.toString());
            infoLabel.setFont(CFont.get(Font.PLAIN, 20));
            infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(infoLabel);
        }
    }

    @Override
    public void onPeopleCountChange() {
        updateInfo(this.lastInfo);
    }

    @Override
    public void onMoralChanged() {
        updateInfo(this.lastInfo);
    }

    /**
     * A panel that gets the Image of the parent's
     * Placeable, sizes it appropriately and displays it
     */
    static class InfoIcon extends JPanel {

        private final Placeable p;

        InfoIcon(Placeable p) {
            this.p = p;
            double x = p.getSize().width / (double) p.getSize().height;
            Dimension imageSize = new Dimension((int) Math.round(x * 32), 32);

            this.setPreferredSize(imageSize);
            this.setSize(imageSize);
            this.setMinimumSize(imageSize);
            this.setMaximumSize(imageSize);
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(p.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }

}
