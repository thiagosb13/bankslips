package com.thiagobezerra.bankslips.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.Status;

public class ListBankSlipsAPITest extends BaseBankSlipAPITest {
	@Test
	public void shouldReturnAllOfBankSlipsOnDatabase() throws Exception {
		when(bankSlipService.listBankSlips()).thenReturn(bankslips());
		
		mockMvc.perform(get("/rest/bankslips/"))
		       .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		       .andExpect(jsonPath("$", hasSize(2)))
		       .andExpect(jsonPath("$[0].id", is("00000000-0000-03e8-0000-0000000007d0")))
		       .andExpect(jsonPath("$[0].due_date", is("2018-05-01")))
		       .andExpect(jsonPath("$[0].status", is("PENDING")))
		       .andExpect(jsonPath("$[0].total_in_cents", is(10000)))
		       .andExpect(jsonPath("$[0].customer", is("Customer 01")))
		       .andExpect(jsonPath("$[1].id", is("00000000-0000-07d0-0000-000000000bb8")))
		       .andExpect(jsonPath("$[1].due_date", is("2018-05-10")))
		       .andExpect(jsonPath("$[1].status", is("PAID")))
		       .andExpect(jsonPath("$[1].total_in_cents", is(20000)))
		       .andExpect(jsonPath("$[1].customer", is("Customer 02")));
	}
	
	@Test
	public void shouldNotSerializeStatusWhenListingBankSlips() {
	    fail();
	}
	
	@Test
	public void shouldReturn200StatusCodeAsResponse() throws Exception {
		mockMvc.perform(get("/rest/bankslips/"))
               .andExpect(status().isOk());
	}
	
	private List<BankSlip> bankslips() {
		BankSlip bankslip01 = new BankSlip();
		bankslip01.setDueDate(LocalDate.of(2018, 5, 1));
		bankslip01.setStatus(Status.PENDING);
		bankslip01.setTotalInCents(new BigDecimal("10000"));
		bankslip01.setId(new UUID(1000, 2000));
		bankslip01.setCustomer("Customer 01");

		BankSlip bankslip02 = new BankSlip();
		bankslip02.setDueDate(LocalDate.of(2018, 5, 10));
		bankslip02.setStatus(Status.PAID);
		bankslip02.setTotalInCents(new BigDecimal("20000"));
		bankslip02.setId(new UUID(2000, 3000));
		bankslip02.setCustomer("Customer 02");
		
		return Arrays.asList(bankslip01, bankslip02);
	}
}
