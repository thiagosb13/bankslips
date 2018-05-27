package com.thiagobezerra.bankslips.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.thiagobezerra.bankslips.BankslipsApplication;
import com.thiagobezerra.bankslips.model.BankSlip;
import com.thiagobezerra.bankslips.model.BeanValidator;
import com.thiagobezerra.bankslips.model.Status;
import com.thiagobezerra.bankslips.service.BankSlipRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@WebAppConfiguration
public class ListBankSlipsAPITest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            									  MediaType.APPLICATION_JSON.getSubtype(),
            									  Charset.forName("utf8"));
	
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@MockBean
	private BankSlipRepository bankSlipRepository;
	
	@MockBean
	private BeanValidator<BankSlip> validator;
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void shouldReturnAllOfBankSlipsOnDatabase() throws Exception {
		when(bankSlipRepository.findAll()).thenReturn(bankslips());
		
		mockMvc.perform(get("/rest/bankslips/"))
		       .andExpect(content().contentType(contentType))
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
