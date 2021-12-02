package io.github.jordandoyle.mcinject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.jordandoyle.mcinject.gui.FontRenderer;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.proxy.interfaces.EntityPlayerSP;
import io.github.jordandoyle.mcinject.proxy.interfaces.Minecraft;
import io.github.jordandoyle.mcinject.proxy.proxies.EntityPlayerSPProxy;
import io.github.jordandoyle.mcinject.proxy.proxies.MinecraftProxy;

public class Wrapper {
	private static FontRenderer fontRenderer = null;
	private static Minecraft    minecraft    = null;
	private static EntityPlayerSP player;
	
	public static FontRenderer getFontRenderer() {
		if(fontRenderer == null)
			fontRenderer = new FontRenderer();
		
		return fontRenderer;
	}
	
	public static Minecraft getMinecraft() {
		if(minecraft == null)
			minecraft = (Minecraft) new MinecraftProxy().proxy();
		
		return minecraft;
	}
	
	public static EntityPlayerSP getPlayer() {
		if(player == null)
			player = (EntityPlayerSP) new EntityPlayerSPProxy().proxy();
	
		return player;
	}
	
	public static void sendPacket(Object o) {
		//net.minecraft.src.Minecraft.getMinecraft().getNetHandler().addToSendQueue(o);
		Object mc = getMinecraft().getMinecraft();
		try {
			Method netHandler = mc.getClass().getMethod(new ObfuscatedMapping("net/minecraft/src/Minecraft", "getNetHandler", "(").getMapping());
			Object nh = netHandler.invoke(mc);
			Method sendQueue = nh.getClass().getMethod(new ObfuscatedMapping("net/minecraft/src/NetClientHandler", "addToSendQueue", "(").getMapping(), Class.forName(new ObfuscatedMapping("net/minecraft/src/Packet").getMapping()));
			sendQueue.invoke(nh, o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
