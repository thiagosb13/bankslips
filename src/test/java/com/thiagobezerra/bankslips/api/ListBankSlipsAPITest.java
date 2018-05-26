package com.thiagobezerra.bankslips.api;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.thiagobezerra.bankslips.BankslipsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@WebAppConfiguration
public class ListBankSlipsAPITest {

	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
	
	@Test
	public void shouldReturnAllOfBankSlipsOnDatabase() {
		fail();
	}
	
	@Test
	public void shouldReturn200StatusCodeAsResponse() throws Exception {
		mockMvc.perform(get("/rest/bankslips/"))
               .andExpect(status().isOk());
	}
}
