package io.github.jordandoyle.mcinject.event;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import io.github.jordandoyle.mcinject.event.events.*;

public final class EventFactory {
	public static void renderOverlay() {
		EventHandler.getInstance().triggerEvent(new EventRenderOverlay());
	}
	
	public static void runTick() {
		EventHandler.getInstance().triggerEvent(new EventTick());
	}


	public static void onKeyPressed(int eventKey, boolean keyState) {
		EventHandler.getInstance().triggerEvent(new EventKeyPressed(eventKey, keyState));
	}
	
	public static void renderHand() {
		// TODO: Implement partial ticks
		EventHandler.getInstance().triggerEvent(new EventRenderHand());
	}
	
	public static void damageEntity(Object source, float amount) {
		EventHandler.getInstance().triggerEvent(new EventDamageEntity(source, amount));
	}
}
