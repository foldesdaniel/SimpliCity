package simplicity.Model.Education;

import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(force = true)
public class School extends Education implements InGameTimeTickListener, Serializable {

    private final EducationLevel levelOfEducation = EducationLevel.SECONDARY;

    private final InGameTime inGameTime;

    public School( Point position) {
        super(FieldType.SCHOOL, position, 7000, 20, 1500);
        inGameTime = InGameTimeManager.getInstance().getInGameTime();
        inGameTime.addInGameTimeTickListener(this);
    }

    @Override
    public void graduate(ArrayList<Integer> studentIds) {
        for (int i = 0; i < studentIds.size(); i++) {
            this.getPeople().get(i).setEducationLevel(levelOfEducation);
            this.getPeople().get(i).setEducation(null);
//            System.out.println(this.getPeople().get(i).getEducationLevel() + " GRADUATED");
        }
        if (studentIds.size() > 0) {
            studentIds.sort(Collections.reverseOrder());
            studentIds.forEach(index -> this.getArrivalDates().remove((int) index));
            studentIds.forEach(index -> this.removePerson((int) index));
            System.out.println(this.getPeople().size());
            System.out.println(this.getArrivalDates().size());
        }
    }

    @Override
    public final void timeTick() {
//        System.out.println("time changed: " + this.inGameTime.getInGameYear() + this.inGameTime.getInGameDay() + this.inGameTime.getInGameHour());
        ArrayList<Integer> graduates = new ArrayList<>();
        for (int i = 0; i < this.getArrivalDates().size(); i++) {
            //[0] year, [1] day, [2] hour
            //change to
//            this.getArrivalDates().get(i).getYear() + 1 == inGameTime.getInGameYear() &&
//            this.getArrivalDates().get(i).getDay() == inGameTime.getInGameDay() &&
//            this.getArrivalDates().get(i).getHour() == inGameTime.getInGameHour()
//            this.getArrivalDates().get(i).getDay() + 1 == this.inGameTime.getInGameDay()
//                    && this.getArrivalDates().get(i).getHour() == this.inGameTime.getInGameHour()
            //to complete School it is 1 year
            if (this.getArrivalDates().get(i).getYear() + 1 == inGameTime.getInGameYear() &&
                    this.getArrivalDates().get(i).getDay() == inGameTime.getInGameDay() &&
                    this.getArrivalDates().get(i).getHour() == inGameTime.getInGameHour()) {
                graduates.add(i);
            }
        }
        graduate(graduates);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.EDUCATION_SCHOOL_IMG;
    }

    @Override
    public Dimension getSize(){
        return new Dimension(2, 1);
    }

}
