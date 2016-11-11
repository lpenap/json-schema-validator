package com.digitalicagroup.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Util {

	public String requestJson(String endpoint, String method, Map<String, String> headers) throws IOException {
		URL url = new URL(endpoint);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json");
		// adding headers
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			conn.setRequestProperty(key, headers.get(key));
		}
		System.out.println("###########################\nRequest: " + endpoint);
		conn.connect();

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringBuffer buffer = new StringBuffer();
		String output;
		while ((output = br.readLine()) != null) {
			buffer.append(output);
		}

		conn.disconnect();

		return buffer.toString();
	}

	public boolean validate(String schemaPath, String JSON) {
		boolean valid = true;
		try {
			InputStream inputStream = getClass().getResourceAsStream(schemaPath);
			JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(new JSONObject(JSON));
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
			e.getCausingExceptions().stream().map(ValidationException::getMessage).forEach(System.out::println);
			valid = false;
		} catch (Exception e) {
			e.printStackTrace();
			valid = false;
		}
		return valid;
	}

	public String readFile(String path) {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				buffer.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
