package edu.uclm.esi.ds.account.http;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;
	
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestAPI {
	@Autowired
	MockMvc server;
	@Autowired
	UserDAO userDAO;
	
	@Test
	void getUserTest() throws Exception {
		User user = this.createUser("Jose");
		ResultActions loginResult = this.sendLogin(user.getName(), user.getPwd());
		String sessionID = loginResult.andReturn().getResponse().getHeader("sessionID");
		
		RequestBuilder request = MockMvcRequestBuilders.get("/api/getUser?sessionID=" + sessionID);
		ResultActions response = this.server.perform(request);
		MvcResult result = response.andExpect(status().isOk()).andReturn();

		MockHttpServletResponse http = result.getResponse();
		String payload = http.getContentAsString();
		JSONObject json = new JSONObject(payload);

		assertTrue(json.get("id").equals(user.getId()));
		System.out.println(json.toString());
	}

	@Test
	void getUserNotFoundTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/api/getUser?sessionID=1234");
		ResultActions response = this.server.perform(request);
		MvcResult result = response.andExpect(status().isNotFound()).andReturn();
	}
	
	/**
	 * Creates and saves into DB an user object with name = name,
	 * email = name@name.com and pwd = name123
	 * 
	 * @param name of the user
	 * @return User object
	 */
	private User createUser(String name) {
		User user = new User();

		user.setName(name);
		user.setEmail(name+"@"+name+".com");
		user.setPwd(
			org.apache.commons.codec.digest.DigestUtils.sha512Hex(name+"123")
		);

		this.userDAO.save(user);
		
		return user;
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
}
