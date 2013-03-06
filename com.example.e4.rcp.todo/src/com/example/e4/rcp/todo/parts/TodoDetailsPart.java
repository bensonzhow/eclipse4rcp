package com.example.e4.rcp.todo.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.model.Todo;

public class TodoDetailsPart {
	private Text text;
	private Text text_1;
	private Control focus;
	private Todo todo;
	private Label summary;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		summary = new Label(parent, SWT.NONE);
		summary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		summary.setText("Summary");
		
		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description");
		
		text_1 = new Text(parent, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 116;
		text_1.setLayoutData(gd_text_1);
		
		Label lblDueDate = new Label(parent, SWT.NONE);
		lblDueDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDueDate.setText("Due Date");
		
		DateTime dateTime = new DateTime(parent, SWT.BORDER);
		GridData gd_dateTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dateTime.widthHint = 281;
		dateTime.setLayoutData(gd_dateTime);
		new Label(parent, SWT.NONE);
		
		Button btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
		
		focus = summary;
	}

	@PreDestroy
	private void dispose() {

	}

	@Focus
	private void setFocus() {
		focus.setFocus();
	}
	
	private void setTodo(@Optional
			@Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		if (todo != null) {
			this.todo = todo;
		}
		
		updateUserInterface(todo);
	}

	private void updateUserInterface(Todo todo) {
		if (todo == null) {
			return;
		}
		
		if (summary != null && !summary.isDisposed()) {
			summary.setText(todo.getSummary());
		}
	}
	
	

}
