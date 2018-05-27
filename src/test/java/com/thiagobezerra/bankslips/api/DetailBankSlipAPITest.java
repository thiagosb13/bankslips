package com.thiagobezerra.bankslips.api;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;

public class DetailBankSlipAPITest extends BaseBankSlipAPITest {

	@Test
	public void whenIdProvidedIsNotAUUIDShouldReturn400StatusCode() throws Exception {
		mockMvc.perform(get("/rest/bankslips/anything"))
	       	   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenABankSlipIsNotFoundShouldReturn404StatusCode() throws Exception {
		doThrow(new BankSlipNotFoundException()).when(bankSlipService).getDetailsById(Mockito.any(UUID.class));
		
		mockMvc.perform(get("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0"))
		       .andExpect(status().isNotFound());
	}
	
	@Test
	public void whenABankSlipIsFoundShouldReturn200StatusCode() throws Exception {
		when(bankSlipService.getDetailsById(Mockito.any(UUID.class))).thenReturn(newBankSlip());
		
		mockMvc.perform(get("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0"))
		       .andExpect(status().isOk());
	}
	
	@Test
	public void whenABankSlipIsFoundShouldReturnIt() throws Exception {
		when(bankSlipService.getDetailsById(Mockito.any(UUID.class))).thenReturn(newBankSlip());
		
		mockMvc.perform(get("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0"))
		       .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		       .andExpect(jsonPath("$.id", is("00000000-0000-03e8-0000-0000000007d0")))
		       .andExpect(jsonPath("$.due_date", is("2018-05-01")))
		       .andExpect(jsonPath("$.status", is("PENDING")))
		       .andExpect(jsonPath("$.total_in_cents", is(10000)))
		       .andExpect(jsonPath("$.customer", is("Customer 01")));
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
