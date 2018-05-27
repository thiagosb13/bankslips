package com.thiagobezerra.bankslips.service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

import com.thiagobezerra.bankslips.model.BankSlip;

@Component
public class BankSlipValidator implements BeanValidator<BankSlip> {
	public boolean isValid(BankSlip bankSlip) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(bankSlip).isEmpty();
	}
}
