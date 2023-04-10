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
import simplicity.Model.Zones.Residential;

@AllArgsConstructor
@Getter
@Setter
public class Person implements InGameTimeTickListener {

    private final InGameTime inGameTime;
    @Setter
    private int mood = (int) (Math.random() * 10 + 65);
    private Date age = new Date(18, 0, 0);
    private int[] born = new int[]{
            InGameTimeManager.getInstance().getInGameTime().getInGameYear(),
            InGameTimeManager.getInstance().getInGameTime().getInGameDay(),
            InGameTimeManager.getInstance().getInGameTime().getInGameHour()
    };
    private int lifeExpectancy = (int) (Math.random() * 25 + 60);
    @Setter
    private EducationLevel educationLevel = EducationLevel.PRIMARY;
    private Workplace workplace = null;
    private Residential home = null;

   public Person() {
       inGameTime = InGameTimeManager.getInstance().getInGameTime();
       inGameTime.addInGameTimeTickListener(this);
   }

    public Person(Residential home) {
        inGameTime = InGameTimeManager.getInstance().getInGameTime();
        inGameTime.addInGameTimeTickListener(this);

        moveIn(home);
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
        if (placeOfWork.getPeople().size() < placeOfWork.getMaxPeople() && !placeOfWork.getPeople().contains(this)) {
            placeOfWork.getPeople().add(this);
            this.workplace = placeOfWork;
        }
    }

    public void moveIn(Residential home) {
        if (home.getPeople().size() < home.getMaxPeople() && !home.getPeople().contains(this)) {
            home.getPeople().add(this);
            this.home = home;
        }
    }

    @Override
    public void timeTick() {
        if (this.inGameTime.getInGameDay() == 0 && this.inGameTime.getInGameHour() == 0 && this.inGameTime.getInGameYear() > 0) {
            //aging process
//            this.age.setDate( this.age.getYear() + 1, 0, 0);
            this.age.addToYear(1);
            //if dead
            if (this.age.getYear() == this.lifeExpectancy) {
                this.workplace.getPeople().remove(this);
                this.workplace = null;
                this.home = null;
                this.mood = (int) (Math.random() * 10 + 45);
                this.lifeExpectancy = (int) (Math.random() * 25 + 60);
                this.educationLevel = EducationLevel.PRIMARY;
                this.age.setDate(18, 0, 0);
            }
        }
    }
}
