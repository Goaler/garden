package org.goaler.garden;

import java.lang.reflect.AnnotatedType;

public class AnnotationApplicationContext{
	
	private String applicationName = "default";
	
	public AnnotationApplicationContext(Class<?> clazz) {
		registerComponents(clazz);
	}
	
	private void registerComponents(Class<?> clazz) {
		AnnotatedType[] anns = clazz.getAnnotatedInterfaces();
	}

	public String getApplicationName(){
		return applicationName;
	}
	
}
