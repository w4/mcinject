package io.github.jordandoyle.mcinject.helpers;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

public class RenderHelper {
	public static void drawBoundingBox(Object o, int colour) {
		double minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;

		try {
			minX = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minX")
									.getMapping()).getDouble(o);
			minY = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minY")
									.getMapping()).getDouble(o);
			minZ = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minZ")
									.getMapping()).getDouble(o);

			maxX = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxX")
									.getMapping()).getDouble(o);
			maxY = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxY")
									.getMapping()).getDouble(o);
			maxZ = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxZ")
									.getMapping()).getDouble(o);
		} catch (Exception e) {
			e.printStackTrace();
		}

		glPushMatrix();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_LINE_SMOOTH);
		glDepthMask(false);
		glLineWidth(1.8F);

		Color c = new Color(colour);
		glColor4f(c.r, c.g, c.b, c.a);

		glBegin(GL_QUADS);
		{
			glVertex3d(minX, minY, minZ);
			glVertex3d(minX, maxY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, maxY, minZ);

			glVertex3d(maxX, minY, maxZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(minX, maxY, maxZ);
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			glVertex3d(maxX, maxY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(minX, maxY, minZ);
			glVertex3d(minX, minY, minZ);

			glVertex3d(minX, maxY, maxZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(maxX, minY, maxZ);
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			glVertex3d(minX, maxY, minZ);
			glVertex3d(maxX, maxY, minZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(minX, maxY, maxZ);

			glVertex3d(minX, maxY, minZ);
			glVertex3d(minX, maxY, maxZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(maxX, maxY, minZ);
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			glVertex3d(minX, minY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, minY, maxZ);
			glVertex3d(minX, minY, maxZ);

			glVertex3d(minX, minY, minZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(maxX, minY, maxZ);
			glVertex3d(maxX, minY, minZ);
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			glVertex3d(minX, minY, minZ);
			glVertex3d(minX, maxY, minZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(minX, maxY, maxZ);

			glVertex3d(maxX, minY, maxZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, maxY, minZ);
		}
		glEnd();

		glBegin(GL_QUADS);
		{
			glVertex3d(minX, maxY, maxZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(minX, maxY, minZ);
			glVertex3d(minX, minY, minZ);
			glVertex3d(maxX, maxY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(maxX, minY, maxZ);
		}
		glEnd();

		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_LIGHTING);
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public static void drawOutlineBoundingBox(Object o, int colour) {
		double minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;

		try {
			minX = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minX")
									.getMapping()).getDouble(o);
			minY = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minY")
									.getMapping()).getDouble(o);
			minZ = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "minZ")
									.getMapping()).getDouble(o);

			maxX = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxX")
									.getMapping()).getDouble(o);
			maxY = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxY")
									.getMapping()).getDouble(o);
			maxZ = o.getClass()
					.getField(
							new ObfuscatedMapping(
									"net/minecraft/src/AxisAlignedBB", "maxZ")
									.getMapping()).getDouble(o);
		} catch (Exception e) {
			e.printStackTrace();
		}

		glPushMatrix();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_LINE_SMOOTH);
		glDepthMask(false);
		glLineWidth(1.8F);

		Color c = new Color(colour);
		glColor4f(c.r, c.g, c.b, c.a);

		glBegin(GL_LINE_STRIP);
		{
			glVertex3d(minX, minY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, minY, maxZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(minX, minY, minZ);
		}
		glEnd();

		glBegin(GL_LINE_STRIP);
		{
			glVertex3d(minX, maxY, minZ);
			glVertex3d(maxX, maxY, minZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(minX, maxY, maxZ);
			glVertex3d(minX, maxY, minZ);
		}
		glEnd();

		glBegin(GL_LINES);
		{
			glVertex3d(minX, minY, minZ);
			glVertex3d(minX, maxY, minZ);
			glVertex3d(maxX, minY, minZ);
			glVertex3d(maxX, maxY, minZ);
			glVertex3d(maxX, minY, maxZ);
			glVertex3d(maxX, maxY, maxZ);
			glVertex3d(minX, minY, maxZ);
			glVertex3d(minX, maxY, maxZ);
		}
		glEnd();

		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_LIGHTING);
		glDisable(GL_LINE_SMOOTH);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public static String toHex(org.lwjgl.util.Color c) {
		return getHexValue(c.getAlpha()) + getHexValue(c.getRed()) + getHexValue(c.getGreen()) + getHexValue(c.getBlue());
	}

	private static String getHexValue(int n) {
		StringBuilder builder = new StringBuilder(Integer.toHexString(n & 0xff));
		while (builder.length() < 2) {
			builder.append("0");
		}
		return builder.toString().toUpperCase();
	}
}
