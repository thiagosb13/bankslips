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

public class CancelBankSlipAPITest extends BaseBankSlipAPITest {

	@Test
	public void ifBankSlipIsNotFoundShouldReturn404StatusCode() throws Exception {
		doThrow(new BankSlipNotFoundException()).when(bankSlipService).cancel(Mockito.any());

		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json(StatusWrapper.CancelStatus())))
				.andExpect(status().isNotFound());
	}

	@Test
	public void ifBankSlipIsValidShouldReturn200StatusCodeAfterCancelIt() throws Exception {
		mockMvc.perform(put("/rest/bankslips/00000000-0000-03e8-0000-0000000007d0")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json(StatusWrapper.CancelStatus())))
				.andExpect(status().isOk());

		verify(bankSlipService, only()).cancel(Mockito.any(UUID.class));
	}
}
