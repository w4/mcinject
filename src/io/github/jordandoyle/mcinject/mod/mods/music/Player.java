package io.github.jordandoyle.mcinject.mod.mods.music;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.base.CaseFormat;
import com.mpatric.mp3agic.Mp3File;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import io.github.jordandoyle.mcinject.Main;
import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventKeyPressed;
import io.github.jordandoyle.mcinject.event.events.EventRenderOverlay;
import io.github.jordandoyle.mcinject.event.events.EventTick;
import io.github.jordandoyle.mcinject.mod.Category;
import io.github.jordandoyle.mcinject.mod.Mod;

public class Player extends Mod implements Listener {
	private BasicPlayer player;
	private io.github.jordandoyle.mcinject.helpers.Player threadHandler;
	private List<File> songs;
	private long lastRan = System.currentTimeMillis();
	private File song;
	private String name;
	
	public Player() {
		super("Music Player", Keyboard.KEY_RBRACKET, Category.MUSIC);
	}

	public void newSong() {
		if(songs == null || songs.isEmpty()) {
			if(System.currentTimeMillis() - lastRan < 20000) return;
			lastRan = System.currentTimeMillis();
			getMusic();
			if(songs == null || songs.isEmpty()) {
				System.out.println("No music!");
			}
		}
		
		if(player != null)
			try {
				player.stop();
			} catch (BasicPlayerException e1) {
				e1.printStackTrace();
			}
		
		try {
			song = songs.get(0);
			BufferedInputStream file = new BufferedInputStream(new FileInputStream(song));
			
			if(song.exists()) {
				System.out.println("test");
			}
			
			System.out.println(song.getName());
			
			player = new BasicPlayer();
			player.open(file);
			threadHandler = new io.github.jordandoyle.mcinject.helpers.Player(player);
			threadHandler.play();
            songs.remove(0);
            
            name = toNiceName(song);
            sendMessage("Now playing \u00a7b" + name + "\u00a7r!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String toNiceName(File f) {
		Mp3File song = null;
		try {
			song = new Mp3File(f.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(song.hasId3v2Tag()) {
			return song.getId3v2Tag().getArtist() + " - " + song.getId3v2Tag().getTitle();
		}
		
		if(song.hasId3v1Tag()) {
			return song.getId3v1Tag().getArtist() + " - " + song.getId3v1Tag().getTitle();
		}
		
		return song.getFilename();
	}

	protected void getMusic() {
		try {
			File f = new File(Main.minecraft, "mcinject");
			f = new File(f, "music");

			if (!f.exists())
				f.mkdirs();

			songs = new LinkedList<File>(Arrays.asList(f.listFiles()));

			Collections.shuffle(songs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onEnable() {
		try {
			getMusic();
			newSong();

			EventHandler.getInstance().registerListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDisable() {
		if (threadHandler != null) threadHandler.stop();

		EventHandler.getInstance().unregisterListener(this);
	}

	@EventSubscribe
	public void onTick(EventTick e) {
		if(songs == null || songs.isEmpty()) {
			getMusic();
		}
		
		if (threadHandler != null && threadHandler.songFinished)
			newSong();
	}

	@EventSubscribe
	public void onKeyPressed(EventKeyPressed e) {
		if (!e.getState()) {
			if (e.getKey() == Keyboard.KEY_RIGHT)
				newSong();

			if (e.getKey() == Keyboard.KEY_UP) {
				if (Math.round(threadHandler.getGain()) > 1)
					return;
				threadHandler
						.changeGain(Math.round(threadHandler.getGain() * 100 + 1D));
				sendMessage("Gain now at "
						+ Math.round(threadHandler.getGain() * 100));
			}

			if (e.getKey() == Keyboard.KEY_DOWN) {
				if (Math.round(threadHandler.getGain()) < 0)
					return;
				threadHandler
						.changeGain(Math.round(threadHandler.getGain() * 100 - 1D));
				sendMessage("Gain now at "
						+ Math.round(threadHandler.getGain() * 100));
			}
		}
	}
	
	@EventSubscribe
	public void onRender(EventRenderOverlay e) {
		try {
			if(name != null && threadHandler != null)
				Wrapper.getFontRenderer().drawStringWithShadow(StringUtils.abbreviate(name.substring(name.indexOf('-') + 2), 20) + " (" + threadHandler.getProgress() + ")", 2, 17, 0xffffffff);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
