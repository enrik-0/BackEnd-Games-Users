package edu.uclm.esi.ds.account.services;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.dao.TokenDAO;
import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.Token;
import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.exceptions.EmailNotSentException;

@Service
public class UserService {
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TokenDAO tokenDAO;
	//sessionID -> User
	private HashMap<String, User> users = new HashMap<String, User>();

	@Transactional
	public void register(String name, String email, String pwd) throws IOException, EmailNotSentException {
		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);

		Token token = new Token();
		token.setUser(user);

		this.userDAO.save(user);
		this.tokenDAO.save(token);

		this.emailService.sendConfirmationEmail(user, token);
	}

	public User login(String name, String pwd, String sessionID) {
		User user = userDAO.findByName(name);

		if (user != null && user.getConfirmationTime() != null &&
				user.getPwd().equals(pwd))
			users.put(sessionID, user);
		else
			user = null;

		return user;
	}

	public void confirm(String tokenId) {
		Token token = this.tokenDAO.findById(tokenId).orElse(null);

		if (token == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		if (!checkTokenValidity(token)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		User user = token.getUser();
		user.setConfirmationTime(System.currentTimeMillis());
		
		this.userDAO.save(user);
		this.tokenDAO.delete(token);
	}

	public User getUserBySessionID(String sessionID) {
		return users.get(sessionID);
	}

	/**
	 * Checks that no more than 15 minutes have passed since token creation time.
	 * 
	 * @param token
	 * @return true if less than 15 minutes have passed, false otherwise.
	 */
	private boolean checkTokenValidity(Token token) {
		boolean isValid = true;
		long elapsedTime = System.currentTimeMillis() - token.getCreationTime();
		
		if (elapsedTime > 15*60000) {
			isValid = false;
		}
		
		return isValid;
	}
}
