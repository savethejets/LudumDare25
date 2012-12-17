package com.ludumdare.timer.domain;

import com.ludumdare.timer.domain.qualifier.TimeQualifier;

import java.util.List;

public interface TimerEvent {
    public TimeQualifier getQualifier();

    public void doOnEvent();

    public void finish(List<TimerEvent> events);
}
