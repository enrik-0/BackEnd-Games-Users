package edu.uclm.esi.ds.account.http;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public void register(@RequestBody Map<String, Object> data) {
		String name = data.get("name").toString();
		String email = data.get("email").toString();
		String pwd1 = data.get("pwd1").toString();
		String pwd2 = data.get("pwd2").toString();

		if (!pwd1.equals(pwd2))
			throw new ResponseStatusException(
				HttpStatus.NOT_ACCEPTABLE,
				"Passwords don't match"
			);

		try {
			this.userService.register(name, email, pwd1);
		} catch (DataIntegrityViolationException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, Object> data) {
		HttpHeaders headers = new HttpHeaders();
		String name = data.get("name").toString();
		String pwd = data.get("pwd").toString();
		String pwdEncripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		String uuid = UUID.randomUUID().toString();

		User user = this.userService.login(name, pwdEncripted, uuid);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Invalid credentials");

		headers.add("sessionID", uuid);
		ResponseEntity<String> res = new ResponseEntity<String>
		("buenas",headers,HttpStatus.OK);
		return res;
		
	}
}
