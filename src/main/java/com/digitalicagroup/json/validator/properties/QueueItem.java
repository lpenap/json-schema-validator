package com.digitalicagroup.json.validator.properties;

import java.util.Map;

public class QueueItem {
	protected boolean isRemote;
	protected String path;
	protected String schema;
	
	protected String method;
	protected Map<String, String> headers;

	public boolean isRemote() {
		return isRemote;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}
