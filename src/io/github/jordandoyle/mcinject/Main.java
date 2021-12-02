package io.github.jordandoyle.mcinject;

import io.github.jordandoyle.mcinject.asm.TransformerManager;
import io.github.jordandoyle.mcinject.handler.HandlerManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.minecraft.src.Minecraft;

public class Main {
	private static Main instance = null;
	public static final String VERSION = "Alpha 2";
	public static File minecraft;
	
	public void start() {
		TransformerManager.runTransformers();
		HandlerManager.registerHandlers();
	}
	
	public static Main downloadDependencies(File f) {
		System.out.println("[mcinject] Downloading dependencies");
		minecraft = f;
		try {
			URL font = new URL("https://cdn.jsdelivr.net/npm/roboto-font@0.1.0/fonts/Roboto/roboto-thin-webfont.ttf");
			File localFont = new File(f, "mcinject");
			localFont.mkdirs();
			
			FileUtils.copyURLToFile(font, new File(localFont, "font.ttf"));
			
			URL u = new URL("http://mcinject.uphero.com/deps.zip");
			String tmp = System.getProperty("java.io.tempdir");
			String path = tmp + "injection_deps.zip";
			File c = new File(path);
			c.deleteOnExit();
			FileUtils.copyURLToFile(u, c);
			
			ZipFile file = new ZipFile(c);
			
			File folder = new File(minecraft, "mcinject/deps");
			
			file.extractAll(folder.getPath());
		
			URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	        Class sysclass = URLClassLoader.class;
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
			
			for (Object entry : file.getFileHeaders()) {
				FileHeader h = (FileHeader) entry;
				File jar = new File(folder, h.getFileName());
				
				System.out.println("[mcinject] Injected " + h.getFileName() + " into system class loader");
				
				method.invoke(sysloader, new Object[] { jar.toURI().toURL() });
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return Main.getInstance();
	}
	
	public static Main getInstance() {
		if(instance == null)
			instance = new Main();
		
		return instance;
	}

}
