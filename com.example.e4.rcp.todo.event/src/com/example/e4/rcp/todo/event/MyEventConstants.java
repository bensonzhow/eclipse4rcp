package com.example.e4.rcp.todo.event;

public class MyEventConstants {
	// Only used for event registration, you cannot
	// send out generic events
	public static final String TOPIC_TODO_DATA_UPDATE = "TOPIC_TODO_DATA_UPDATE/*";

	public static final String TOPIC_TODO_DATA_UPDATE_NEW = "TOPIC_TODO_DATA_UPDATE/NEW";

	public static final String TOPIC_TODO_DATA_UPDATE_DELETE = "TOPIC_TODO_DATA_UPDATE/DELETED";

	public static final String TOPIC_TODO_DATA_UPDATE_UPDATED = "TOPIC_TODO_DATA_UPDATE/UPDATED";
}
