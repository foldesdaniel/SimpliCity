package simplicity.Model.GameTime;

import lombok.*;

import java.util.Timer;
import java.util.TimerTask;

@NoArgsConstructor
public class InGameTime {

    private int inGameYear = 0;
    private int inGameDay = 0;
    private int inGameHour = 0;

    private Timer inGameElapsedTime;

    private InGameTimeListener inGameTimeListener;

    public void startInGameTime(InGameSpeeds speed) {
        inGameElapsedTime = new Timer();
        TimerTask inGameElapsedTimeAction = new TimerTask() {
            @Override
            public void run() {
                if (inGameHour < 23) inGameHour++;
                else {
                    inGameHour = 0;
                    if (inGameDay < 364) inGameDay++;
                    else {
                        inGameDay = 0;
                        inGameYear++;
                    }
                }
                String str = "Year: " + inGameYear + ", day: " + inGameDay + ", hour: " + inGameHour;
                inGameTimeListener.timeChanged(str);
            }
        };
        inGameElapsedTime.scheduleAtFixedRate(inGameElapsedTimeAction, 0, speed.getSpeed());
    }

    public void setInGameTimeListener(InGameTimeListener inGameTimeListener){
        this.inGameTimeListener = inGameTimeListener;
    }

    public void stopInGameTime() {
        inGameElapsedTime.cancel();
    }


}
