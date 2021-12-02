package io.github.jordandoyle.mcinject.event.events;

import io.github.jordandoyle.mcinject.event.Event;

public class EventKeyPressed extends Event {
	private int key;
	private boolean state;
	
	public EventKeyPressed(int key, boolean state) {
		this.key = key;
		this.state = state;
	}
	
	/**
	 * Gets the key that was pressed
	 * 
	 * @return key that was pressed
	 */
	public int getKey() {
		return key;
	}
	
	/**
	 * Returns the state of the key press
	 * 
	 * false = key down
	 * true = key up
	 * 
	 * @return state of key
	 */
	public boolean getState() {
		return state;
	}
}
