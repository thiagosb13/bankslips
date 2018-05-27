package com.thiagobezerra.bankslips.service;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

public class BankSlipServiceTest {

	@Test(expected = InvalidBankSlipException.class)
	public void whenBackSlipIsValidShouldThrownAnException() {
		fail();
	}
	
	@Test
	public void shouldCalculateFineRates() {
		fail();
	}
}
