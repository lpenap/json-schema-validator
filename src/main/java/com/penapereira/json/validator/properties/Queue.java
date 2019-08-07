package com.penapereira.json.validator.properties;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Queue {
	protected int version;
	protected List<QueueItem> items;

	public Queue() {
		this.version = 2;
		this.items = new ArrayList<>();
	}

	public void addItem(QueueItem item) {
		this.items.add(item);
	}

}
