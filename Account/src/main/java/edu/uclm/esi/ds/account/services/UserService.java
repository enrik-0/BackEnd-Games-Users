package edu.uclm.esi.ds.account.services;

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

	public User login(String name, String pwd) {
		return userDAO.findByName(name);
	}

	public User getUserById(String id) {
		return userDAO.findById(id).map(u -> (User) u).orElse(null);
	}
}
