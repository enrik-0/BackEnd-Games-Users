package edu.uclm.esi.ds.account.services;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public void register(String name, String email, String pwd) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);
		this.userDAO.save(user);
		sendGames(user.getName());

	}

	private void sendGames(String name) {
		JSONObject jso = new JSONObject();
		jso.put("name", name);
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
		    HttpPost request = new HttpPost("http://localhost/api/newUser?name=" + name);
		    StringEntity params = new StringEntity(jso.toString());
		    request.addHeader("content-type", "application/json");
		    request.setEntity(params);
		    httpClient.execute(request);
		} catch (Exception ex) {
		}
	}

	public void login(String name, String pwd) {
		User user = userDAO.findByName(name);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN
					, "Invalid credentials");
		String pwdEncripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		if (!user.getPwd().equals(pwdEncripted))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN
					, "Invalid credentials");
	}

}
