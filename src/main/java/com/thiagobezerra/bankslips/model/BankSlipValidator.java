package com.thiagobezerra.bankslips.model;

import org.springframework.stereotype.Component;

@Component
public class BankSlipValidator implements BeanValidator<BankSlip> {
	public boolean isValid(BankSlip bankSlip) {
		return false;
	}
}
