package com.thiagobezerra.bankslips.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.BankSlipValidator;

public class BankSlipValidatorTest {

	@Test
	public void whenEveryBankSlipPropertyIsFilledShouldValidateObject() {
		BankSlipValidator validator = new BankSlipValidator();
		
		assertThat(validator.isValid(newBankSlip()), is(true));
	}
	
	@Test
	public void whenCustomerIsNullShouldNotValidateObject() {
		BankSlipValidator validator = new BankSlipValidator();
		
		BankSlip bankSlip = newBankSlip();
		bankSlip.setCustomer(null);
		
		assertThat(validator.isValid(bankSlip), is(false));
	}
	
	@Test
	public void whenDueDateIsNullShouldNotValidateObject() {
		BankSlipValidator validator = new BankSlipValidator();
		
		BankSlip bankSlip = newBankSlip();
		bankSlip.setDueDate(null);
		
		assertThat(validator.isValid(bankSlip), is(false));
	}
	
	@Test
	public void whenStatusIsNullShouldNotValidateObject() {
		BankSlipValidator validator = new BankSlipValidator();
		
		BankSlip bankSlip = newBankSlip();
		bankSlip.setStatus(null);
		
		assertThat(validator.isValid(bankSlip), is(false));
	}
	
	@Test
	public void whenTotalInCentsIsNullShouldNotValidateObject() {
		BankSlipValidator validator = new BankSlipValidator();
		
		BankSlip bankSlip = newBankSlip();
		bankSlip.setTotalInCents(null);
		
		assertThat(validator.isValid(bankSlip), is(false));
	}
	
	private BankSlip newBankSlip() {
		BankSlip bankslip = new BankSlip();
		bankslip.setDueDate(LocalDate.of(2018, 5, 1));
		bankslip.setStatus(Status.PENDING);
		bankslip.setTotalInCents(new BigDecimal("10000"));
		bankslip.setId(new UUID(1000, 2000));
		bankslip.setCustomer("Customer 01");	
		
		return bankslip;
	}
}
