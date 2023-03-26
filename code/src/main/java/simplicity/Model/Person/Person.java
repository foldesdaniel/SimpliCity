package simplicity.Model.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Education.Education;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;

@AllArgsConstructor
@Getter
public class Person {

    private int mood = 5;
    private int age = 18;

    private int[] born = new int[]{
            InGameTimeManager.getInstance().getInGameTime().getInGameYear(),
            InGameTimeManager.getInstance().getInGameTime().getInGameDay(),
            InGameTimeManager.getInstance().getInGameTime().getInGameHour()
    };
    private int lifeExpectancy;
    @Setter
    private EducationLevel educationLevel = EducationLevel.PRIMARY;


    public Person() {

        this.lifeExpectancy = (int) (Math.random() * 25 + 60);
    }

    public void goToSchool(Education placeOfEducation) {
        if (placeOfEducation.getPeople().size() < placeOfEducation.getMaxPeople() && !placeOfEducation.getPeople().contains(this)) {
            placeOfEducation.getPeople().add(this);
            InGameTime igt = InGameTimeManager.getInstance().getInGameTime();
            placeOfEducation.getArrivalDates().add(
                    new int[]{
                            igt.getInGameYear(),
                            igt.getInGameDay(),
                            igt.getInGameHour()
                    }
            );
        }
    }

}
