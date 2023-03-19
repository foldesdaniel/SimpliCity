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
                System.out.println("Year: " + inGameYear + ", day: " + inGameDay + ", hour: " + inGameHour);
            }
        };
        inGameElapsedTime.scheduleAtFixedRate(inGameElapsedTimeAction, 0, speed.getSpeed());
    }

    public void stopInGameTime() {
        inGameElapsedTime.cancel();
    }


}
