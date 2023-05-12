package edu.uclm.esi.ds.account.services;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.entities.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EmailService {

	public void sendConfirmationEmail(User user) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		
		JSONObject jsonBody = createJsonBody(user);
		
		RequestBody body = RequestBody.create(mediaType, jsonBody.toString());
		Request request = new Request.Builder()
		  .url("https://api.sendinblue.com/v3/smtp/email")
		  .method("POST", body)
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Accept", "application/json")
		  .addHeader("api-key", "hwzT263EFNpJAyHK")
		  .addHeader("Cookie", "__cf_bm=nzdkBS3OtFjVjz9mCuqVqRNePtvYaoyn04kQUlEOulk-1681196846-0-AdJ9l01WIhqfsKYenbeILcYJZ8CrodgjcaDa/6E4otTcGasg9ZjZ7ZFHuFNoI1pIAaHe3FgByr3OI7E0m6RJGkM=")
		  .build();

		Response response = client.newCall(request).execute();	
	}
	
	private JSONObject createJsonBody(User user) {
		JSONObject jsonSender = new JSONObject()
				.put("name", "Games S.A")
				.put("email", "AngelLuis.Rodriguez1@alu.uclm.es");

		JSONObject jsonTo = new JSONObject()
				.put("email", user.getEmail())
				.put("name", user.getName());
		
		JSONArray jsonArrayTo = new JSONArray().put(jsonTo);

		JSONObject jsonBody = new JSONObject()
				.put("sender", jsonSender)
				.put("to", jsonArrayTo)
				.put("subject", "Welcome to Games")
				.put("htmlContent", "Verify your account");
	
		return jsonBody;
	}
}
