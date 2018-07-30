package org.garden.exception;

public class NoSuchBeanException extends RuntimeException {

	private static final long serialVersionUID = -7325169854690910396L;

	public NoSuchBeanException(Class<?> clazz) {
		super("没有定义bean:" + clazz);
	}

	public NoSuchBeanException(String msg){
		super("没有定义bean:" + msg);
	}
	
}
