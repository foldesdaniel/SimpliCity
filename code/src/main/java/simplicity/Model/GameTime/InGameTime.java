package simplicity.Model.GameTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.Model.Listeners.InGameTimeTickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@NoArgsConstructor
public class InGameTime implements Serializable {

    private final ArrayList<InGameTimeTickListener> inGameTimeTickListeners = new ArrayList<>();
    @Setter
    public transient Timer inGameElapsedTime;
    @Getter
    private int inGameYear = 0;
    @Getter
    private int inGameDay = 0;
    @Getter
    private int inGameHour = 0;
    private InGameTimeListener inGameTimeListener;

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

    /**
     * use to start the time in-game with a specific speed
     *
     * @param speed the speed we want to start the game with
     */
    public void startInGameTime(InGameSpeeds speed) {
        inGameElapsedTime = new Timer();
        TimerTask inGameElapsedTimeAction = new TimerTask() {
            @Override
            public void run() {
                if (inGameTimeListener != null) inGameTimeListener.timeChanged(inGameYear, inGameDay, inGameHour);
                for (int i = 0; i < inGameTimeTickListeners.size(); i++) {
                    inGameTimeTickListeners.get(i).timeTick();
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
            }
        };
        inGameElapsedTime.scheduleAtFixedRate(inGameElapsedTimeAction, 0, speed.getSpeed());
    }

    /**
     * used to stop the time in-game
     */
    public void stopInGameTime() {
        inGameElapsedTime.cancel();
    }

    public void setInGameTimeListener(InGameTimeListener inGameTimeListener) {
        this.inGameTimeListener = inGameTimeListener;
    }

    public void addInGameTimeTickListener(InGameTimeTickListener inGameTimeTickListener) {
        this.inGameTimeTickListeners.add(inGameTimeTickListener);
    }

}
