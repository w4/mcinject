package io.github.jordandoyle.mcinject.mod;

public enum Category {
	MOVEMENT("Movement"),
	PLAYER("Player"),
	RENDER("Render"),
	MUSIC("Music");
	
	private String name;
	
	private Category(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
