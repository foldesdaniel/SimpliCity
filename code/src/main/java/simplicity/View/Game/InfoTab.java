package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Zone;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InfoTab extends JPanel implements PeopleChangeListener, MoralChangeListener {

    private Placeable lastInfo = null;

    public InfoTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GameModel.getInstance().addPeopleChangeListener(this);
        this.init();
    }

    public void init(){
        this.removeAll();
        JLabel tempLabel = new JLabel("Empty selection");
        tempLabel.setFont(CFont.get(Font.PLAIN, 20));
        this.add(tempLabel);
    }

    public void updateInfo(Placeable f) {
        this.removeAll();
        if(this.lastInfo != null && this.lastInfo instanceof Zone z){
            z.removePeopleChangeListener(this);
        }
        this.lastInfo = f;
        if(this.lastInfo != null && this.lastInfo instanceof Zone z){
            z.addPeopleChangeListener(this);
        }
        if (f == null) {
            this.init();
        } else {
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
                for(int i=0;i<people.size();i++){
                    Person person = people.get(i);
                    infoText += "<li>" + person.getAge().getYear() + "yo (mood: " + person.getMood() + ")";
                    infoText += "<ul><li>";
                    if(ff instanceof Residential fff){
                        if(person.getEducation() != null){
                            infoText += "School: " + person.getEducation();
                        }else if(person.getWorkplace() != null){
                            infoText += "Workplace: " + person.getWorkplace();
                        }else{
                            infoText += "No school or workplace";
                        }
                    }
                    infoText += "</li></ul>";
                    infoText += "</li>";
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

}
