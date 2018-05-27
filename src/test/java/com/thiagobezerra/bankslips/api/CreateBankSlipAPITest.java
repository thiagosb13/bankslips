package com.thiagobezerra.bankslips.api;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.exception.InvalidBankSlipException;

public class CreateBankSlipAPITest extends BaseBankSlipAPITest {
	@Test
	public void whenCreateABankSplipShouldReturn201StatusCode() throws Exception {
		doNothing().when(bankSlipService).save(Mockito.any(BankSlip.class));
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType))
        										.andExpect(status().isCreated());
	}
	
	@Test
	public void whenBankSlipIsValidShouldPersistIt() throws Exception {
		doNothing().when(bankSlipService).save(Mockito.any(BankSlip.class));
				
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType));
		
		verify(bankSlipService, only()).save(Mockito.any(BankSlip.class));
	}
	
	@Test
	public void whenBankSlipIsNotInformedShouldReturn400StatusCode() throws Exception {
		mockMvc.perform(post("/rest/bankslips/").content(json(null))
												.contentType(contentType))
												.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenBankSlipIsInvalidShouldReturn422StatusCode() throws Exception {
		doThrow(new InvalidBankSlipException()).when(bankSlipService).save(Mockito.any(BankSlip.class));
		
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
