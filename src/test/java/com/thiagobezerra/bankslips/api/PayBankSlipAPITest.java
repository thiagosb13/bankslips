package com.thiagobezerra.bankslips.api;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import com.thiagobezerra.bankslips.model.StatusWrapper;
import com.thiagobezerra.bankslips.service.exception.BankSlipNotFoundException;

public class PayBankSlipAPITest extends BaseBankSlipAPITest {

	@Test
	public void ifBankSlipIsNotFoundShouldReturn404StatusCode() throws Exception {
		doThrow(new BankSlipNotFoundException()).when(bankSlipService).pay(Mockito.any());
		
		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(json(StatusWrapper.PaidStatus())))
			   .andExpect(status().isNotFound());
	}
	
	@Test
	public void ifBankSlipIsValidShouldReturn200StatusCodeAfterPayIt() throws Exception {
		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json(StatusWrapper.PaidStatus())))
		.andExpect(status().isOk());
		
		verify(bankSlipService, only()).pay(Mockito.any(UUID.class));
	}
	
	@Test
	public void ifStatusIsDifferentFromCancelAndPaidShouldReturn422StatusCode() throws Exception {
		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json(new StatusWrapper())))
		.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void ifStatusIsNotValidShouldReturn422StatusCode() throws Exception {
		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(json(new InvalidObject())))
			   .andExpect(status().isUnprocessableEntity());
	}
	
	private class InvalidObject {
		private String param;

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}
}
