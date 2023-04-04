package edu.uclm.esi.ds.account.http;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.account.dao.UserDAO;
import edu.uclm.esi.ds.account.entities.User;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:80") 
public class APIController {

	@Autowired
	private UserDAO userDao;
	@GetMapping("/getUser")
	public String getUser(@RequestHeader("Property") String secure
			, @RequestBody Map<String, String> requested ) {
		if(!secure.equals("abcd"))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		JSONObject jso =  new JSONObject();	
		User user;
		try {
		user = this.userDao.findByName(requested.get("name"));
		
		 if(user.equals(null));
		}catch(NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		jso.put("id", user.getId());
		jso.put("name", user.getName());
		jso.put("email", user.getEmail());
		return jso.toString();
	}
	
}
