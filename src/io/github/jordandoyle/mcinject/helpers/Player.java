package io.github.jordandoyle.mcinject.helpers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public class Player implements BasicPlayerListener {
	private BasicPlayer player;
	public boolean songFinished = false;
	private boolean shouldStop = false, shouldStart = false;
	private double gain = 0.5;
	private long progress;

	public Player(BasicPlayer player) {
		this.player = player;
		this.player.addBasicPlayerListener(this);
	}

	public void play() {
		try {
			player.play();
			player.setGain(0.5);
			shouldStart = true;
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			player.stop();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void changeGain(double g) {
		try {
			player.setGain((double) g / 100D);

			this.gain = g / 100;
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public double getGain() {
		return gain;
	}

	public String getProgress() {
		return String.format(
				"%d:%02d",
				TimeUnit.MICROSECONDS.toMinutes(progress),
				TimeUnit.MICROSECONDS.toSeconds(progress)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MICROSECONDS
								.toMinutes(progress)));
	}

	@Override
	public void opened(Object stream, Map properties) {
	}

	@Override
	public void progress(int bytesread, long microseconds, byte[] pcmdata,
			Map properties) {
		progress = microseconds;
	}

	@Override
	public void stateUpdated(BasicPlayerEvent event) {
		if (event.getCode() == BasicPlayerEvent.STOPPED)
			songFinished = true;
	}

	@Override
	public void setController(BasicController controller) {
	}
}
