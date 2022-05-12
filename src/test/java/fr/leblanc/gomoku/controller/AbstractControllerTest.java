package fr.leblanc.gomoku.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import fr.leblanc.gomoku.service.UserService;

abstract class AbstractControllerTest {
	
	protected static final String USER = "testUser@gmail.com";
	protected static final String PASSWORD = "testPassword";

	@Autowired
	protected UserService userService;
	
	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	void registration() throws Exception {
		
		if (userService.findUserByEmail(USER) == null) {
			mockMvc.perform(post("/registration").param("email", USER).param("password", PASSWORD).with(csrf()))
			.andExpect(status().isFound());
		}
		
		assertNotNull(userService.findUserByEmail(USER));
	}

}
