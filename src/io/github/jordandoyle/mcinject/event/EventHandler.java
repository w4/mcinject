package io.github.jordandoyle.mcinject.event;

import io.github.jordandoyle.mcinject.Main;
import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.events.EventRenderOverlay;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.mod.Mod;
import io.github.jordandoyle.mcinject.mod.ModManager;
import io.github.jordandoyle.mcinject.proxy.interfaces.Minecraft;

import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;

/**
 * Handles everything to do with events
 * 
 * @author Jordan Doyle
 * 
 */
public class EventHandler implements Listener {
	private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
	
	/**
	 * Singleton instance of this class
	 */
	private static EventHandler instance;

	static {
		try {
			instance = EventHandler.class.newInstance();
			instance.registerListener(instance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the singleton instance of this class
	 * 
	 * @return singleton instance of this class
	 */
	public static EventHandler getInstance() {
		return instance;
	}

	public Field gameSettings;

	private Field guiScale,
				  displayWidth;

	public Field debug;
	
	private float titlePosition = -150;
	private Map<String, Float> positions = new HashMap<String, Float>();
	
	@EventSubscribe
	public void renderOverlay(EventRenderOverlay e) {
		// TODO: yeah, I should probably move this into its own class
		try {
			Minecraft mc = Wrapper.getMinecraft();
			
			if(gameSettings == null)
				gameSettings = mc.getMinecraft().getClass().getField(new ObfuscatedMapping("net/minecraft/src/Minecraft", "gameSettings").getMapping());
			
			Object settings = gameSettings.get(mc.getMinecraft());
			
			if(debug == null)
				debug = settings.getClass().getField(new ObfuscatedMapping("net/minecraft/src/GameSettings", "showDebugInfo").getMapping());
			
			if(displayWidth == null)
				displayWidth = mc.getMinecraft().getClass().getField(new ObfuscatedMapping("net/minecraft/src/Minecraft", "displayWidth").getMapping());
			
			int width = displayWidth.getInt(mc.getMinecraft());
			int nextPosition = 0;
			
			if(!debug.getBoolean(settings)) {
				if(Math.round(titlePosition) != 2) {
					titlePosition += 0.5;
				}
				
				Wrapper.getFontRenderer().drawStringWithShadow("mcinject \u00a76" + Main.VERSION, titlePosition, 2, 0xffffff);
				
				for(Mod m : ModManager.getMods()) {
					if(m.isEnabled()) {
						if(!positions.containsKey(m.getName()))
							positions.put(m.getName(), Wrapper.getFontRenderer().getWidth(m.getName()) + 2);
						
						if(Math.round(positions.get(m.getName())) != -2)
							positions.put(m.getName(), positions.get(m.getName()) - 0.5F);
						
						Wrapper.getFontRenderer().drawStringWithShadow(m.getName(), width / 2 - Wrapper.getFontRenderer().getWidth(m.getName()) + positions.get(m.getName()), nextPosition + 2, 0xffffff);
						nextPosition += Wrapper.getFontRenderer().getHeight(m.getName());
					} else {
						if(!positions.containsKey(m.getName()))
							continue;
						
						if(Math.round(positions.get(m.getName())) != Math.round(Wrapper.getFontRenderer().getWidth(m.getName()) + 2)) {
							positions.put(m.getName(), positions.get(m.getName()) + 0.5F);
							Wrapper.getFontRenderer().drawStringWithShadow(m.getName(), width / 2 - Wrapper.getFontRenderer().getWidth(m.getName()) + positions.get(m.getName()), nextPosition + 2, 0xffffff);
							nextPosition += Wrapper.getFontRenderer().getHeight(m.getName());
						} else
							positions.remove(m.getName());
					}
				}
			}
			
			if(guiScale == null)
				guiScale = settings.getClass().getField(new ObfuscatedMapping("net/minecraft/src/GameSettings", "guiScale").getMapping());
			
			guiScale.setInt(settings, 2);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Registers a listener
	 * 
	 * @param l An instance of a class that extends Listener
	 */
	public void registerListener(Listener l) {
		if(listeners.contains(l)) return;
		
		listeners.add(l);
	}
	
	/**
	 * Unregisters a listener
	 * 
	 * @param l An instance of a class that extends Listener
	 */
	public void unregisterListener(Listener l) {
		if(!listeners.contains(l)) return;
		
		listeners.remove(l);
	}
	
	/**
	 * Passes an event to all registered listeners
	 * 
	 * @param e Event that will be sent to listeners
	 */
	private void alertListeners(Event e) {
		for(Listener listener : listeners) {
			for(Method m : listener.getClass().getMethods()) {
				if (m.getParameterTypes().length > 0
						&& m.isAnnotationPresent(EventSubscribe.class)
						&& m.getParameterTypes()[0].isAssignableFrom(e.getClass()))
					try {
						m.setAccessible(true);
						m.invoke(listener, e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		}
	}
	
	/**
	 * Fires an event to all listeners
	 * 
	 * @param e an instance of an event
	 */
	public void triggerEvent(Event e) {
		alertListeners(e);
	}
}
