package edu.uclm.esi.ds.account.services;

import java.util.Optional;

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
	}

	public void login(String name, String pwd) {
		User user = userDAO.findByName(name);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Invalid credentials");
		String pwdEncripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		if (!user.getPwd().equals(pwdEncripted))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Invalid credentials");
	}

	public User getUserById(String id) {
		return userDAO.findById(id).map(u -> (User) u).orElse(null);
	}
}
