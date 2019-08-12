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

import com.penapereira.json.validator.exception.ResourceNotFoundException;
import com.penapereira.json.validator.properties.Queue;
import com.penapereira.json.validator.properties.QueueItem;
import com.penapereira.json.validator.util.Util;
import com.penapereira.json.validator.util.UtilInterface;

public class JSONValidator {
	protected Queue queue;
	private UtilInterface util = new Util();
	private static Logger log = LoggerFactory.getLogger(JSONValidator.class);

	public JSONValidator(Queue queue) {
		this.queue = queue;
	}

	public JSONValidator() {
	}

	public void executeQueue() {
		log.info("  Validating " + this.queue.getItems().size() + " elements in queue...");
		for (QueueItem item : this.queue.getItems()) {
			log.debug("  # checking " + item.getPath());
			String result = "[OK]";
			try {
				processQueueItem(item);
			} catch (ResourceNotFoundException e) {
				result = "[ERROR] Resource not found";
			} catch (IOException e) {
				result = "[ERROR] I/O Error reading content";
			} catch (JSONException e) {
				result = "[ERROR] Invalid Json";
			} catch (ValidationException e) {
				result = "[ERROR] Invalid against schema";
			}
			log.info(String.format("  %s -> %s !", item.getPath(), result));
		}
	}

	private void processQueueItem(QueueItem item)
			throws ResourceNotFoundException, IOException, ValidationException, JSONException {
		String rawJson;
		if (item.isRemote()) {
			rawJson = util.requestJson(item.getPath(), item.getMethod(), item.getHeaders());
		} else {
			rawJson = util.getFileContents(item.getPath());
		}
		validate(item, rawJson);
	}

	public void validate(QueueItem item, String Json) throws JSONException, ValidationException {
		InputStream inputStream = getClass().getResourceAsStream(item.getSchema());
		Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
		if (item.isArray()) {
			schema.validate(new JSONArray(Json));
		} else {
			schema.validate(new JSONObject(Json));
		}
	}

}
