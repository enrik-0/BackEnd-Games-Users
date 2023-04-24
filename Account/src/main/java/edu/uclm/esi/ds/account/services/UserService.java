package edu.uclm.esi.ds.account.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	//sessionID -> User
	private HashMap<String, User> users = new HashMap<String, User>();

	public void register(String name, String email, String pwd) {
		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);

		this.userDAO.save(user);
	}

	public User login(String name, String pwd, String sessionID) {
		User user = userDAO.findByName(name);

		if (user != null && user.getPwd().equals(pwd))
			users.put(sessionID, user);
		else
			user = null;

		return user;
	}

	public User getUserBySessionID(String sessionID) {
		return users.get(sessionID);
	}
}

