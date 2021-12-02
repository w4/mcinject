package io.github.jordandoyle.mcinject.event.events;

import io.github.jordandoyle.mcinject.event.Event;

public class EventDamageEntity extends Event {
	private Object source;
	private float  damage;
	
	public EventDamageEntity(Object source, float damage) {
		this.source = source;
		this.damage = damage;
	}
	
	/**
	 * Gets the source of the damage
	 * 
	 * @return instance of DamageSource
	 */
	public Object getSource() {
		return source;
	}
	
	/**
	 * Gets amount of damage that was dealt
	 * 
	 * @return amount of damage to entity caused
	 */
	public float getDamage() {
		return damage;
	}
}
