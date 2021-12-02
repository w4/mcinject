package io.github.jordandoyle.mcinject.mod;

import io.github.jordandoyle.mcinject.mod.mods.movement.*;
import io.github.jordandoyle.mcinject.mod.mods.music.Player;
import io.github.jordandoyle.mcinject.mod.mods.player.AutoRespawn;
import io.github.jordandoyle.mcinject.mod.mods.render.ESP;
import io.github.jordandoyle.mcinject.mod.mods.render.FullBright;

import java.util.ArrayList;
import java.util.List;

public class ModManager {
	private static List<Mod> mods = new ArrayList<Mod>();
	
	static {
		// Movement
		mods.add(new Sprint());
		mods.add(new Fly());
		
		// Player
		mods.add(new AutoRespawn());
		
		// Render
		mods.add(new ESP());
		mods.add(new FullBright());
		
		mods.add(new Player());
	}
	
	public static Mod getMod(String name) {
		for(Mod m : mods) {
			if(m.getName().equalsIgnoreCase(name))
				return m;
		}
		
		return null;
	}

	public static List<Mod> getMods() {
		return mods;
	}
}
