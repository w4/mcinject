package io.github.jordandoyle.mcinject.handler;

public abstract class Handler {
	// TODO: Add some better functionality in here in the future
	
	private boolean registered = false;
	private String name;
	
	public Handler(String name) {
		this.name = name;
	}
	
	protected abstract void onRegister();

	public boolean hasRegistered() {
		return registered;
	}
	
	public void register() throws Exception {
		if(hasRegistered())
			throw new Exception("Cannot register an already registered handler");
		
		registered = true;
		onRegister();
	}
	
	public String getName() {
		return name;
	}
}
