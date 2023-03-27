package simplicity.Model.GameTime;

import lombok.Getter;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.Model.Listeners.InGameTimeTickListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class InGameTime {

    @Getter
    private int inGameYear = 0;
    @Getter
    private int inGameDay = 0;
    @Getter
    private int inGameHour = 0;

    private Timer inGameElapsedTime;

    private InGameTimeListener inGameTimeListener;
    private final ArrayList<InGameTimeTickListener> inGameTimeTickListeners = new ArrayList<>();

    public InGameTime() {
    }

    public InGameTime(int inGameYear, int inGameDay, int inGameHour) {
        this.inGameYear = inGameYear;
        this.inGameDay = inGameDay;
        this.inGameHour = inGameHour;
    }

    public void setInGameTime(int inGameYear, int inGameDay, int inGameHour) {
        this.inGameYear = inGameYear;
        this.inGameDay = inGameDay;
        this.inGameHour = inGameHour;
    }

    public void startInGameTime(InGameSpeeds speed) {
        inGameElapsedTime = new Timer();
        TimerTask inGameElapsedTimeAction = new TimerTask() {
            @Override
            public void run() {
                inGameTimeListener.timeChanged(inGameYear, inGameDay, inGameHour);
                for (InGameTimeTickListener inGameTimeTickListener : inGameTimeTickListeners) {
                    inGameTimeTickListener.timeTick();
                }
                if (inGameHour < 23) inGameHour++;
                else {
                    inGameHour = 0;
                    if (inGameDay < 364) inGameDay++;
                    else {
                        inGameDay = 0;
                        inGameYear++;

                    }
                }
//                inGameTimeTickListener.timeTick();
            }
        };
        inGameElapsedTime.scheduleAtFixedRate(inGameElapsedTimeAction, 0, speed.getSpeed());
    }

    public void setInGameTimeListener(InGameTimeListener inGameTimeListener) {
        this.inGameTimeListener = inGameTimeListener;
    }

    public void addInGameTimeTickListener(InGameTimeTickListener inGameTimeTickListener) {
        this.inGameTimeTickListeners.add(inGameTimeTickListener);
    }

    public void stopInGameTime() {
        inGameElapsedTime.cancel();
    }

}
