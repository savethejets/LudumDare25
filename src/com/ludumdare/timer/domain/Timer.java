package com.ludumdare.timer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private final CountMethodImpl countUp = new CountUp();
    private final CountMethodImpl countDown = new CountDown();

    long minutes;
    long seconds;

    private boolean isPaused;

    private CountMethodImpl method;

    private List<TimerEvent> events;

    private ScheduledExecutorService exec;

    public Timer() {
        method = new CountUp();

        events = new ArrayList<TimerEvent>();

        initTimer();
    }

    public Timer(CountMethodImpl method) {
        this.method = method;

        events = new ArrayList<TimerEvent>();

        initTimer();
    }

    public void countUp() {
        if (!countUp.equals(method)) {
            method = countUp;
        }
    }

    public void countDown() {
        if (!countDown.equals(method)) {
            method = countDown;
        }
    }

    public void dispose() {
        exec.shutdown();
    }

    public void pause() {
        isPaused = true;
    }

    public void unPause() {
        isPaused = false;
    }

    public void addTimerEvent(TimerEvent timerEvent) {
        events.add(timerEvent);
    }

    private void triggerEvents() {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getQualifier().isEqual(this.minutes, this.seconds)) {
                events.get(i).doOnEvent();
                events.get(i).finish(events);
            }
        }
    }

    private void initTimer() {
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        if (!isPaused) {
                            method.count(0, 1);
                            triggerEvents();
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);
    }

    public List<TimerEvent> getEvents() {
        return events;
    }

    private interface CountMethodImpl {
        public void count(long minutes, long seconds);
    }

    private class CountUp implements CountMethodImpl {
        @Override
        public void count(long minutes, long seconds) {

            Timer.this.minutes += minutes;
            Timer.this.seconds += seconds;

            if (Timer.this.seconds >= 59) {
                Timer.this.minutes++;
                Timer.this.seconds = 0;
            }

        }
    }

    private class CountDown implements CountMethodImpl {
        @Override
        public void count(long minutes, long seconds) {

            if ((Timer.this.minutes > 0 && Timer.this.seconds > 0) || (Timer.this.minutes == 0 && Timer.this.seconds > 0)) {
                Timer.this.minutes -= minutes;
                Timer.this.seconds -= seconds;

                if (Timer.this.seconds <= -1) {
                    Timer.this.minutes--;
                    Timer.this.seconds = 59;
                }
            }
        }
    }

    public void reset() {
        minutes = 0;
        seconds = 0;
        events.clear();
    }

    public void kill() {
        exec.shutdown();
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public boolean isCountDown() {
        return method.equals(countDown);
    }
}
