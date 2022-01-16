package com.rindus.codingtask.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rindus.codingtask.CodingTaskApplication;
import com.rindus.codingtask.controllers.UserController;
import com.rindus.codingtask.dto.AddressDTO;
import com.rindus.codingtask.dto.CompanyDTO;
import com.rindus.codingtask.dto.GeoDTO;
import com.rindus.codingtask.dto.UserDTO;
import com.rindus.codingtask.utils.Constants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { CodingTaskApplication.class,
		UserController.class })
@AutoConfigureMockMvc
class UserControllerTests {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper objectMapper;

	private UserDTO userExpected;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
		userExpected = new UserDTO();
		userExpected.setId(Long.valueOf(1));
		userExpected.setName("Leanne Graham");
		userExpected.setUsername("Bret");
		userExpected.setEmail("Sincere@april.biz");
		userExpected.setPhone("1-770-736-8031 x56442");
		userExpected.setWebsite("hildegard.org");

		AddressDTO address = new AddressDTO();
		address.setCity("Gwenborough");
		address.setStreet("Kulas Light");
		address.setSuite("Apt. 556");
		address.setZipcode("92998-3874");
		GeoDTO geo = new GeoDTO();
		geo.setLat(-37.3159);
		geo.setLng(81.1496);
		address.setGeo(geo);
		userExpected.setAddress(address);

		CompanyDTO company = new CompanyDTO();
		company.setName("Romaguera-Crona");
		company.setCatchPhrase("Multi-layered client-server neural-net");
		company.setBs("harness real-time e-markets");
		userExpected.setCompany(company);
	}

	@Test
	public void whenGetUsersGet200() throws Exception {
		ResultActions resultActions = mvc.perform(get("/users")).andDo(print());

		resultActions.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		MvcResult result = resultActions.andReturn();
		UserDTO[] usersActual = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO[].class);
		UserDTO userActual = Arrays.stream(usersActual).filter(user -> user.getId().equals(Long.valueOf(1))).findFirst()
				.get();
		assertEquals(userExpected, userActual);
	}

	@Test
	public void whenGetUserGet200() throws Exception {
		ResultActions resultActions = mvc.perform(get("/users/1")).andDo(print());

		resultActions.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		MvcResult result = resultActions.andReturn();
		UserDTO userActual = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
		assertEquals(userExpected, userActual);
	}

	@Test
	public void whenGetUserGet404() throws Exception {
		ResultActions resultActions = mvc.perform(get("/users/2300")).andDo(print());

		resultActions.andExpect(status().isNotFound()).andExpect(content().string(Constants.USER_NOT_FOUND));
	}

	@Test
	public void whenPostUserGet201() throws Exception {
		userExpected.setId(null);
		userExpected.setEmail("hello@rromaguera.com");

		ResultActions resultActions = mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userExpected))).andDo(print());

		resultActions.andExpect(status().isCreated());
	}

	@Test
	public void whenPutUserGet201() throws Exception {
		userExpected.setEmail("hello@rromaguera.com");

		ResultActions resultActions = mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userExpected))).andDo(print());

		resultActions.andExpect(status().isOk());
	}

	@Test
	public void whenPatchUserGet200() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Matt");

		ResultActions resultActions = mvc.perform(patch("/users/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO))).andDo(print());

		resultActions.andExpect(status().isOk());
	}

	@Test
	public void whenPatchUserGet200NoContent() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("Matt");

		ResultActions resultActions = mvc.perform(patch("/users/12313").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDTO))).andDo(print());

		resultActions.andExpect(status().isOk());
		MvcResult result = resultActions.andReturn();
		assertTrue(result.getResponse().getContentAsString().isEmpty());
	}

	@Test
	public void whenDeleteUserGet200() throws Exception {
		ResultActions resultActions = mvc.perform(delete("/users/1")).andDo(print());

		resultActions.andExpect(status().isOk());
	}

	@Test
	public void whenGetJSONFileGet200() throws Exception {
		ResultActions resultActions = mvc.perform(get("/users/json")).andDo(print());

		resultActions.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(header().string("Content-Disposition", "attachment; filename=\"users.json\""));
	}
	
	@Test
	public void whenGetXMLFileGet200() throws Exception {
		ResultActions resultActions = mvc.perform(get("/users/xml")).andDo(print());

		resultActions.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(header().string("Content-Disposition", "attachment; filename=\"users.xml\""));

	}

}
