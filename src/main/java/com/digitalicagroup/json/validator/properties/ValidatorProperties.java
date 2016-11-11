package com.digitalicagroup.json.validator.properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.digitalicagroup.json.Util;

public class ValidatorProperties {
	protected final String schema = "/schema/json-validator-properties-schema.json";
	protected final String properties = "/json-validator-properties.json";

	public Queue loadQueue() throws UnsupportedPropertiesVersionException, InvalidPropertiesException {
		Util util = new Util();
		String propertiesRaw = util.readFile(properties);
		if (!util.validate(schema, propertiesRaw)) {
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
			i++;
		}
		return queue;
	}

}
