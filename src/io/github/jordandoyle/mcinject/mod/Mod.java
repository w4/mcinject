package io.github.jordandoyle.mcinject.mod;

import java.lang.reflect.Method;

import org.objectweb.asm.Type;

import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.proxy.interfaces.Minecraft;

public abstract class Mod {
	private String name;
	private int key;
	private boolean enabled;
	private Category category;
	
	protected Minecraft mc = Wrapper.getMinecraft();
	
	public Mod(String name, int key, Category category) {
		this.name = name;
		this.key = key;
		this.category = category;
	}
	
	public Mod(String name, Category category) {
		this.name = name;
		this.key = -1;
		this.category = category;
	}

	public void toggle() {
		if (enabled)
			disable();
		else
			enable();
	}

	public boolean isEnabled() {
		return enabled;
	}

	protected abstract void onEnable();

	protected abstract void onDisable();

	public void enable() {
		if(enabled)
			return;
		
		enabled = true;
		sendMessage(name + " \u00a7aenabled");
		onEnable();
	}

	public void disable() {
		if(!enabled)
			return;
		
		enabled = false;
		sendMessage(name + " \u00a7cdisabled");
		onDisable();
	}

	public String getName() {
		return name;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public int getKey() {
		return key;
	}
	
	protected void sendMessage(String message) {
		Wrapper.getPlayer().addChatMessage("\u00a76[mcinject] \u00a7f" + message);
	}
}
