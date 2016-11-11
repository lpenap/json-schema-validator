package com.digitalicagroup.json.validator.properties;

import java.util.ArrayList;
import java.util.List;

public class Queue {
	protected int version;
	protected List<QueueItem> items;

	public Queue() {
		this.version = 1;
		this.items = new ArrayList<>();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<QueueItem> getItems() {
		return items;
	}

	public void setItems(List<QueueItem> items) {
		this.items = items;
	}

	public void addItem(QueueItem item) {
		this.items.add(item);
	}

}
