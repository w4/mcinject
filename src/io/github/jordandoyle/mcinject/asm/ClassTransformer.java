package io.github.jordandoyle.mcinject.asm;

public abstract class ClassTransformer {
	public abstract void transform();
	
	protected Class loadClass(byte[] b, String className) {
		Class clazz = null;
		try {
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class cls = Class.forName("java.lang.ClassLoader");
			java.lang.reflect.Method method = cls.getDeclaredMethod(
					"defineClass", new Class[] { String.class, byte[].class,
							int.class, int.class });

			method.setAccessible(true);
			try {
				Object[] args = new Object[] { className, b, new Integer(0),
						new Integer(b.length) };
				clazz = (Class) method.invoke(loader, args);
			} finally {
				method.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return clazz;
	}
}
