package org.example;

import Model.InGameSpeeds;
import Model.InGameTime;

public class Main {
    public static void main(String[] args) {
        InGameTime t = new InGameTime();
        t.startInGameTime(InGameSpeeds.FASTEST);
        try {
            Thread.sleep(3000);
            t.stopInGameTime();
            Thread.sleep(3000);
            t.startInGameTime(InGameSpeeds.NORMAL);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
