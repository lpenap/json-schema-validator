package com.penapereira.json.validator.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Util {

	public String requestJson(String endpoint, String method, Map<String, String> headers) throws IOException {
		HttpURLConnection conn = createConnection(endpoint, method, headers);
		conn.connect();
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		StringBuffer buffer = readStringFromConnection(conn);
		conn.disconnect();
		return buffer.toString();
	}

	private StringBuffer readStringFromConnection(HttpURLConnection conn) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringBuffer buffer = new StringBuffer();
		String output;
		while ((output = br.readLine()) != null) {
			buffer.append(output);
		}
		return buffer;
	}

	private HttpURLConnection createConnection(String endpoint, String method, Map<String, String> headers)
			throws MalformedURLException, IOException, ProtocolException {
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
		for (String key : headers.keySet()) {
			conn.setRequestProperty(key, headers.get(key));
		}
		return conn;
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
