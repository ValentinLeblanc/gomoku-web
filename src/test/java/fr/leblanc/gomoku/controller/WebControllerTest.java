package fr.leblanc.gomoku.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import fr.leblanc.gomoku.model.Game;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class WebControllerTest extends AbstractControllerTest {
	
	@Test
	void loginPageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/login").with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();

		assertTrue(result.getResponse().getContentAsString().contains("<title>Login</title>"));
	}
	
	@Test
	void homePageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/").with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains("<title>Gomoku</title>"));
	}
	
	@Test
	void boardPageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/board").flashAttr("game", new Game()).with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains("<title>Gomoku Board</title>"));
	}
	
	@Test
	void onlinePageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/online").with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains("<title>Gomoku Online</title>"));
	}
	
	@Test
	void settingsPageTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/settings").with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains("<title>Settings</title>"));
	}
	
	
}
