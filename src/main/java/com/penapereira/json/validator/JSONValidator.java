package com.penapereira.json.validator;

import java.io.IOException;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penapereira.json.validator.properties.Queue;
import com.penapereira.json.validator.properties.QueueItem;
import com.penapereira.json.validator.util.Util;

public class JSONValidator {
	protected Queue queue;
	private static Logger log = LoggerFactory.getLogger(JSONValidator.class);

	public JSONValidator(Queue queue) {
		this.queue = queue;
	}

	public JSONValidator() {

	}

	public void executeQueue() {
		log.info("Validating " + this.queue.getItems().size() + "  elements in queue...");
		Util util = new Util();
		for (QueueItem item : this.queue.getItems()) {
			log.debug("# checking " + item.getPath());
			String rawJson = "";
			if (item.isRemote()) {
				try {
					log.info("Requesting remote Json: " + item.getPath());
					rawJson = util.requestJson(item.getPath(), item.getMethod(), item.getHeaders());
				} catch (IOException e) {
					log.error("Error requesting remote json", e);
				}
			} else {
				rawJson = util.readFile(item.getPath());
			}
			if (validate(item, rawJson)) {
				log.info(item.getPath() + " -> OK !");
			} else {
				log.error(item.getPath() + " -> INVALID !");
			}

		}

	}

	public boolean validate(QueueItem item, String Json) {
		boolean valid = true;
		try {
			InputStream inputStream = getClass().getResourceAsStream(item.getSchema());
			JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
			Schema schema = SchemaLoader.load(rawSchema);
			if (item.isArray()) {
				schema.validate(new JSONArray(Json));
			} else {
				schema.validate(new JSONObject(Json));
			}
		} catch (ValidationException | JSONException e) {
			log.error("Error validating json: " + e.getMessage());
			log.debug("Error while validating schema", e);
			valid = false;
		}
		return valid;
	}

}
