package edu.uclm.esi.ds.account.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.entities.Token;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmailService emailService;

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

	public User login(String name, String pwd) {
		return userDAO.findByName(name);
	}

	public User getUserById(String id) {
		return userDAO.findById(id).map(u -> (User) u).orElse(null);
	}
}
