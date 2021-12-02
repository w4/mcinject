package io.github.jordandoyle.mcinject.proxy;

import java.lang.reflect.InvocationHandler;

public interface Proxy extends InvocationHandler {
	public Object proxy();
}
