package edu.uclm.esi.ds.controller;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
	
	static {
		Stripe.apiKey = "sk_test_51MqB8XKhg9Z0Z1gkBOO9ySdgeazWaA9JyvJRQvca4gK1ABwgTZdJuLb3ekyqAi74wuFEjMc12FyTg6uEkwXwPPS100ZohJxgxv"; //Se accede en el objeto PaymentIntentCreateParams
	}

	@RequestMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total = (long) Math.floor(amount*100);
		
		PaymentIntentCreateParams params = new PaymentIntentCreateParams
				.Builder()
				.setCurrency("eur")
				.setAmount(total)
				.build();
		
		try {
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			return clientSecret;
		}catch (Exception e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha podido procesar el pago");
		}
		
	}

}
