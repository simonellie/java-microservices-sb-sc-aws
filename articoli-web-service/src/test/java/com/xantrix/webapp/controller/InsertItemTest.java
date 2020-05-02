package com.xantrix.webapp.controller;

import com.xantrix.webapp.ItemsWebServiceApplication;
import com.xantrix.webapp.entity.Item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import java.util.Locale;

import com.xantrix.webapp.repository.ItemRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ItemsWebServiceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InsertItemTest {
	 
    private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Before
	public void setup()	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	private String ApiBaseUrl = "/api/item";
	
	String JsonData =  
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
			"    \"ingredients\": null,\r\n" +
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
	public void A_testInsArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());

				assertThat(itemRepository.findByCodArt("123Test"))
				.extracting(Item::getCodArt)
				.isEqualTo("123Test");
	}
	
	@Test
	public void B_testErrInsArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.code").value(406))
				.andExpect(jsonPath("$.message").value("Add for item 123Test already available! It's impossible to use a POST method."))
				.andDo(print());
	}
	
	String ErrJsonData =  
					"{\r\n" + 
					"    \"codArt\": \"123Test\",\r\n" + 
					"    \"descrizione\": \"\",\r\n" +  //<<< Articolo privo di descrizione
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
					"    \"ingredients\": null,\r\n" +
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
	public void C_testErrInsArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ErrJsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.message").value("The Description field must have a number of characters between 6 and 80"))
				.andDo(print());
	}
	
	String JsonDataMod =  
			"{\r\n" + 
			"    \"codArt\": \"123Test\",\r\n" + 
			"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
			"    \"um\": \"PZ\",\r\n" + 
			"    \"codStat\": \"TESTART\",\r\n" + 
			"    \"pzCart\": 6,\r\n" + 
			"    \"pesoNetto\": 1.75,\r\n" + 
			"    \"idStatoArt\": \"2 \",\r\n" + //<<< Modifica Stato Articolo a 2
			"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
			"    \"barcode\": [\r\n" + 
			"        {\r\n" + 
			"            \"barcode\": \"12345678\",\r\n" + 
			"            \"idTipoArt\": \"CP\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"ingredients\": null,\r\n" +
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
	public void D_testUpdArticolo() throws Exception {
				
		mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonDataMod)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
		
		assertThat(itemRepository.findByCodArt("123Test"))
		.extracting(Item::getIdStatoArt)
		.isEqualTo("2");
	}
	
	String ErrJsonDataMod =  
			"{\r\n" + 
			"    \"codArt\": \"pippo123\",\r\n" + 
			"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
			"    \"um\": \"PZ\",\r\n" + 
			"    \"codStat\": \"TESTART\",\r\n" + 
			"    \"pzCart\": 6,\r\n" + 
			"    \"pesoNetto\": 1.75,\r\n" + 
			"    \"idStatoArt\": \"2 \",\r\n" + //<<< Modifica Stato Articolo a 2
			"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
			"    \"barcode\": [\r\n" + 
			"        {\r\n" + 
			"            \"barcode\": \"12345678\",\r\n" + 
			"            \"idTipoArt\": \"CP\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"ingredients\": null,\r\n" +
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
	public void E_testErrUpdArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ErrJsonDataMod)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Item pippo123 not available! It's impossible to use a PUT method."))
				.andDo(print());
	}
	
	@Test
	public void F_testDelArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/delete/123Test")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(200))
				.andExpect(jsonPath("$.message").value("Delete item 123Test execute with success."))
				.andDo(print());
	}
	
	@Test
	public void G_testErrDelArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/delete/123Test")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ErrJsonDataMod)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Item 123Test not available!"))
				.andDo(print());
	}
	
}
