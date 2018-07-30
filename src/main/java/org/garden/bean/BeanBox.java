package org.garden.bean;

public class BeanBox<T> {

	private String beanName;

	private T instance;

	public BeanBox(String beanName, T instance) {
		super();
		this.beanName = beanName;
		this.instance = instance;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public T getInstance() {
		return instance;
	}

	public void setInstance(T instance) {
		this.instance = instance;
	}

}
