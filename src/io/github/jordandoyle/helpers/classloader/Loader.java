package io.github.jordandoyle.helpers.classloader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

/**
 * This class helps you load JARs and folders onto the system classloader. This
 * makes the classloader also look through these JAR files (or folders) for
 * classes.
 * 
 * @author Jordan Doyle
 * 
 */
public class Loader {
	/**
	 * Adds a JAR file to the classpath
	 * 
	 * @param f
	 *            Link to JAR File
	 * @throws Exception
	 */
	public static void addJar(String f) throws Exception {
		addJar(new File(f));
	}

	/**
	 * Adds a JAR file to the classpath
	 * 
	 * @param f
	 *            Link to JAR File
	 * @throws Exception
	 */
	public static void addJar(File f) throws Exception {
		String ext = FilenameUtils.getExtension(f.getName());

		if (ext == "")
			throw new Exception("Could not find extension of file");

		if (ext.toLowerCase() != "jar")
			throw new Exception("Cannot inject non-jar file into classpath");

		addURL(f.toURI().toURL());
	}

	/**
	 * This method allows you add a folder to the classpath
	 * 
	 * @param u
	 *            URL to add to the classpath
	 * @throws Exception
	 */
	public static void addURL(URL u) throws Exception {
		File f = new File(u.toURI());

		if (!f.isDirectory()
				&& !FilenameUtils.getExtension(f.getName()).equalsIgnoreCase(
						"jar")) {
			throw new Exception(
					"addURL requires a directory to add to classpath");
		}

		Method m = URLClassLoader.class.getDeclaredMethod("addURL",
				new Class[] { URL.class });
		m.setAccessible(true);

		m.invoke(ClassLoader.getSystemClassLoader(), new Object[] { u });
	}

	/**
	 * Returns all classes loaded in the system class loader
	 * 
	 * @return all loaded classes
	 * @throws Exception
	 */
	public static Vector<Class> getAllClasses(ClassLoader classLoader)
			throws Exception {
		Field f = ClassLoader.class.getDeclaredField("classes");
		f.setAccessible(true);

		Vector<Class> classes = (Vector<Class>) f.get(classLoader);

		return classes;
	}

	/**
	 * Returns all classes loaded in the system class loader
	 * 
	 * @return all loaded classes
	 * @throws Exception
	 */
	public static Vector<Class> getAllClasses() throws Exception {
		return getAllClasses(ClassLoader.getSystemClassLoader());
	}
}
