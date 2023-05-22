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

    /**
     * @param position the current position in the grid
     */
    public School(Point position) {
        super(FieldType.SCHOOL, position, 7000, 20, 1500);
        inGameTime = InGameTimeManager.getInstance().getInGameTime();
        inGameTime.addInGameTimeTickListener(this);
    }


    /**
     * used to upgrade a persons education level and remove their place of education
     *
     * @param studentIds list of the indexes of people graduating
     */
    private void removeGraduates(ArrayList<Integer> studentIds) {
        for (int i = 0; i < studentIds.size(); i++) {
            this.getPeople().get(i).setEducationLevel(levelOfEducation);
            this.getPeople().get(i).setEducation(null);
        }
    }

    /**
     * used to clear out people from the place of education
     *
     * @param studentIds list of the indexes of people graduating
     */
    @Override
    public void graduate(ArrayList<Integer> studentIds) {
        removeGraduates(studentIds);
        if (studentIds.size() > 0) {
            studentIds.sort(Collections.reverseOrder());
            studentIds.forEach(index -> this.getArrivalDates().remove((int) index));
            studentIds.forEach(index -> this.removePerson(index));
        }
    }

    /**
     * used to check if people attending have completed their education
     */
    @Override
    public final void timeTick() {
        ArrayList<Integer> graduatingPeople = new ArrayList<>();
        for (int i = 0; i < this.getArrivalDates().size(); i++) {
            if (this.getArrivalDates().get(i).getYear() + 1 == inGameTime.getInGameYear() &&
                    this.getArrivalDates().get(i).getDay() == inGameTime.getInGameDay() &&
                    this.getArrivalDates().get(i).getHour() == inGameTime.getInGameHour()) {
                graduatingPeople.add(i);
            }
        }
        graduate(graduatingPeople);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.EDUCATION_SCHOOL_IMG;
    }

    @Override
    public Dimension getSize() {
        return new Dimension(2, 1);
    }

}
