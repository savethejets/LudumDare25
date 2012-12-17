package com.ludumdare.timer.domain.qualifier;

public class EveryQualifier implements TimeQualifier{

    private long minutesPassed = 0;
    private long secondsPassed = 0;

    private long minutesTrigger;
    private long secondsTrigger;

    public EveryQualifier(long minutes, long seconds) {
        this.minutesTrigger = minutes;
        this.secondsTrigger = seconds;
    }

    public boolean isEqual(long minutes, long seconds) {

        secondsPassed++;

        if (secondsPassed >= 59) {
            minutesPassed++;
            secondsPassed = 0;
        }

        if (secondsPassed >= secondsTrigger && minutesPassed >= minutesTrigger) {
            return true;
        }

        return false;
    }

    public void reset() {
        minutesPassed = 0;
        secondsPassed = 0;
    }
}
