package com.digitalicagroup.json.validator.properties;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.digitalicagroup.json.Util;
import com.digitalicagroup.json.validator.JSONValidator;

public class ValidatorProperties {
	protected static final String schema = "/schema/json-validator-properties-schema.json";
	protected static final String properties = "/json-validator-properties.json";

	public static Queue loadQueue() throws UnsupportedPropertiesVersionException, InvalidPropertiesException {
		Util util = new Util();
		String propertiesRaw = util.readFile(properties);
		JSONValidator validator = new JSONValidator();
		if (!validator.validate(schema, propertiesRaw)) {
			throw new InvalidPropertiesException("invalid json-validator-properties.json syntax");
		}
		JSONObject properties = new JSONObject(propertiesRaw);

		Queue queue = new Queue();
		if (queue.getVersion() != (int) properties.get("version")) {
			throw new UnsupportedPropertiesVersionException();
		}

		JSONArray queueItems = properties.getJSONArray("queue");
		int i = 0;
		while (!queueItems.isNull(i)) {
			JSONObject rawItem = queueItems.getJSONObject(i);
			QueueItem item = new QueueItem();
			item.setRemote((boolean) rawItem.get("is_remote"));
			item.setPath((String) rawItem.get("path"));
			item.setSchema((String) rawItem.get("schema"));
			queue.addItem(item);

			if (item.isRemote()) {
				item.setMethod(rawItem.isNull("method") ? "GET" : rawItem.getString("method"));
				Map<String, String> headers = new HashMap<String, String>();
				if (!rawItem.isNull("headers")) {
					JSONArray headersRaw = rawItem.getJSONArray("headers");
					int j = 0;
					while (!headersRaw.isNull(j)) {
						JSONObject pair = headersRaw.getJSONObject(j);
						headers.put(pair.getString("key"), pair.getString("value"));
						j++;
					}
				}
				item.setHeaders(headers);
			}
			i++;
		}
		return queue;
	}

}
