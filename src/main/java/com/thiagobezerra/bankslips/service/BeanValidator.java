package com.thiagobezerra.bankslips.service;

public interface BeanValidator<T> {
	boolean isValid(T object);
}
