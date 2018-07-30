package org.goaler.garden.annotation;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解处理器工厂
 * 
 * @author goaler
 *
 */
public class AnnotationHandlerFactory {

	private Map<Annotation, AnnotationHandler> handlerMap = new ConcurrentHashMap<>();

	public void registerAnnHandler(Annotation ann, AnnotationHandler handler) {
		handlerMap.put(ann, handler);
	}

	public AnnotationHandler getAnnotationHandler(Annotation ann) {
		return handlerMap.get(ann);
	}

}
