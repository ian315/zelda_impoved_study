package com.ian.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.Objects;

public class Audios {

    private Clip clip = AudioSystem.getClip();
    public static final Audios hit;

    static {
        try {
            hit = new Audios("/Hit.wav") {
            };
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private Audios(String name) throws LineUnavailableException {
        try {
            AudioInputStream audioSystem = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResourceAsStream(name)));
            clip.open(audioSystem);
            clip.start();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void play() {
        if (clip.isRunning())
            clip.stop(); // Para o som atual se estiver tocando

        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        try {
            new Thread(() -> clip.loop(Clip.LOOP_CONTINUOUSLY)).start();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
