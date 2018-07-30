package org.garden;

import org.garden.bean.BeanBox;

public interface BeanFactory {
	
	public BeanBox<?> registerBean(String name, BeanBox<?> beanBox);
	
	public Object getBean(String name);

	public <T> T getBean(String name, Class<T> clazz);
}
