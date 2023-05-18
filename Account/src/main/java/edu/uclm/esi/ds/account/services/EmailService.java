package edu.uclm.esi.ds.account.services;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.account.entities.Token;
import edu.uclm.esi.ds.account.entities.User;
import edu.uclm.esi.ds.account.exceptions.EmailNotSentException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class EmailService {
	
	public void sendConfirmationEmail(User user, Token token) throws EmailNotSentException, IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		String msg = readFile("confirmation_message.html.txt");
		msg = msg.replace("#nombre#", user.getName());
		msg = msg.replace("#TOKEN#", token.getId());

		JSONObject jsoTo = new JSONObject()
				.put("name", user.getName())
				.put("email", user.getEmail());
		
		JSONObject emailParameters = new JSONObject(readFile("sendinblue.parameters.txt"));
		JSONObject payload = new JSONObject()
				.put("sender", emailParameters.getJSONObject("sender"))
				.put("to", new JSONArray().put(jsoTo))
				.put("subject", emailParameters.getString("subject"))
				.put("htmlContent", msg);

		Request request = createRequest(emailParameters, payload);

		Response response = client.newCall(request).execute();	
		if (response.code() >= 400)
			throw new EmailNotSentException();
		
		response.close();
	}

	private Request createRequest(JSONObject emailParameters, JSONObject payload) throws JSONException {
		MediaType mediaType = MediaType.parse("application/json");
		JSONObject headers = emailParameters.getJSONObject("headers");	


		RequestBody body = RequestBody.create(payload.toString(), mediaType);
		Request request = new Request.Builder()
		  .url(emailParameters.getString("endpoint"))
		  .method("POST", body)
		  .addHeader("Content-Type", headers.getString("Content-Type"))
		  .addHeader("Accept", headers.getString("Accept"))
		  .addHeader("api-key", headers.getString("api-key"))
		  .addHeader("Cookie", "__cf_bm=nzdkBS3OtFjVjz9mCuqVqRNePtvYaoyn04kQUlEOulk-1681196846-0-AdJ9l01WIhqfsKYenbeILcYJZ8CrodgjcaDa/6E4otTcGasg9ZjZ7ZFHuFNoI1pIAaHe3FgByr3OI7E0m6RJGkM=")
		  .build();

		return request;
	}

	private String readFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		}
	}	
}
