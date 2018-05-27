package com.thiagobezerra.bankslips.model;

public interface BeanValidator<T> {
	boolean isValid(T object);
}
