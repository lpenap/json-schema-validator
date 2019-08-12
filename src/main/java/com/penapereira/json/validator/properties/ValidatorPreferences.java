package com.penapereira.json.validator.properties;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.penapereira.json.validator.JSONValidator;
import com.penapereira.json.validator.exception.ResourceNotFoundException;
import com.penapereira.json.validator.exception.UnsupportedPropertiesVersionException;
import com.penapereira.json.validator.util.Util;
import com.penapereira.json.validator.util.UtilInterface;

import lombok.Data;

@Data
public class ValidatorPreferences {
	protected final String schema = "/schema/json-validator-properties-schema.json";
	private static final String DEFAULT_PROPERTIES_FILE = "/example-properties.json";
	protected String propertiesFile = "";
	private UtilInterface util;

	private static ValidatorPreferences uniqueInstance = null;

	protected ValidatorPreferences() {
		propertiesFile = DEFAULT_PROPERTIES_FILE;
		util = new Util();
	}

	public static ValidatorPreferences instance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ValidatorPreferences();
		}
		return uniqueInstance;
	}

	public Queue getQueue() throws UnsupportedPropertiesVersionException, ResourceNotFoundException {
		return parseJsonPreferences(util.getFileContents(propertiesFile));
	}

	protected Queue parseJsonPreferences(String json) throws UnsupportedPropertiesVersionException {
		JSONValidator validator = new JSONValidator();
		QueueItem myProperties = new QueueItem();
		myProperties.setSchema(schema);
		validator.validate(myProperties, json);
		JSONObject properties = new JSONObject(json);

		Queue queue = new Queue();
		if (queue.getVersion() != (int) properties.get("version")) {
			throw new UnsupportedPropertiesVersionException();
		}

		JSONArray queueItems = properties.getJSONArray("queue");
		processQueueItems(queue, queueItems);
		return queue;
	}

	private void processQueueItems(Queue queue, JSONArray queueItems) {
		int i = 0;
		while (!queueItems.isNull(i)) {
			JSONObject rawItem = queueItems.getJSONObject(i++);
			QueueItem item = new QueueItem((boolean) rawItem.get("is_remote"), (String) rawItem.get("path"),
					(String) rawItem.get("schema"));
			item.setArray((boolean) rawItem.get("is_array"));
			queue.addItem(item);

			if (item.isRemote()) {
				processRemoteItem(rawItem, item);
			}
		}
	}

	private void processRemoteItem(JSONObject rawItem, QueueItem item) {
		item.setMethod(rawItem.isNull("method") ? "GET" : rawItem.getString("method"));
		Map<String, String> headers = new HashMap<String, String>();
		if (!rawItem.isNull("headers")) {
			JSONArray headersRaw = rawItem.getJSONArray("headers");
			processRemoteItemHeaders(headers, headersRaw);
		}
		item.setHeaders(headers);
	}

	private void processRemoteItemHeaders(Map<String, String> headers, JSONArray headersRaw) {
		int j = 0;
		while (!headersRaw.isNull(j)) {
			JSONObject pair = headersRaw.getJSONObject(j);
			headers.put(pair.getString("key"), pair.getString("value"));
			j++;
		}
	}

}
