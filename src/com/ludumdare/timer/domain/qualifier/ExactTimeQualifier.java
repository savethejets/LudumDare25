package com.ludumdare.timer.domain.qualifier;

public class ExactTimeQualifier implements TimeQualifier{

    protected long minutes;
    protected long seconds;

    public ExactTimeQualifier(long minutes, long seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public boolean isEqual(long minutes, long seconds) {
        return minutes == this.minutes && seconds == this.seconds;
    }

}
