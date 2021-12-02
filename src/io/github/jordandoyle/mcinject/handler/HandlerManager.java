package io.github.jordandoyle.mcinject.handler;

import io.github.jordandoyle.mcinject.handler.handlers.KeyPressHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
	private static List<Handler> handlers = new ArrayList<Handler>();
	
	static {
		handlers.add(new KeyPressHandler());
	}
	
	public static Handler getHandler(String name) {
		for(Handler h : handlers) {
			if(h.getName().equalsIgnoreCase(name))
				return h;
		}
		
		return null;
	}

	public static List<Handler> getHandlers() {
		return handlers;
	}
	
	public static void registerHandlers() {
		try {
			for(Handler h : getHandlers())
				if(!h.hasRegistered())
					h.register();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}