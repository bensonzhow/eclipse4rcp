package com.example.e4.rcp.todo.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.e4.core.services.events.IEventBroker;

import com.example.e4.rcp.todo.event.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class MyTodoModelImpl implements ITodoModel {

	static int current = 1;
	private List<Todo> model;
	private IEventBroker broker;

	public MyTodoModelImpl() {
		model = createInitialModel();
	}

	// Always return a new copy of the data
	@Override
	public List<Todo> getTodos() {

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ArrayList<Todo> list = new ArrayList<Todo>();
		for (Todo todo : model) {
			list.add(todo.copy());
		}
		return list;
	}

	// Saves or updates
	@Override
	public synchronized boolean saveTodo(Todo newTodo) {
		Todo updateTodo = null;
		for (Todo todo : model) {
			if (todo.getId() == newTodo.getId()) {
				updateTodo = todo;
			}
		}
		if (updateTodo != null) {
			updateTodo.setSummary(newTodo.getSummary());
			updateTodo.setDescription(newTodo.getDescription());
			updateTodo.setDone(newTodo.isDone());
			updateTodo.setDueDate(newTodo.getDueDate());
			broker.post(MyEventConstants.TOPIC_TODO_DATA_UPDATE_UPDATED, "Topic updated");
		} else {
			newTodo.setId(current++);
			model.add(newTodo);
			broker.post(MyEventConstants.TOPIC_TODO_DATA_UPDATE_NEW, "New topic");
		}

		return true;
	}

	@Override
	public Todo getTodo(long id) {
		for (Todo todo : model) {
			if (todo.getId() == id) {
				return todo.copy();
			}
		}
		return null;
	}

	@Override
	public boolean deleteTodo(long id) {
		Todo deleteTodo = null;
		for (Todo todo : model) {
			if (id == todo.getId()) {
				deleteTodo = todo;
			}
		}
		if (deleteTodo != null) {
			model.remove(deleteTodo);
			broker.post(MyEventConstants.TOPIC_TODO_DATA_UPDATE_DELETE, "Topic deleted");
			return true;
		}
		return false;
	}

	// Example data, change if you like
	private List<Todo> createInitialModel() {
		ArrayList<Todo> list = new ArrayList<Todo>();
		list.add(createTodo("Application model", "Learn the application model"));
		list.add(createTodo("DI", "@Inject looks interesting"));
		list.add(createTodo("SWT", "Learn Widgets"));
		list.add(createTodo("JFace", "Especially Viewers!"));
		list.add(createTodo("OSGi", "Services"));
		list.add(createTodo("CSS Styling",
				"Learn how to style your application"));
		list.add(createTodo("Compatibility Layer", "Run Eclipse 3.x"));
		return list;
	}

	private Todo createTodo(String summary, String description) {
		return new Todo(current++, summary, description, false, new Date());
	}

	@Override
	public void setEventBroker(IEventBroker broker) {
		this.broker = broker;
	}
}