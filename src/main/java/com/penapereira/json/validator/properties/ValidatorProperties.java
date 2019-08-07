package com.penapereira.json.validator.properties;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.penapereira.json.validator.JSONValidator;
import com.penapereira.json.validator.util.Util;

public class ValidatorProperties {
	protected static final String schema = "/schema/json-validator-properties-schema.json";
	protected static final String properties = "/json-validator-properties.json";

	public static Queue loadQueue() throws UnsupportedPropertiesVersionException, InvalidPropertiesException {
		Util util = new Util();
		String propertiesRaw = util.readFile(properties);
		JSONValidator validator = new JSONValidator();
		QueueItem myProperties = new QueueItem();
		myProperties.setSchema(schema);
		if (!validator.validate(myProperties, propertiesRaw)) {
			throw new InvalidPropertiesException("invalid json-validator-properties.json syntax");
		}
		JSONObject properties = new JSONObject(propertiesRaw);

		Queue queue = new Queue();
		if (queue.getVersion() != (int) properties.get("version")) {
			throw new UnsupportedPropertiesVersionException();
		}

		JSONArray queueItems = properties.getJSONArray("queue");
		processQueueItems(queue, queueItems);
		return queue;
	}

	private static void processQueueItems(Queue queue, JSONArray queueItems) {
		int i = 0;
		while (!queueItems.isNull(i)) {
			JSONObject rawItem = queueItems.getJSONObject(i);
			QueueItem item = new QueueItem();
			item.setRemote((boolean) rawItem.get("is_remote"));
			item.setArray((boolean) rawItem.get("is_array"));
			item.setPath((String) rawItem.get("path"));
			item.setSchema((String) rawItem.get("schema"));
			queue.addItem(item);

			if (item.isRemote()) {
				processRemoteItem(rawItem, item);
			}
			i++;
		}
	}

	private static void processRemoteItem(JSONObject rawItem, QueueItem item) {
		item.setMethod(rawItem.isNull("method") ? "GET" : rawItem.getString("method"));
		Map<String, String> headers = new HashMap<String, String>();
		if (!rawItem.isNull("headers")) {
			JSONArray headersRaw = rawItem.getJSONArray("headers");
			processRemoteItemHeaders(headers, headersRaw);
		}
		item.setHeaders(headers);
	}

	private static void processRemoteItemHeaders(Map<String, String> headers, JSONArray headersRaw) {
		int j = 0;
		while (!headersRaw.isNull(j)) {
			JSONObject pair = headersRaw.getJSONObject(j);
			headers.put(pair.getString("key"), pair.getString("value"));
			j++;
		}
	}

}
