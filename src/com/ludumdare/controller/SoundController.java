package com.ludumdare.controller;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundController {

    private HashMap<SoundId, Sound> soundMapping = new HashMap<SoundId, Sound>();

    public enum SoundId {

        INVADER_DESTROYED,
        INVADER_SHOOT,
        SHOOTER_SHOOT,
        SHOOTER_DESTROYED

    }

    public void registerSound(SoundId soundId, Sound sound) {
        soundMapping.put(soundId, sound);
    }

    public void play(SoundId soundId) {
        Sound sound = soundMapping.get(soundId);

        if (sound != null) {
            sound.play();
        }
    }

}
