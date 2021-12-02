package io.github.jordandoyle.mcinject.mod.mods.player;

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

public class AutoRespawn extends Mod implements Listener {
	private Method isAlive = null;
	
	public AutoRespawn() {
		super("AutoRespawn", Keyboard.KEY_O, Category.PLAYER);
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
	public void tick(EventTick e) {
		try {
			Object player = Wrapper.getPlayer().getInstance();
			if(isAlive == null) {
				Class entityLivingBase = Class.forName(new ObfuscatedMapping("net/minecraft/src/EntityLivingBase").getMapping());
				Object elb = entityLivingBase.cast(player);
				isAlive = elb.getClass().getMethod(new ObfuscatedMapping("net/minecraft/src/EntityLivingBase", "isEntityAlive", "()V").getMapping());
			}
			boolean alive = (Boolean) isAlive.invoke(player);
			
			if(!alive) {
				Object packet = Class.forName(new ObfuscatedMapping("net/minecraft/src/Packet205ClientCommand").getMapping()).getConstructor(int.class).newInstance(1);
				Wrapper.sendPacket(packet);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

}
