package io.github.jordandoyle.mcinject.proxy.proxies;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.Type;

import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.proxy.Proxy;
import io.github.jordandoyle.mcinject.proxy.interfaces.Minecraft;

public class MinecraftProxy implements Proxy {
	private static Class c;
	private static Object instance;
	
	static {
		try {
			String className = (new ObfuscatedMapping("net/minecraft/src/Minecraft")).getMapping();
			c = Class.forName(className);
			Method m = c.getDeclaredMethod((new ObfuscatedMapping("net/minecraft/src/Minecraft", "getMinecraft", "()L" + className.replace(".", "/") + ";")).getMapping());
			instance = m.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object proxy() {
		return java.lang.reflect.Proxy.newProxyInstance(Minecraft.class.getClassLoader(), new Class[] { Minecraft.class }, this);
	}
	
	@Override
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		Object res = null;
		String name = new ObfuscatedMapping("net/minecraft/src/Minecraft", m.getName(), Type.getMethodDescriptor(m)).getMapping();
		
		m = c.getDeclaredMethod(name, m.getParameterTypes());
		m.setAccessible(true);
		res = m.invoke(instance, args);
		
		return res;
	}
}
