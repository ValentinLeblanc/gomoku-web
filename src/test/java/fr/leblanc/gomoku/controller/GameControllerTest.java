package fr.leblanc.gomoku.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.web.dto.MoveDTO;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@Transactional
@Rollback(false)
class GameControllerTest extends AbstractControllerTest {

	@Test
	@Order(2)
	void localGameTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/game/LOCAL").with(user(USER).password(PASSWORD)))
				.andExpect(status().isOk()).andReturn();

		assertEquals("/board", result.getResponse().getForwardedUrl());

		Game currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertNotNull(currentGame);

		assertEquals(GameType.LOCAL, currentGame.getType());

	}

	@Test
	@Order(3)
	void addMoveTest() throws Exception {

		Game currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertNotNull(currentGame);

		MoveDTO move = new MoveDTO(1, 5, 6, GomokuColor.BLACK.toNumber());

		Gson gson = new Gson();

		mockMvc.perform(post("/add-move/LOCAL").with(user(USER).password(PASSWORD)).content(gson.toJson(move))
				.contentType(MediaType.APPLICATION_JSON).with(csrf())).andExpect(status().isOk());

		move = new MoveDTO(2, 5, 7, GomokuColor.WHITE.toNumber());

		assertEquals(1, currentGame.getMoves().size());
		
		mockMvc.perform(post("/add-move/LOCAL").with(user(USER).password(PASSWORD)).content(gson.toJson(move))
				.contentType(MediaType.APPLICATION_JSON).with(csrf())).andExpect(status().isOk());

		currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertEquals(2, currentGame.getMoves().size());

	}

	@Test
	@Order(4)
	void undoMoveTest() throws Exception {

		Game currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertNotNull(currentGame);

		assertEquals(2, currentGame.getMoves().size());

		mockMvc.perform(post("/undo-move/LOCAL").with(user(USER).password(PASSWORD)).with(csrf())).andExpect(status().isOk());

		currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertEquals(1, currentGame.getMoves().size());

	}

	@Test
	@Order(5)
	void resetLocalGameTest() throws Exception {

		Game currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertNotNull(currentGame);

		assertEquals(1, currentGame.getMoves().size());

		mockMvc.perform(post("/reset-game/LOCAL").content("").contentType(MediaType.APPLICATION_JSON)
				.with(user(USER).password(PASSWORD)).with(csrf())).andExpect(status().isFound());

		currentGame = userService.findUserByEmail(USER).getCurrentLocalGame();

		assertNotNull(currentGame);

		assertTrue(currentGame.getMoves().isEmpty());

	}

//	@Test
//	@Order(6)
//	void onlineGameTest() throws Exception {
//		MvcResult result = mockMvc.perform(get("/game/ONLINE").with(user(USER).password(PASSWORD)))
//				.andExpect(status().isOk()).andReturn();
//
//		assertEquals("/online", result.getResponse().getForwardedUrl());
//	}
}
