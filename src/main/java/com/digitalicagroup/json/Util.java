package com.digitalicagroup.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Util {

	public String requestJson(String endpoint, String method, Map<String, String> headers) throws IOException {
		URL url = new URL(endpoint);
		HttpURLConnection conn = null;		
		
		if (url.getProtocol().equalsIgnoreCase("https")) {
			conn = (HttpsURLConnection) url.openConnection();
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}

		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json");
		// adding headers
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			conn.setRequestProperty(key, headers.get(key));
		}
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
