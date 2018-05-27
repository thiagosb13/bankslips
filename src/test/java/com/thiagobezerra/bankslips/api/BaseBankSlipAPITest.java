package com.thiagobezerra.bankslips.api;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.runner.RunWith;
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
import com.thiagobezerra.bankslips.service.BankSlipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankslipsApplication.class)
@WebAppConfiguration
public abstract class BaseBankSlipAPITest {
	protected HttpMessageConverter httpMessageConverter;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	@MockBean
	protected BankSlipService bankSlipService;
	
	protected MockMvc mockMvc;
	
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
	
	protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
													MediaType.APPLICATION_JSON.getSubtype(),
													Charset.forName("utf8"));

	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
