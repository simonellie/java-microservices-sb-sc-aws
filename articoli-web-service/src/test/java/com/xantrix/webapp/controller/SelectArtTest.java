package com.xantrix.webapp.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
public class SelectArtTest {
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup()	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	private String ApiBaseUrl = "/api/items";
	
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
	
	/*
	 * 
	 */
	@Test
	public void A_listArtByEan() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/ean/8008490000021")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //articoli
				.andExpect(jsonPath("$.codArt").exists())
				.andExpect(jsonPath("$.codArt").value("002000301"))
				.andExpect(jsonPath("$.descrizione").exists())
				.andExpect(jsonPath("$.descrizione").value("ACQUA ULIVETO 15 LT"))
				.andExpect(jsonPath("$.um").exists())
				.andExpect(jsonPath("$.um").value("PZ"))
				.andExpect(jsonPath("$.codStat").exists())
				.andExpect(jsonPath("$.codStat").value(""))
				.andExpect(jsonPath("$.pzCart").exists())
				.andExpect(jsonPath("$.pzCart").value("6"))
				.andExpect(jsonPath("$.pesoNetto").exists())
				.andExpect(jsonPath("$.pesoNetto").value("1.5"))
				.andExpect(jsonPath("$.idStatoArt").exists())
				.andExpect(jsonPath("$.idStatoArt").value("1"))
				.andExpect(jsonPath("$.dataCreaz").exists())
				.andExpect(jsonPath("$.dataCreaz").value("2010-06-14"))
				 //barcode
				.andExpect(jsonPath("$.barcode[0].barcode").exists())
				.andExpect(jsonPath("$.barcode[0].barcode").value("8008490000021")) 
				.andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
				.andExpect(jsonPath("$.barcode[0].idTipoArt").value("CP")) 
				 //famAssort
				.andExpect(jsonPath("$.famAssort.id").exists())
				.andExpect(jsonPath("$.famAssort.id").value("1")) 
				.andExpect(jsonPath("$.famAssort.descrizione").exists())
				.andExpect(jsonPath("$.famAssort.descrizione").value("DROGHERIA ALIMENTARE")) 
				 //ingredient
				.andExpect(jsonPath("$.ingredient").isEmpty())
				 //Iva
				.andExpect(jsonPath("$.iva.idIva").exists())
				.andExpect(jsonPath("$.iva.idIva").value("22")) 
				.andExpect(jsonPath("$.iva.descrizione").exists())
				.andExpect(jsonPath("$.iva.descrizione").value("IVA RIVENDITA 22%"))
				.andExpect(jsonPath("$.iva.aliquota").exists())
				.andExpect(jsonPath("$.iva.aliquota").value("22"))	
				
				.andDo(print());
	}
	
	private String Barcode = "8008490002138";
	
	@Test
	public void B_ErrlistArtByEan() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/ean/" + Barcode)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Item with barcode " + Barcode + " not found."))
				.andDo(print());
	}
	
	@Test
	public void C_listArtByCodArt() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/code/002000301")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
	
	private String CodArt = "002000301b";
	
	@Test
	public void D_ErrlistArtByCodArt() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/code/" + CodArt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Item with code " + CodArt + " not found."))
				.andDo(print());
	}
	
	private String JsonData2 = "[" + JsonData + "]";

	@Test
	public void E_listArtByDesc() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/description/ACQUA ULIVETO 15 LT")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				//.andExpect(content().json(JsonData2)) 
				.andReturn();
	}
	
	@Test
	public void G_TestErrlistArtByDesc() throws Exception {
		String Filter = "123ABC";
		
		mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/search/description/" + Filter)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value(404))
				.andExpect(jsonPath("$.message").value("Items with description " + Filter + " not found."))
				.andReturn();
	}
}
