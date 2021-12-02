package io.github.jordandoyle.mcinject.mod.mods.render;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Color;

import io.github.jordandoyle.mcinject.Wrapper;
import io.github.jordandoyle.mcinject.event.EventHandler;
import io.github.jordandoyle.mcinject.event.EventSubscribe;
import io.github.jordandoyle.mcinject.event.Listener;
import io.github.jordandoyle.mcinject.event.events.EventRenderHand;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;
import io.github.jordandoyle.mcinject.helpers.RenderHelper;
import io.github.jordandoyle.mcinject.mod.Category;
import io.github.jordandoyle.mcinject.mod.Mod;

public class ESP extends Mod implements Listener {
	private Class renderManager, player, entity, aabb;
	private Field renderPosX, renderPosY, renderPosZ, loadedEntityList;
	private Object theWorld;
	private Color innerOpacity = new Color(69, 179, 235, 0),
				  outerOpacity = new Color(69, 179, 235, 0);
	
	public ESP() {
		super("ESP", Keyboard.KEY_U, Category.RENDER);
	}

	@Override
	protected void onEnable() {
		innerOpacity = new Color(69, 179, 235, 15);
		outerOpacity = new Color(69, 179, 235, 15);
		EventHandler.getInstance().registerListener(this);
	}

	@Override
	protected void onDisable() {
		//EventHandler.getInstance().unregisterListener(this);
	}

	@EventSubscribe
	public void renderHand(EventRenderHand e) {
		double rpx = 0;
		double rpy = 0;
		double rpz = 0;
		List<Object> entityList = new ArrayList<Object>();

		try {
			if (renderManager == null)
				renderManager = Class.forName(new ObfuscatedMapping(
						"net/minecraft/src/RenderManager").getMapping());

			if (player == null)
				player = Class.forName(new ObfuscatedMapping(
						"net/minecraft/src/EntityPlayer").getMapping());
			
			if(aabb == null)
				aabb = Class.forName(new ObfuscatedMapping(
						"net/minecraft/src/AxisAlignedBB").getMapping());
			
			if (entity == null)
				entity = Class.forName(new ObfuscatedMapping(
						"net/minecraft/src/Entity").getMapping());

			if (renderPosX == null)
				renderPosX = renderManager.getField(new ObfuscatedMapping(
						"net/minecraft/src/RenderManager", "renderPosX")
						.getMapping());

			if (renderPosY == null)
				renderPosY = renderManager.getField(new ObfuscatedMapping(
						"net/minecraft/src/RenderManager", "renderPosY")
						.getMapping());

			if (renderPosZ == null)
				renderPosZ = renderManager.getField(new ObfuscatedMapping(
						"net/minecraft/src/RenderManager", "renderPosZ")
						.getMapping());

			theWorld = Wrapper
					.getMinecraft()
					.getMinecraft()
					.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/Minecraft",
									"theWorld").getMapping())
					.get(Wrapper.getMinecraft().getMinecraft());
			loadedEntityList = theWorld.getClass().getField(
					new ObfuscatedMapping("net/minecraft/src/World",
							"loadedEntityList").getMapping());

			rpx = renderPosX.getDouble(null);
			rpy = renderPosY.getDouble(null);
			rpz = renderPosZ.getDouble(null);
			entityList = (List<Object>) loadedEntityList.get(theWorld);

			if(this.isEnabled()) {
				if(innerOpacity.getAlpha() != 0x26)
					innerOpacity.setAlpha(innerOpacity.getAlpha() + 1);
			
				if(outerOpacity.getAlpha() != 0xFF)
					outerOpacity.setAlpha(outerOpacity.getAlpha() + 1);
			} else {
				if(outerOpacity.getAlpha() != 0x10) {
					if(innerOpacity.getAlpha() != 0x10)
						innerOpacity.setAlpha(innerOpacity.getAlpha() - 1);
				
					outerOpacity.setAlpha(outerOpacity.getAlpha() - 1);
				} else
					EventHandler.getInstance().unregisterListener(this);
			}
			
			for (Object o : entityList) {
				if (player.isInstance(o) && !o.equals(Wrapper.getPlayer().getInstance())) {
					Object p = entity.cast(o);
	
					double posX = p
							.getClass()
							.getField(
									new ObfuscatedMapping(
											"net/minecraft/src/Entity", "posX")
											.getMapping()).getDouble(p)
							- rpx;
					double posY = p
							.getClass()
							.getField(
									new ObfuscatedMapping(
											"net/minecraft/src/Entity", "posY")
											.getMapping()).getDouble(p)
							- rpy;
					double posZ = p
							.getClass()
							.getField(
									new ObfuscatedMapping(
											"net/minecraft/src/Entity", "posZ")
											.getMapping()).getDouble(p)
							- rpz;
					double height = p
							.getClass()
							.getField(
									new ObfuscatedMapping(
											"net/minecraft/src/Entity", "height")
											.getMapping()).getDouble(p);
					double width = p
							.getClass()
							.getField(
									new ObfuscatedMapping(
											"net/minecraft/src/Entity", "width")
											.getMapping()).getDouble(p) - 0.12;
	
					Object boundingBox = aabb.getMethod(new ObfuscatedMapping(
							"net/minecraft/src/AxisAlignedBB", "getBoundingBox", "(")
							.getMapping(), double.class, double.class, double.class, double.class, double.class, double.class).invoke(null, posX
							- width, posY + 0.1, posZ - width, posX + width, posY
							+ height + 0.2, posZ + width);
					
					RenderHelper.drawBoundingBox(boundingBox, (int) Long.parseLong(RenderHelper.toHex(innerOpacity), 16));
					RenderHelper.drawOutlineBoundingBox(boundingBox, (int) Long.parseLong(RenderHelper.toHex(outerOpacity), 16));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
