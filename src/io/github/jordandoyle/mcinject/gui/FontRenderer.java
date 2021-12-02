package io.github.jordandoyle.mcinject.gui;

import static org.lwjgl.opengl.GL11.*;
import io.github.jordandoyle.mcinject.Main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.GlyphPage;
import org.newdawn.slick.font.effects.ColorEffect;

import com.google.common.io.Resources;

public class FontRenderer {
	private Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	
	public FontRenderer() {
		URL fontUrl = null;
		try {
			fontUrl = new File(Main.minecraft, "mcinject/font.ttf").toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT,
					fontUrl.openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.unicodeFont = new UnicodeFont(font.deriveFont(27F));

		try {
			this.unicodeFont.addAsciiGlyphs();
			this.unicodeFont.getEffects().add(
					new ColorEffect(java.awt.Color.WHITE));
			this.unicodeFont.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 32; i++) {
			int shadow = (i >> 3 & 1) * 85;
			int red = (i >> 2 & 1) * 170 + shadow;
			int green = (i >> 1 & 1) * 170 + shadow;
			int blue = (i >> 0 & 1) * 170 + shadow;

			if (i == 6) {
				red += 85;
			}

			if (i >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}

			this.colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | blue
					& 255;
		}
	}

	private final UnicodeFont unicodeFont;
	private final int[] colorCodes = new int[32];

	public void drawString(String text, float x, float y, int color) {
		x *= 2.0F;
		y *= 2.0F;
		float originalX = x;
		
		glPushMatrix();
		glScaled(0.5, 0.5, 0.5);
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		int currentColor = color;
		char[] characters = text.toCharArray();

		int index = 0;
		for (char c : characters) {
			if (c == '\r') {
				x = originalX;
			}
			if (c == '\n') {
				y += getHeight(Character.toString(c)) * 2.0F;
			}
			if (c != '\247'
					&& (index == 0 || index == characters.length - 1 || characters[index - 1] != '\247')) {
				unicodeFont.drawString(x, y, Character.toString(c),
						new org.newdawn.slick.Color(currentColor));
				x += getWidth(Character.toString(c)) * 2.0F;
			} else if (c == ' ') {
				x += unicodeFont.getSpaceWidth();
			} else if (c == '\247' && index != characters.length - 1) {
				int codeIndex = "0123456789abcdef".indexOf(text
						.charAt(index + 1));
				if (codeIndex < 0)
					continue;
				int col = this.colorCodes[codeIndex];
				currentColor = col;
			}

			index++;
		}

		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public void drawStringWithShadow(String text, float x, float y, int color) {
		drawString(stripControlCodes(text), x + 0.5F, y + 0.75F,
				0x55000000);
		drawString(text, x, y, color);
	}

	public void drawCenteredString(String text, float x, float y, int color) {
		drawString(text, x / 2.0F - getWidth(text) / 2.0F, y, color);
	}

	public void drawCenteredStringWithShadow(String text, float x, float y,
			int color) {
		drawCenteredString(stripControlCodes(text), x + 0.5F,
				y + 0.5F, color);
		drawCenteredString(text, x, y, color);
	}

	public float getWidth(String s) {
		float width = 0.0F;

		String s1 = s;
		for (char c : s1.toCharArray()) {
			width += unicodeFont.getWidth(Character.toString(c));
		}

		return width / 2.0F;
	}

	public float getHeight(String s) {
		return unicodeFont.getHeight(s) / 2.0F;
	}

	public UnicodeFont getFont() {
		return this.unicodeFont;
	}
	
	private String stripControlCodes(String par0Str)
    {
        return patternControlCode.matcher(par0Str).replaceAll("");
    }
}
