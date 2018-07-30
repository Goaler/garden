package org.goaler.garden.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.goaler.garden.BeanFactory;
import org.goaler.garden.exception.NoSuchBeanException;

public class DefaultBeanFactory implements BeanFactory{
	
	private Map<String, BeanBox<?>> beanMap = new ConcurrentHashMap<String, BeanBox<?>>(100);

	public BeanBox<?> registerBean(String name, BeanBox<?> beanBox) {
		return beanMap.put(name, beanBox);
	}

	public Object getBean(String name) {
		Object obj = beanMap.get(name).getInstance();
		if (obj != null) {
			return obj;
		}
		throw new NoSuchBeanException(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> clazz) {
		Object obj = beanMap.get(name).getInstance();
		if (clazz.isInstance(obj)) {
			return (T)obj;
		}
		throw new NoSuchBeanException(clazz);
	}

}
