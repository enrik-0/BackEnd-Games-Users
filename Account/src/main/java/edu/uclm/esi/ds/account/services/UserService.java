package edu.uclm.esi.ds.account.services;

import java.util.HashMap;

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
	//cookie -> userId
	private HashMap<String, User> users = new HashMap<String, User>();

	public void register(String name, String email, String pwd) {
		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);

		this.userDAO.save(user);
	}

	public User login(String name, String pwd, String cookie) {
		
		User user = userDAO.findByName(name);
		if (user != null && user.getPwd().equals(pwd))
				users.put(cookie, user);
		else
			user = null;
		
		return user;
	}

	public User getUserByCookie(String cookie) {
		return users.get(cookie);
	}
}

