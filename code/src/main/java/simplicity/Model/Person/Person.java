package simplicity.Model.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Education.Education;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Placeables.Workplace;

@AllArgsConstructor
@Getter
public class Person implements InGameTimeTickListener {

    private int mood = 5;
    private Date age = new Date(18,0,0);

    private int[] born = new int[]{
            InGameTimeManager.getInstance().getInGameTime().getInGameYear(),
            InGameTimeManager.getInstance().getInGameTime().getInGameDay(),
            InGameTimeManager.getInstance().getInGameTime().getInGameHour()
    };
    private int lifeExpectancy = (int) (Math.random() * 25 + 60);
    @Setter
    private EducationLevel educationLevel = EducationLevel.PRIMARY;

    private Workplace workplace = null;

    private final InGameTime inGameTime;


    public Person() {
        inGameTime = InGameTimeManager.getInstance().getInGameTime();
        inGameTime.addInGameTimeTickListener(this);
    }

    public void goToSchool(Education placeOfEducation) {
        if (placeOfEducation.getPeople().size() < placeOfEducation.getMaxPeople() && !placeOfEducation.getPeople().contains(this)) {
            placeOfEducation.getPeople().add(this);
            InGameTime igt = InGameTimeManager.getInstance().getInGameTime();
            placeOfEducation.getArrivalDates().add(
                    new Date(igt.getInGameYear(), igt.getInGameDay(), igt.getInGameHour())
            );
        }
    }

    public void goToWork(Workplace placeOfWork) {
        if(placeOfWork.getPeople().size() < placeOfWork.getMaxPeople() && !placeOfWork.getPeople().contains(this)) {
            placeOfWork.getPeople().add(this);
            this.workplace = placeOfWork;
        }
    }

    @Override
    public void timeTick() {
        InGameTime igt = InGameTimeManager.getInstance().getInGameTime();
        if(igt.getInGameDay() == 0 && igt.getInGameHour() == 0 && igt.getInGameYear() > 0){
            this.age.setDate( this.age.getYear() + 1, 0, 0);
            //if dead
            if(this.age.getYear() == this.lifeExpectancy) {
                this.workplace.getPeople().remove(this);
                this.workplace = null;
                this.lifeExpectancy = (int) (Math.random() * 25 + 60);
                this.educationLevel = EducationLevel.PRIMARY;
                this.age.setDate(18, 0, 0);
            }
        }
    }
}
