package simplicity.View.Game;

import simplicity.Model.Education.Education;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.PlaceableTemp;
import simplicity.Model.Placeables.Workplace;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Zone;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InfoTab extends JPanel implements PeopleChangeListener, MoralChangeListener {

    private Placeable lastInfo = null;
    private final Dimension imageSize = new Dimension(32,32);

    public InfoTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //this.setBackground(new Color(255,0,0));
        GameModel.getInstance().addPeopleChangeListener(this);
        this.init();
    }

    public void init(){
        this.removeAll();
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel tempLabel = new JLabel("Empty selection");
        tempLabel.setFont(CFont.get(Font.PLAIN, 20));
        this.add(tempLabel);
    }

    public void updateInfo(Placeable _f) {
        this.removeAll();
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(this.lastInfo != null && this.lastInfo instanceof Zone z){
            z.removePeopleChangeListener(this);
        }
        Placeable f = _f instanceof PlaceableTemp ff ? ff.getPlaceable() : _f;
        this.lastInfo = f;
        if(this.lastInfo != null && this.lastInfo instanceof Zone z){
            z.addPeopleChangeListener(this);
        }
        if (f == null) {
            this.init();
        } else {
            //this.add(new InfoIcon(f));
            Point position = f.getPosition();
            Image img = f.getImage();
            String name = f.getDisplayName();
            Dimension size = f.getDisplaySize();
            int tax = f.calculateTax();
            int buildPrice = f.getBuildPrice();
            String infoText = "<html>";
            infoText += "<b>" + name + "</b><br>";
            infoText += "Position: (" + position.x + "," + position.y + ")<br>";
            infoText += "Size: " + size.width + "x" + size.height + "<br>";
            infoText += "Tax: " + tax + "<br>";
            infoText += "Build price: " + buildPrice + "<br>";
            if(f instanceof Zone ff){
                infoText += "Capacity: " + ff.getPeople().size() + "/" + ff.getMaxPeople() + "<br>";
                ArrayList<Person> people = ff.getPeople();
                if(ff instanceof Residential fff) infoText += "Mood: " + fff.calculateZoneMood() + "<br>";
                infoText += "People:<br><ul style=\"padding:0;\">";
                if(people.size() == 0){
                    infoText += "There are no people here";
                }else{
                    for(int i=0;i<people.size();i++){
                        Person person = people.get(i);
                        infoText += "<li>" + person.getAge().getYear() + "yo (mood: " + person.getMood() + ")";
                        if(ff instanceof Residential fff){
                            infoText += "<ul><li>";
                            if(person.getEducation() != null){
                                infoText += "School: " + person.getEducation();
                            }else if(person.getWorkplace() != null){
                                infoText += "Workplace: " + person.getWorkplace();
                            }else{
                                infoText += "No school or workplace";
                            }
                            infoText += "</li></ul>";
                        }else if(ff instanceof Workplace || ff instanceof Education){
                            infoText += "<ul><li>";
                            infoText += "Home: " + person.getHome();
                            infoText += "</li></ul>";
                        }
                        infoText += "</li>";
                    }
                }
                infoText += "</ul>";
            }
            infoText += "</html>";
            JLabel tempLabel = new JLabel(infoText);
            tempLabel.setFont(CFont.get());
            this.add(tempLabel);
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

    class InfoIcon extends JPanel {

        private final Placeable p;

        InfoIcon(Placeable p){
            this.p = p;
            this.setPreferredSize(imageSize);
            this.setSize(imageSize);
            this.setMaximumSize(imageSize);
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(p.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }

}
