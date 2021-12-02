package io.github.jordandoyle.mcinject.mod.mods.render;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventTick;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.mod.Category;
import io.github.jordandoyle.mcinject.mod.Mod;

public class FullBright extends Mod implements Listener {
	private long nextGammaChange;
	private Field gameSettings, gammaSetting;

	public FullBright() {
		super("Fullbright", Keyboard.KEY_M, Category.RENDER);
	}

	@Override
	public void onEnable() {
		try {
			if (gameSettings == null)
				gameSettings = mc
						.getMinecraft()
						.getClass()
						.getField(
								new ObfuscatedMapping(
										"net/minecraft/src/Minecraft",
										"gameSettings").getMapping());

			if (gammaSetting == null)
				gammaSetting = gameSettings
						.get(Wrapper.getMinecraft().getMinecraft())
						.getClass()
						.getField(
								new ObfuscatedMapping(
										"net/minecraft/src/GameSettings",
										"gammaSetting").getMapping());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventHandler.getInstance().registerListener(this);
	}

	@EventSubscribe
	public void onTick(EventTick e) {
		try {
			if (this.isEnabled()) {
				if (Math.floor(gammaSetting.getFloat(gameSettings.get(Wrapper
						.getMinecraft().getMinecraft()))) < 10F) {
					if (System.currentTimeMillis() > nextGammaChange) {
						gammaSetting
								.setFloat(
										gameSettings.get(Wrapper.getMinecraft()
												.getMinecraft()),
										gammaSetting.getFloat(gameSettings
												.get(Wrapper.getMinecraft()
														.getMinecraft())) + 0.25F);
						nextGammaChange = System.currentTimeMillis() + 5L;
					}
				}

				if (Math.floor(gammaSetting.getFloat(gameSettings.get(Wrapper
						.getMinecraft().getMinecraft()))) > 10F) {
					if (System.currentTimeMillis() > nextGammaChange) {
						gammaSetting
								.setFloat(
										gameSettings.get(Wrapper.getMinecraft()
												.getMinecraft()),
										gammaSetting.getFloat(gameSettings
												.get(Wrapper.getMinecraft()
														.getMinecraft())) - 0.25F);
						nextGammaChange = System.currentTimeMillis() + 5L;
					}
				}
			} else {
				if (gammaSetting.getFloat(gameSettings.get(Wrapper
						.getMinecraft().getMinecraft())) > 1f) {
					if (System.currentTimeMillis() > nextGammaChange) {
						gammaSetting
								.setFloat(
										gameSettings.get(Wrapper.getMinecraft()
												.getMinecraft()),
										gammaSetting.getFloat(gameSettings
												.get(Wrapper.getMinecraft()
														.getMinecraft())) - 0.25F);
						nextGammaChange = System.currentTimeMillis() + 5L;
					}
				} else
					EventHandler.getInstance().unregisterListener(this);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
	}
}
