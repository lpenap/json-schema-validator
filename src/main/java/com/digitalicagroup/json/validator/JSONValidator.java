package com.digitalicagroup.json.validator;

import java.io.IOException;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.digitalicagroup.json.Util;
import com.digitalicagroup.json.validator.properties.Queue;
import com.digitalicagroup.json.validator.properties.QueueItem;

public class JSONValidator {
	protected Queue queue;

	public JSONValidator(Queue queue) {
		this.queue = queue;
	}

	public JSONValidator() {

	}

	public void executeQueue() {
		Util util = new Util();
		for (QueueItem item : this.queue.getItems()) {
			System.out.println("# checking " + item.getPath());
			String rawJson = "";
			if (item.isRemote()) {
				try {
					rawJson = util.requestJson(item.getPath(), item.getMethod(), item.getHeaders());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				rawJson = util.readFile(item.getPath());
			}
			if (validate(item.getSchema(), rawJson)) {
				System.out.println("  -> valid!");
			} else {
				System.out.println("  -> invalid!");
			}

		}

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

}
