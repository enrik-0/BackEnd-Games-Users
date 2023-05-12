package edu.uclm.esi.ds.account.http;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentController {
	static {
		Stripe.apiKey = "sk_test_51MxoAtEjDCrL15M4z00iteOiJiMDyAkBTvYHHo3OWQP7fYE3zc7oQSSIrlvYcn4pChgCSB4EUBmxkL2Hcp8ex8pU00T2zI9bu5";
		}
	@Autowired
	private UserService userService;
	private HashMap<String, Integer> points = new HashMap<String, Integer>();
	private HashMap<String, User> users = new HashMap<String, User>();

	@GetMapping("/prepay")
	public String prepay(@RequestParam int amount,@RequestParam String sessionID ) {
		User user = this.userService.getUserBySessionID(sessionID);

		if(user != null) {
			long total = (long) amount * 100;
			int pointEarned = amount * 100;
			PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
			.setCurrency("eur")
			.setAmount(total)
			.build();
			PaymentIntent intent;
			try {

				intent = PaymentIntent.create(params);

				JSONObject jso = new JSONObject(intent.toJson());
				String clientSecret = jso.getString("client_secret"); 

				this.points.put(clientSecret, pointEarned);
				this.users.put(clientSecret, user);

				return clientSecret;
			} catch (StripeException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST
						,"payment failed");
			}
		}
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

	@PostMapping(value = "/paymentOK", consumes = "application/json")
	public void paymentOK(@RequestBody Map<String, String> info) {
		String token = info.get("token");
		User user = userService.getUserBySessionID(info.get("sessionID"));
		int pointsDiff = this.points.get(token);

		if (user != null && pointsDiff >= 0)
			if (user.getId().equals(users.get(token).getId())) {
				user.setPoints(pointsDiff);
				this.userService.updateUser(user);
			}
	}

	@GetMapping(value = "/getPoints", headers = "sessionID")
	public int getPoint(HttpServletRequest request, HttpServletResponse response) {
		String session = request.getHeader("sessionID");
		User user = userService.getUserBySessionID(session);

		if (user == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		else
			return user.getPoints();
	}
}
