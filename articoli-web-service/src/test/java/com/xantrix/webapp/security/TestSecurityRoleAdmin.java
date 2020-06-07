package com.xantrix.webapp.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xantrix.webapp.ItemsWebServiceApplication;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

 

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ItemsWebServiceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSecurityRoleAdmin
{
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.defaultRequest(get("/")
				.with(user("admin").roles("USER","ADMIN"))) //active roles
				.apply(springSecurity()) //Activate security
				.build();

	}
	
	private String ApiBaseUrl = "/api/item";
	
	String JsonData =  
			"{\n" + 
			"    \"codArt\": \"002000301\",\n" + 
			"    \"descrizione\": \"ACQUA ULIVETO 15 LT\",\n" + 
			"    \"um\": \"PZ\",\n" + 
			"    \"codStat\": \"\",\n" + 
			"    \"pzCart\": 6,\n" + 
			"    \"pesoNetto\": 1.5,\n" + 
			"    \"idStatoArt\": \"1\",\n" + 
			"    \"dataCreaz\": \"2010-06-14\",\n" + 
			"    \"barcode\": [\n" + 
			"        {\n" + 
			"            \"barcode\": \"8008490000021\",\n" + 
			"            \"idTipoArt\": \"CP\"\n" + 
			"        }\n" + 
			"    ],\n" + 
			"    \"famAssort\": {\n" + 
			"        \"id\": 1,\n" + 
			"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\n" + 
			"    },\n" + 
			"    \"ingredient\": null,\n" +
			"    \"iva\": {\n" + 
			"        \"idIva\": 22,\n" + 
			"        \"descrizione\": \"IVA RIVENDITA 22%\",\n" + 
			"        \"aliquota\": 22\n" + 
			"    }\n" + 
			"}";
	
	@Test
	public void A_listArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/code/002000301")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
	
	String JsonData2 =  
			"{\r\n" + 
			"    \"codArt\": \"123Test\",\r\n" + 
			"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
			"    \"um\": \"PZ\",\r\n" + 
			"    \"codStat\": \"TESTART\",\r\n" + 
			"    \"pzCart\": 6,\r\n" + 
			"    \"pesoNetto\": 1.75,\r\n" + 
			"    \"idStatoArt\": \"1 \",\r\n" + 
			"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
			"    \"barcode\": [\r\n" + 
			"        {\r\n" + 
			"            \"barcode\": \"12345678\",\r\n" + 
			"            \"idTipoArt\": \"CP\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"ingredient\": null,\r\n" +
			"    \"iva\": {\r\n" + 
			"        \"idIva\": 22,\r\n" + 
			"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" + 
			"        \"aliquota\": 22\r\n" + 
			"    },\r\n" + 
			"    \"famAssort\": {\r\n" + 
			"        \"id\": 1,\r\n" + 
			"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" + 
			"    }\r\n" + 
			"}";
	
	@Test
	public void B_testInsArticolo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	public void C_testDelArticolo() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/delete/123Test")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200"))
				.andExpect(jsonPath("$.message").value("Delete item 123Test execute with success."))
				.andDo(print());
	}
	 
}
