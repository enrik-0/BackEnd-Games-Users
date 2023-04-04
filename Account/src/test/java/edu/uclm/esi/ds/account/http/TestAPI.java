package edu.uclm.esi.ds.account.http;


import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import edu.uclm.esi.ds.account.entities.User;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class TestAPI {
	@Autowired
	private MockMvc server;
	@Test
	public void testgetUser() throws Exception {

		JSONObject jso = new JSONObject();
		jso.put("name", "vaca");
		this.register("vaca", "vaca@vaca.com", "vaca123@", "vaca123@");
		
		ResultActions response = this.sendRequest(jso, "http://pepitogrillo.com", "abcd");
		response.andExpect(status().isForbidden());

		response = this.sendRequest(jso, "http://localhost:80", "abc");
		response.andExpect(status().isForbidden());


		response = this.sendRequest(jso, "http://localhost:80", "abcd");
		response.andExpect(status().isOk()).andReturn();

		jso = new JSONObject();
		jso.put("name","yo");
		
		response = this.sendRequest(jso, "http://localhost:80", "abcd");
		response.andExpect(status().isNotFound());
	}
	
	private ResultActions sendRequest(JSONObject jso, String origin, String property) throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("http://localhost/api/getUser?")
				.contentType("application/json")
				.header("origin", origin)
				.header("Property",property)
				.content(jso.toString());
		ResultActions response = this.server.perform(request);
		return response;

	}

	private ResultActions register(String name, String email, String pwd1, String pwd2)
			throws Exception, UnsupportedEncodingException {
		JSONObject jsoUser = new JSONObject()
				.put("name", name)
				.put("email", email)
				.put("pwd1", pwd1)
				.put("pwd2", pwd2);
		RequestBuilder request = MockMvcRequestBuilders.post("/users/register?")
				.contentType("application/json")
				.content(jsoUser.toString());

		ResultActions response = this.server.perform(request);
		
		return response;
	}
}
