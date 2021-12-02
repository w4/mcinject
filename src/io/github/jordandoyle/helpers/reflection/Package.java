package io.github.jordandoyle.helpers.reflection;

import io.github.jordandoyle.helpers.classloader.Loader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * This class provides you with package functions to better manage your packages
 * and perform functions such as retrieving all classes from a package and more.
 * 
 * @author Jordan Doyle
 * 
 */
public class Package {
	/**
	 * Returns all the classes from a package
	 * 
	 * @param p
	 *            The Package object to get classes from
	 * @return all classes from a Package object
	 * @throws Exception
	 */
	public static List<Class> getClassesInPackage(java.lang.Package p, ClassLoader classLoader)
			throws Exception {
		return getClassesInPackage(p.getName(), classLoader);
	}

	/**
	 * Returns all the classes from a package
	 * 
	 * @param p
	 *            The Package object to get classes from
	 * @return all classes from a Package object
	 * @throws Exception
	 */
	public static List<Class> getClassesInPackage(java.lang.Package p)
			throws Exception {
		return getClassesInPackage(p.getName());
	}
	
	/**
	 * Returns all the classes from a package
	 * 
	 * @param name
	 *            The fully-qualified name of the package
	 * @return all the classes from a package
	 * @throws Exception
	 */
	public static List<Class> getClassesInPackage(String name) throws Exception {
		return getClassesInPackage(name, ClassLoader.getSystemClassLoader());
	}
	
	/**
	 * Returns all the classes from a package
	 * 
	 * @param name
	 *            The fully-qualified name of the package
	 * @return all the classes from a package
	 * @throws Exception
	 */
	public static List<Class> getClassesInPackage(String name, ClassLoader classLoader) throws Exception {
		Vector<Class> classes = Loader.getAllClasses(classLoader);

		List<Class> inPackage = new ArrayList<Class>();

		for (Class c : classes) {
			java.lang.Package p = c.getPackage();

			if (p != null && p.getName() != null
					&& p.getName().startsWith(name))
				inPackage.add(c);
		}

		return inPackage;
	}
}
