package io.github.jordandoyle.helpers.reflection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Resources;

import au.com.bytecode.opencsv.CSVReader;

/**
 * This class helps you use reflection and ASM in Minecraft.
 * 
 * It will provide you with tools to get obfuscated/compiled names for methods,
 * classes and fields.
 * 
 * 
 * @author Jordan Doyle
 * 
 */
public class Mapping {
	private static List<String[]> fields, methods, obfuscated;

	static {
		// Fields
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(Resources
					.getResource("fields.csv").openStream()));
			fields = reader.readAll();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Methods
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(Resources
					.getResource("methods.csv").openStream()));
			methods = reader.readAll();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Packaged
		try {
			InputStream stream = Resources.getResource("client.srg")
					.openStream();

			StringBuilder sb = new StringBuilder();
			String line;

			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));

			obfuscated = new ArrayList<String[]>();

			while ((line = br.readLine()) != null) {
				String[] l = line.split("\\s+");
				obfuscated.add(l);
			}

			br.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the compiled name of a field
	 * 
	 * @param f
	 *            The name of the uncompiled field
	 * @return The compiled name of a field
	 */
	public static String getCompiledField(String f) {
		for (String[] field : fields) {
			if (field[1].equals(f)) {
				return field[0];
			}
		}

		return f;
	}

	/**
	 * Returns the compiled name of a method
	 * 
	 * @param m
	 *            The name of the uncompiled method
	 * @return The compiled name of a method
	 */
	public static String getCompiledMethod(String m) {
		for (String[] method : methods) {
			if (method[1].equals(m)) {
				return method[0];
			}
		}

		return m;
	}

	/**
	 * Returns the obfuscated name of a class
	 * 
	 * @param c
	 *            The class of which you require the obfuscated name for
	 * @return The obfuscated name of a class
	 */
	public static String getMappedClass(String c) {
		String name = c.replace(".", "/");

		for (String[] obf : obfuscated) {
			if (obf[0].startsWith("CL") && obf[2].equals(name)) {
				return obf[1];
			}
		}

		return c;
	}

	/**
	 * Returns the obfuscated name of a method
	 * 
	 * @param c
	 *            The class of which the method is contained
	 * @param m
	 *            The name of the unobfuscated method
	 * @return The obfuscated name of a method
	 */
	public static String getMappedMethod(String c, String m) {
		return getMappedMethod(c, m, null, null);
	}

	/**
	 * 
	 * @param c
	 *            The class of which the method is contained
	 * @param m
	 *            The name of the unobfuscated method
	 * @param d
	 *            The decompiled descriptor types for the method
	 * @param od
	 *            The compiled descriptor types for the method
	 * @return The obfuscated name of a method
	 */
	public static String getMappedMethod(String c, String m, String d, String od) {
		String name = c.replace(".", "/");

		String methodName = getCompiledMethod(m);

		if (methodName == null)
			methodName = m;

		for (String[] obf : obfuscated) {
			if (obf[0].startsWith("MD") && obf[1].startsWith(getMappedClass(c))
					&& obf[3].equals(name + "/" + methodName)) {
				if (od != null)
					if (!obf[2].equals(od))
						return methodName;

				if (d != null)
					if (!obf[4].equals(d))
						return methodName;

				String method = obf[1].substring(StringUtils
						.length(getMappedClass(c) + "/"));
				return method;
			}
		}

		return methodName;
	}

	/**
	 * Returns the obfuscated name of a field
	 * 
	 * @param c
	 *            The class of which the variable is contained
	 * @param f
	 *            The name of the unobfuscated field
	 * @return The obfuscated name of a field
	 */
	public static String getMappedField(String className, String f) {
		String name = className.replace(".", "/");

		String fieldName = getCompiledField(f);

		if (fieldName == null)
			fieldName = f;

		for (String[] obf : obfuscated) {
			if (obf[0].startsWith("FD")
					&& obf[1].startsWith(getMappedClass(className))
					&& obf[2].equals(name + "/" + fieldName)) {
				String field = obf[1].substring(StringUtils
						.length(getMappedClass(className) + "/"));
				return field;
			}
		}

		return fieldName;
	}
}