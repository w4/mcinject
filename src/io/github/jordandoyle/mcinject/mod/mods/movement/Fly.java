package io.github.jordandoyle.mcinject.mod.mods.movement;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventTick;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.mod.Category;
import io.github.jordandoyle.mcinject.mod.Mod;

public class Fly extends Mod implements Listener {
	private boolean couldFly = false;
	
	public Fly() {
		super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
	}

	@Override
	protected void onEnable() {
		EventHandler.getInstance().registerListener(this);
	}
	
	@EventSubscribe
	public void onTick(EventTick e) {
		Object player = Wrapper.getPlayer().getInstance();
		try {
			Field capabilities = player.getClass().getField(new ObfuscatedMapping("net/minecraft/src/EntityPlayer", "capabilities").getMapping());
			Object c = capabilities.get(player);
			Field isFlying = c.getClass().getField(new ObfuscatedMapping("net/minecraft/src/PlayerCapabilities", "isFlying").getMapping());
			isFlying.setBoolean(c, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onDisable() {
		EventHandler.getInstance().unregisterListener(this);
		
		Object player = Wrapper.getPlayer().getInstance();
		try {
			Field capabilities = player.getClass().getField(new ObfuscatedMapping("net/minecraft/src/EntityPlayer", "capabilities").getMapping());
			Object c = capabilities.get(player);
			Field isFlying = c.getClass().getField(new ObfuscatedMapping("net/minecraft/src/PlayerCapabilities", "isFlying").getMapping());
			isFlying.setBoolean(c, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
