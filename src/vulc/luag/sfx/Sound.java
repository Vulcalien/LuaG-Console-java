/*******************************************************************************
 * Copyright (C) 2019 Vulcalien
 * This code or part of it is licensed under MIT License by Vulcalien
 ******************************************************************************/
package vulc.luag.sfx;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;

public class Sound {

	private final static Mixer MIXER;
	static {
		MIXER = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
	}

	private Clip clip;

	public Sound(URL url) {
		try {
			Clip clip = (Clip) MIXER.getLine(new DataLine.Info(Clip.class, null));
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
			clip.open(audioStream);
			this.clip = clip;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		if(clip == null) return;
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		if(clip == null) return;
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		if(clip == null) return;
		clip.stop();
	}

}
