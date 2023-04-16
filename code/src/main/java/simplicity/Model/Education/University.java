package simplicity.Model.Education;

import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class University extends Education implements InGameTimeTickListener {

    private final EducationLevel levelOfEducation = EducationLevel.UNIVERSITY;

    private final InGameTime inGameTime;

    public University(Point position) {
        super(FieldType.UNIVERSITY, position, 1000, 20, 800);
        inGameTime = InGameTimeManager.getInstance().getInGameTime();
        inGameTime.addInGameTimeTickListener(this);
    }

    @Override
    public void graduate(ArrayList<Integer> studentIds) {
        for (int i = 0; i < studentIds.size(); i++) {
            this.getPeople().get(i).setEducationLevel(levelOfEducation);
//            System.out.println(this.getPeople().get(i).getEducationLevel() + " GRADUATED");
        }
        if (studentIds.size() > 0) {
            studentIds.sort(Collections.reverseOrder());
            studentIds.forEach(index -> this.getArrivalDates().remove((int) index));
            studentIds.forEach(index -> this.getPeople().remove((int) index));
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
            //this.getArrivalDates().get(i).getYear() + 2 == inGameTime.getInGameYear() &&
            //this.getArrivalDates().get(i).getDay() == inGameTime.getInGameDay() &&
            //this.getArrivalDates().get(i).getHour() == inGameTime.getInGameHour()
            //to complete Uni it is 2 years
            if (this.getArrivalDates().get(i).getDay() + 1 == this.inGameTime.getInGameDay()
                    && this.getArrivalDates().get(i).getHour() == this.inGameTime.getInGameHour()) {
                graduates.add(i);
            }
        }
        graduate(graduates);
    }
}
