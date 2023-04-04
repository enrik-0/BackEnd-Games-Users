package edu.uclm.esi.ds.account.http;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.uclm.esi.ds.account.entities.User;
	
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestAPI {
	@Autowired
	MockMvc server;
	
	@Test
	void getUserTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/api/getUser?id=b2387495-3906-4af0-b6e9-0b166813b0bb");
		ResultActions response = this.server.perform(request);
		MvcResult result = response.andExpect(status().isOk()).andReturn();

		MockHttpServletResponse http = result.getResponse();
		String payload = http.getContentAsString();
		JSONObject json = new JSONObject(payload);

		assertTrue(json.get("id").equals("b2387495-3906-4af0-b6e9-0b166813b0bb"));
		System.out.println(json.toString());
	}

	@Test
	void getUserNotFoundTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/api/getUser?id=1234");
		ResultActions response = this.server.perform(request);
		MvcResult result = response.andExpect(status().isNotFound()).andReturn();
	}
}
