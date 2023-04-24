package edu.uclm.esi.ds.account.http;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TestUser {
	@Autowired
	private MockMvc server;

	@Test
	@Order(1)
	void testRegister() throws Exception {
		ResultActions result = this.sendRegister("Pepe", "pepe@pepe.com",
				"pepe123", "pepe123");
		result.andExpect(status().isOk()).andReturn();

		result = this.sendRegister("Ana", "Ana@ana.com", "Pasword123", "Password123");
		result.andExpect(status().isNotAcceptable()).andReturn();

		result = this.sendRegister("Pepe", "pepe@pepe.com", "pepe123", "pepe123");
		result.andExpect(status().isConflict()).andReturn();

		result = this.sendRegister("Ana", "Ana@ana.com", "Password123", "Password123");
		result.andExpect(status().isOk()).andReturn();
	}

	@Test
	@Order(2)
	void testLogin() throws Exception {
		ResultActions result = this.sendLogin("Pepe",
				org.apache.commons.codec.digest.DigestUtils.sha512Hex("pepe123"));
		result.andExpect(status().isOk()).andReturn();
		assertTrue(result.andReturn().getResponse().getHeader("sessionID") != null);

		result = this.sendLogin("Ana", 
				org.apache.commons.codec.digest.DigestUtils.sha512Hex("Password123"));
		result.andExpect(status().isOk()).andReturn();

		result = this.sendLogin("Paco",
			org.apache.commons.codec.digest.DigestUtils.sha512Hex("Password123"));
		result.andExpect(status().isForbidden()).andReturn();
		assertTrue(result.andReturn().getResponse().getHeader("sessionID") == null);
	}

	private ResultActions sendLogin(String name, String pwd) throws Exception {
		JSONObject jsoUser = new JSONObject()
				.put("name", name)
				.put("pwd", pwd);

		RequestBuilder request = MockMvcRequestBuilders.put("/users/login")
				.contentType("application/json")
				.content(jsoUser.toString());

		ResultActions response = this.server.perform(request);

		return response;
	}

	private ResultActions sendRegister(String name, String email, String pwd1, String pwd2)
			throws Exception, UnsupportedEncodingException {
		JSONObject jsoUser = new JSONObject()
				.put("name", name)
				.put("email", email)
				.put("pwd1", pwd1)
				.put("pwd2", pwd2);

		RequestBuilder request = MockMvcRequestBuilders.post("/users/register")
				.contentType("application/json")
				.content(jsoUser.toString());

		ResultActions response = this.server.perform(request);
		return response;
	}
}
