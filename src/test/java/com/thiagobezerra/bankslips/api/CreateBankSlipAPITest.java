package com.thiagobezerra.bankslips.api;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;

public class CreateBankSlipAPITest extends BaseBankSlipAPITest {
	@Test
	public void whenCreateABankSplipShouldReturn201StatusCode() throws Exception {
		when(validator.isValid(Mockito.any(BankSlip.class))).thenReturn(true);
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType))
        										.andExpect(status().isCreated());
	}
	
	@Test
	public void whenBankSlipIsValidShouldPersistIt() throws Exception {
		when(validator.isValid(Mockito.any(BankSlip.class))).thenReturn(true);
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType));
		
		verify(bankSlipRepository, only()).save(Mockito.any(BankSlip.class));
	}
	
	@Test
	public void whenBankSlipIsNotInformedShouldReturn400StatusCode() throws Exception {
		mockMvc.perform(post("/rest/bankslips/").content(json(null))
												.contentType(contentType))
												.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenBankSlipIsInvalidShouldReturn422StatusCode() throws Exception {
		when(validator.isValid(Mockito.any(BankSlip.class))).thenReturn(false);
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType))
												.andExpect(status().isUnprocessableEntity());
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
