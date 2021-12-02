package io.github.jordandoyle.mcinject.handler.handlers;

import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventKeyPressed;
import io.github.jordandoyle.mcinject.handler.Handler;
import io.github.jordandoyle.mcinject.mod.Mod;
import io.github.jordandoyle.mcinject.mod.ModManager;

public final class KeyPressHandler extends Handler implements Listener {
	public KeyPressHandler() {
		super("KeyPress");
	}

	public void onRegister() {
		EventHandler.getInstance().registerListener(this);
	}
	
	@EventSubscribe
	public void onKeyDown(EventKeyPressed e) {
		if(e.getState() == false)
			return;
		
		for(Mod m : ModManager.getMods()) {
			if(e.getKey() == m.getKey())
				m.toggle();
		}
	}
}
