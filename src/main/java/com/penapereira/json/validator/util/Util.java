package com.penapereira.json.validator.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penapereira.json.validator.exception.ResourceNotFoundException;

public class Util implements UtilInterface {

	private static Logger log = LoggerFactory.getLogger(Util.class);

	@Override
	public String requestJson(String endpoint, String method, Map<String, String> headers) throws IOException, ResourceNotFoundException {
		HttpURLConnection conn = createConnection(endpoint, method, headers);
		conn.connect();
		if (conn.getResponseCode() != 200) {
			throw new ResourceNotFoundException("Failed : HTTP error code : " + conn.getResponseCode());
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
		HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();

		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json");

		// adding headers
		for (String key : headers.keySet()) {
			conn.setRequestProperty(key, headers.get(key));
		}
		return conn;
	}

	private String readInputStream(InputStream input) {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String currentLine;
		try {
			while ((currentLine = reader.readLine()) != null) {
				stringBuffer.append(currentLine);
			}
		} catch (IOException e) {
			log.error("Error (IOException)reading input stream from file ", e);
		}
		return stringBuffer.toString();
	}

	private String readResourceFile(String path) throws ResourceNotFoundException {
		InputStream input = getClass().getResourceAsStream(path);
		if (input == null) {
			throw new ResourceNotFoundException(String.format("Preferences not found in path: %s", path));
		}
		return readInputStream(input);
	}

	private String readFile(String path) throws FileNotFoundException {
		return readInputStream(new FileInputStream(path));
	}

	@Override
	public String getFileContents(String path) throws ResourceNotFoundException {
		String contents = "";
		boolean notFoundInFileSystem = false;
		try {
			contents = readFile(path);
		} catch (FileNotFoundException ignored) {
			notFoundInFileSystem = true;
		}
		if (notFoundInFileSystem) {
			contents = readResourceFile(path);
		}
		return contents;
	}

}
