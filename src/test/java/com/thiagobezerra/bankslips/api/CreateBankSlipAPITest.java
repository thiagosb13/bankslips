package com.thiagobezerra.bankslips.api;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
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
public class CreateBankSlipAPITest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
												  MediaType.APPLICATION_JSON.getSubtype(),
												  Charset.forName("utf8"));
	
	private HttpMessageConverter httpMessageConverter;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@MockBean
	private BeanValidator<BankSlip> validator;
	
	@MockBean
	private BankSlipRepository bankSlipRepository;
	
	private MockMvc mockMvc;
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.httpMessageConverter = Arrays.asList(converters)
        								  .stream()
        								  .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
        								  .findAny()
        								  .orElse(null);

        assertThat(this.httpMessageConverter, notNullValue());
    }
	
	@Test
	public void whenCreateABankSplipShouldReturn201StatusCode() throws Exception {
		when(validator.isValid(Mockito.any(BankSlip.class))).thenReturn(true);
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType))
        										.andExpect(status().isCreated())
        										.andExpect(status().reason("Bankslip created"));
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
												.andExpect(status().isBadRequest())
												.andExpect(status().reason("Bankslip not provided in the request body."));
	}
	
	@Test
	public void whenBankSlipIsInvalidShouldReturn422StatusCode() throws Exception {
		when(validator.isValid(Mockito.any(BankSlip.class))).thenReturn(false);
		
		mockMvc.perform(post("/rest/bankslips/").content(json(newBankSlip()))
												.contentType(contentType))
												.andExpect(status().isUnprocessableEntity())
												.andExpect(status().reason("Invalid bankslip provided.The possible reasons are: A field of the provided bankslip was null or with invalid values."));
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
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
