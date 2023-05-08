package edu.uclm.esi.ds.services;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.controller.Manager;
import edu.uclm.esi.ds.entities.Token;
import edu.uclm.esi.ds.entities.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EmailService {

	public void sendConfirmationEmail(User user, Token token) throws IOException {
		String bodyHtml = Manager.get().readFile("resources.txt");
		bodyHtml= bodyHtml.replace("#TOKEN#", token.getId());
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		
		JSONObject jsoSender = new JSONObject().
				put("name","Juegos, S.A").
				put("email", "mario.t.m2001@gmail.com");
		
		JSONObject jsoTo = new JSONObject().
				put("email", user.getEmail()).
				put("name", user.getName());
		JSONArray jsaTo=new JSONArray().
				put(jsoTo);
		
		JSONObject jsoBody=new JSONObject();
		jsoBody.put("sender", jsoSender);
		jsoBody.put("to", jsaTo);
		jsoBody.put("subject", "Bienvenido a los juegos");
		jsoBody.put("htmlContent", bodyHtml);
		System.out.println(jsoBody.toString());
		RequestBody body = RequestBody.create(mediaType,jsoBody.toString());
		Request request = new Request.Builder()
			.url("https://api.sendinblue.com/v3/smtp/email")
			.method("POST", body)
			.addHeader("Accept", " application/json")
			.addHeader("Api-key","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")
			.addHeader("Content-Type", "application/json")
			.build();
		try {
			Response response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
