package edu.uclm.esi.ds.account.http;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public String getUser(@RequestParam String sessionID) {
		JSONObject json = new JSONObject();
		User user = this.userService.getUserBySessionID(sessionID);

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("points", user.getPoints());

		return json.toString();
	}

	@PutMapping("/updatePoints")
	public void updatePoints(@RequestParam String sessionID, @RequestParam int ammount, @RequestParam boolean add) {
		User user = this.userService.getUserBySessionID(sessionID);

		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (add) {
			user.setPoints(ammount);
		} else {
			if ((user.getPoints() - ammount) < 0) {
				throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
			}
			user.removePoints(ammount);
		}
		
		this.userService.updateUser(user);
	}
}
