package edu.uclm.esi.ds.account.http;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.services.UserService;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:80") 
public class APIController {
	@Autowired
	private UserService userService;

	@GetMapping("/getUser")
	public String getUser(@RequestParam String id) {
		JSONObject json =  new JSONObject();
		User user = this.userService.getUserById(id);
		
		if (user == null) {
			throw ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());

		return json.toString();
	}
}
