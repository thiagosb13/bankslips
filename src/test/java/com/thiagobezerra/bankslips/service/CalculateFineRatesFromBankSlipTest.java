package com.thiagobezerra.bankslips.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;

public class CalculateFineRatesFromBankSlipTest {
	@Mock
	protected BankSlipRepository bankSlipRepository;  

	@Mock
	protected BankSlipValidator bankSlipValidator;  
	
	private BankSlipService bankSlipService;
	
	@Before
    public void setup() throws Exception {
        this.bankSlipService = new BankSlipService(bankSlipRepository, bankSlipValidator);
    }
	
	@Test
	public void whenBankSlipIsNotDelayedShouldReturnZeroAsFine() {
		BankSlip bankSlip = newBankSlip();
		bankSlip.setDueDate(LocalDate.now().plusDays(5));
		
		bankSlipService.calculateFineRates(bankSlip);
		
		assertThat(bankSlip.getFine(), is(new BigDecimal("0")));
	}

	@Test
	public void whenBankSlipIsLessOrEqualToTenDaysDelayedShouldIncreaseAHalfPercentOfFine() {
		BankSlip bankSlip = newBankSlip();
		bankSlip.setDueDate(LocalDate.now().minusDays(5));
		
		bankSlipService.calculateFineRates(bankSlip);
		
		assertThat(bankSlip.getFine(), is(new BigDecimal("50.000")));
	}
	
	@Test
	public void whenBankSlipIsMoreThanTenDaysDelayedShouldIncreaseOnePercentOfFine() {
		BankSlip bankSlip = newBankSlip();
		bankSlip.setDueDate(LocalDate.now().minusDays(15));
		
		bankSlipService.calculateFineRates(bankSlip);
		
		assertThat(bankSlip.getFine(), is(new BigDecimal("100.00")));
	}
	
	private BankSlip newBankSlip() {
		BankSlip bankslip = new BankSlip();
		bankslip.setStatus(Status.PENDING);
		bankslip.setTotalInCents(new BigDecimal("10000"));
		bankslip.setId(new UUID(1000, 2000));
		bankslip.setCustomer("Customer 01");	
		
		return bankslip;
	}
}
