package edu.uclm.esi.ds.account.http;

import java.io.IOException;
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
		Stripe.apiKey = "sk_test_51Mo0XwH4w1q9YTZYdmUtkr8MVbkZrlwAiqIwXoYIWXg453p5ot6YHt5tqhHgU666WmVzuvjZsfS0vQcD0ubB0CSI00cMqGDzf3";
		}
	@Autowired
	private UserService userService;
	
	@RequestMapping("/aa")
	public void aa(@RequestParam String session) {
		User user = userService.getUserBySessionID(session);
		System.out.println(user);
	}

	@GetMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total = (long) Math.floor(amount * 100);
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
		.setCurrency("eur")
		.setAmount(total)
		.build();
		PaymentIntent intent;
		try {
			intent = PaymentIntent.create(params);
		JSONObject jso = new JSONObject(intent.toJson());
		String clientSecret = jso.getString("client_secret"); 
		return clientSecret;
		} catch (StripeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST
					,"payment failed");
		}
		}

	@PostMapping(value = "/paymentOK", consumes = "application/json")
	public void paymentOK(@RequestBody Map<String, String> info) {
		String token = info.get("token");
	}
	
	@GetMapping(value = "/getMoney", headers = "sessionId")
	public int getMoney(HttpServletRequest request, HttpServletResponse response) {
		String session = request.getHeader("sessionId");
		User user = userService.getUserBySessionID(session);
		if (user == null)
			try {
				response.sendRedirect("http://localhost:4200/login");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		return 2;
		
	}
}

