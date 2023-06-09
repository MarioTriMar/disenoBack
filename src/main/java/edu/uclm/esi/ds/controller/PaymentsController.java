package edu.uclm.esi.ds.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.ds.domain.Match;
import edu.uclm.esi.ds.services.GameService;
import edu.uclm.esi.ds.services.UsersService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
	
	@Autowired
	private UsersService usersService;
	
	static {
		Stripe.apiKey = "sk_test_51MqB8XKhg9Z0Z1gkBOO9ySdgeazWaA9JyvJRQvca4gK1ABwgTZdJuLb3ekyqAi74wuFEjMc12FyTg6uEkwXwPPS100ZohJxgxv";
	}

	@RequestMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total=(long) Math.floor(amount*100);
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.
				Builder().
				setCurrency("eur").
				setAmount(total)
				.build();
		try {
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			return clientSecret;
	
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha podido procesar el pago");
		}
	}
	
	@PostMapping(name= "/paymentOk", consumes ="application/json")
	public void paymentOk(@RequestBody Map<String, String> info) {
		String token=info.get("token");
		
	}
	
	@PutMapping("/pagoCompletado")
	public void pagoCompletado(@RequestParam double amount, @RequestParam String idPlayer) {
		try {
			usersService.pagoCompletado(amount, idPlayer);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
	}
	
	
}
