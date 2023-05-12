package edu.uclm.esi.ds.account.services;

<<<<<<< HEAD
import java.util.HashMap;
=======
import java.io.IOException;
>>>>>>> 6279f38 (minimal changes to email process)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.entities.Token;

@Service
public class UserService {
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserDAO userDAO;
	//sessionID -> User
	private HashMap<String, User> users = new HashMap<String, User>();

	public void register(String name, String email, String pwd) {
		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);
		Token token = new Token();
		token.setUser(user);

		this.userDAO.save(user);
		try {
			this.emailService.sendConfirmationEmail(user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void updateUser(User user) {
		this.userDAO.save(user);
		
	}
}

