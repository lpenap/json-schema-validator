package com.penapereira.json.validator.properties;

import java.util.Map;

import lombok.Data;

@Data
public class QueueItem {
	protected boolean isRemote;
	protected boolean isArray;
	protected String path;
	protected String schema;
	protected String method;
	protected Map<String, String> headers;

	public QueueItem(boolean isRemote, String path, String schema) {
		setRemote(isRemote);
		setPath(path);
		setSchema(schema);
	}

	public QueueItem() {
	}
}
