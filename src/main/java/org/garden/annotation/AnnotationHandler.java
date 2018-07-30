package org.garden.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationHandler {
	
	void handle(Annotation ann, Object bean);

}
