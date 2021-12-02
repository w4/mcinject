package io.github.jordandoyle.mcinject.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Mapper {
	private static List<String[]> classes, methods, fields;
	
	static {
		// Classes
		{
			try {
				URL url = new URL("http://mcinject.uphero.com/1.7.2/classes.html");
				InputStream is = url.openStream();
				
				InputStreamReader reader      = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(reader);
				
				String line;
				
				classes = new ArrayList<String[]>();
				
				while((line = bufferedReader.readLine()) != null) {
					String[] l = line.split("\\s+");
					classes.add(l);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		// Methods
		{
			try {
				URL url = new URL("http://mcinject.uphero.com/1.7.2/methods.html");
				InputStream is = url.openStream();
				
				InputStreamReader reader      = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(reader);
						
				String line;
					
				methods = new ArrayList<String[]>();
						
				while((line = bufferedReader.readLine()) != null) {
					String[] l = line.split("\\s+");
					methods.add(l);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Fields
		{
			try {
				URL url = new URL("http://mcinject.uphero.com/1.7.2/fields.html");
				InputStream is = url.openStream();
					
				InputStreamReader reader      = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(reader);
								
				String line;
							
				fields = new ArrayList<String[]>();
								
				while((line = bufferedReader.readLine()) != null) {
					String[] l = line.split("\\s+");
					fields.add(l);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getClass(String unobfuscated) {
		for(String[] cl : classes) {
			if(StringUtils.equalsIgnoreCase(cl[1], unobfuscated)) {
				return cl[0];
			}
		}
		
		return unobfuscated;
	}

	public static String getMethod(String class1, String unobfuscated) {
		for(String[] cl : methods) {
			if(StringUtils.equalsIgnoreCase(cl[1], unobfuscated)
					&& StringUtils.equalsIgnoreCase(cl[2], class1)) {
				return cl[0];
			}
		}
		
		return unobfuscated;
	}

	public static String getField(String class1, String unobfuscated) {
		for(String[] cl : fields) {
			if(StringUtils.equalsIgnoreCase(cl[1], unobfuscated)
					&& StringUtils.containsIgnoreCase(cl[2], class1)) {
				return cl[0];
			}
		}
		
		return unobfuscated;
	}
}
