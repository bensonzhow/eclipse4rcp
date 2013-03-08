package com.example.e4.rcp.todo.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.event.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDeletionPart {

	@Inject
	private ITodoModel model;
	private ComboViewer viewer;
	@Inject 
	private UISynchronize sync;
	@Inject 
	private IEventBroker broker;

	@PostConstruct
	public void postConstruct(Composite parent) {

		parent.setLayout(new GridLayout(2, false));
		viewer = new ComboViewer(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
		Combo combo = viewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				Todo todo = (Todo) element;
				return todo.getSummary();
			}
		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		updateViewer();

		Button button = new Button(parent, SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ISelection selection = viewer.getSelection();
				IStructuredSelection sel = (IStructuredSelection) selection;

				if (sel.size() > 0) {
					Todo firstElement = (Todo) sel.getFirstElement();
					model.deleteTodo(firstElement.getId());
					broker.post(MyEventConstants.TOPIC_TODO_DATA_UPDATE_DELETE, Messages.TodoDeletionPart_0);
					updateViewer();
				}
			}

		});

		button.setText(Messages.TodoDeletionPart_1);

	}

	private void updateViewer() {
		Job job = new Job(Messages.TodoDeletionPart_2) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				final List<Todo> todos = model.getTodos();
				sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						viewer.setInput(todos);
						if (todos.size() > 0) {
							viewer.setSelection(new StructuredSelection(todos
									.get(0)));
						}
						viewer.refresh();
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		// List<Todo> todos = model.getTodos();
	}
	
	@Inject
	@Optional
	private void getNotified(@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE) String topic) {
		if (viewer != null) {
			viewer.setInput(model.getTodos());
		}
	}

	public void focus() {
		viewer.getCombo().setFocus();
	}

}
