package io.github.jordandoyle.mcinject.mod.mods.movement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;

import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventTick;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.mod.Category;
import io.github.jordandoyle.mcinject.mod.Mod;

public class Sprint extends Mod implements Listener {

	public Sprint() {
		super("Sprint", Keyboard.KEY_P, Category.MOVEMENT);
	}

	@Override
	protected void onEnable() {
		EventHandler.getInstance().registerListener(this);
	}

	@Override
	protected void onDisable() {
		EventHandler.getInstance().unregisterListener(this);
	}
	
	@EventSubscribe
	public void onTick(EventTick e) {
		try {
			Class p = Wrapper.getPlayer().getInstance().getClass();
			Object o = p.getField(new ObfuscatedMapping("net/minecraft/src/EntityPlayerSP", "movementInput").getMapping()).get(Wrapper.getPlayer().getInstance());
		
			boolean b = (Boolean) Wrapper.getPlayer().isSneaking();
		
			Wrapper.getPlayer().setSprinting((o.getClass().getField(new ObfuscatedMapping("net/minecraft/src/MovementInput", "moveForward").getMapping()).getFloat(o) > 0 && !b));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
