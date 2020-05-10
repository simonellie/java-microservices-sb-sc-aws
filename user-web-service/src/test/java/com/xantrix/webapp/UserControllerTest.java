package com.xantrix.webapp;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.xantrix.webapp.repository.UserRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserWebServiceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
    private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Before
	public void setup()	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	String JsonData =  
			"{\n" + 
			"    \"userId\": \"Nicola\",\n" + 
			"    \"password\": \"123Stella\",\n" + 
			"    \"active\": \"Yes\",\n" +
			"    \"roles\": [\n" +
			"            \"USER\"\n" + 
			"        ]\n" + 
			"}";
	
	@Test
	public void A_testInsUtente1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	public void B_testListUserByUserId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/search/userid/Nicola")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				  
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.userId").exists())
				.andExpect(jsonPath("$.userId").value("Nicola"))
				.andExpect(jsonPath("$.password").exists())
				.andExpect(jsonPath("$.active").exists())
				.andExpect(jsonPath("$.active").value("Yes"))
				  
				.andExpect(jsonPath("$.roles[0]").exists())
				.andExpect(jsonPath("$.roles[0]").value("USER"))
				.andDo(print());
		
				assertThat(passwordEncoder.matches("123Stella", 
						userRepository.findByUserId("Nicola").getPassword()))
				.isEqualTo(true);
	}
	
	String JsonData2 = 
			"{\n" + 
			"    \"userId\": \"Admin\",\n" + 
			"    \"password\": \"VerySecretPwd\",\n" + 
			"    \"active\": \"Yes\",\n" +
			"    \"roles\": [\n" +
			"            \"USER\",\n" + 
			"            \"ADMIN\"\n" + 
			"        ]\n" + 
			"}";
	
	@Test
	public void C_testInsUtente2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	String JsonDataUsers = 
			"[\n" + 
			"	{\n" + 
			"	    \"userId\": \"Nicola\",\n" + 
			"	    \"password\": \"123Stella\",\n" + 
			"	    \"active\": \"Yes\",\n" +
			"	    \"roles\": [\n" +
			"		    \"USER\"\n" + 
			"		]\n" + 
			"	},\n" + 
			"	{\n" + 
			"	    \"userId\": \"Admin\",\n" + 
			"	    \"password\": \"VerySecretPwd\",\n" + 
			"	    \"active\": \"Yes\",\n" +
			"	    \"roles\": [\n" +
			"		    \"USER\",\n" + 
			"		    \"ADMIN\"\n" + 
			"		]\n" + 
			"	}\n" + 
			"]";
	
	@Test
	public void D_testGetAllUser() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/search/all")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //UTENTE 1
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].userId").exists())
				.andExpect(jsonPath("$[0].userId").value("Nicola"))
				.andExpect(jsonPath("$[0].password").exists())
				.andExpect(jsonPath("$[0].active").exists())
				.andExpect(jsonPath("$[0].active").value("Yes"))
				.andExpect(jsonPath("$[0].roles[0]").exists())
				.andExpect(jsonPath("$[0].roles[0]").value("USER")) 
				 //UTENTE 2
				.andExpect(jsonPath("$[1].id").exists())
				.andExpect(jsonPath("$[1].userId").exists())
				.andExpect(jsonPath("$[1].userId").value("Admin"))
				.andExpect(jsonPath("$[1].password").exists())
				.andExpect(jsonPath("$[1].active").exists())
				.andExpect(jsonPath("$[1].active").value("Yes"))
				.andExpect(jsonPath("$[1].roles[0]").exists())
				.andExpect(jsonPath("$[1].roles[0]").value("USER")) 
				.andExpect(jsonPath("$[1].roles[1]").exists())
				.andExpect(jsonPath("$[1].roles[1]").value("ADMIN")) 
				.andReturn();
		
				assertThat(passwordEncoder.matches("VerySecretPwd", 
						userRepository.findByUserId("Admin").getPassword()))
				.isEqualTo(true);
	}
	
	
	
	@Test
	public void E_testDelUtente1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/Nicola")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Utente Nicola Eseguita Con Successo"))
				.andDo(print());
	}
	
	@Test
	public void F_testDelUtente2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/Admin")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Utente Admin Eseguita Con Successo"))
				.andDo(print());
	}
	
	
	
}


