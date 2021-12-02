package io.github.jordandoyle.mcinject.event;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Target(value = METHOD)
@Retention(value = RUNTIME)
public @interface EventSubscribe {
	
}
