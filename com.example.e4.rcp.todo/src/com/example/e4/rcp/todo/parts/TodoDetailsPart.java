package com.example.e4.rcp.todo.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDetailsPart {
	private Control focus;
	@Inject MDirtyable dirty;
	@Inject ITodoModel model;
	private Todo todo;
	private Text summary;
	private Text description;
	private DateTime dateTime;
	private Button btnDone;
	private DataBindingContext ctx = new DataBindingContext();
	private IChangeListener listener;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblSummary = new Label(parent, SWT.NONE);
		lblSummary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSummary.setText("Summary");

		summary = new Text(parent, SWT.BORDER);
		summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDescription.setText("Description");

		description = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_text_1.heightHint = 116;
		description.setLayoutData(gd_text_1);

		Label lblDueDate = new Label(parent, SWT.NONE);
		lblDueDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDueDate.setText("Due Date");

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1));
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");

		listener = new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event) {
				if (dirty != null) {
					dirty.setDirty(true);
				}
			}
		};

		focus = summary;
	}

	@PreDestroy
	private void dispose() {

	}

	@Focus
	private void setFocus() {
		focus.setFocus();
	}

	@Inject
	public void setTodo(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		if (todo == null) {
			return;
		}

		if (dirty != null && dirty.isDirty()) {
			if (MessageDialog
					.openConfirm(null, "Unsaved changes",
							"You have modified unsaved changes. Do you wish to save them?")) {
				save(dirty);
			}
		}
		this.todo = todo;
		updateUserInterface(todo);
	}

	private void updateUserInterface(Todo todo) {
		if (todo == null) {
			return;
		}

		if (summary != null && !summary.isDisposed()) {

			// Deregister change listener to the old bindings
			IObservableList providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().removeChangeListener(listener);
			}

			ctx.dispose();

			IObservableValue target = WidgetProperties.text(SWT.Modify)
					.observe(summary);
			IObservableValue model = PojoProperties.value(Todo.FIELD_SUMMARY)
					.observe(todo);
			ctx.bindValue(target, model);

			target = WidgetProperties.text(SWT.Modify).observe(description);
			model = PojoProperties.value(Todo.FIELD_DESCRIPTION).observe(todo);
			ctx.bindValue(target, model);

			target = WidgetProperties.selection().observe(btnDone);
			model = PojoProperties.value(Todo.FIELD_DONE).observe(todo);
			ctx.bindValue(target, model);

			IObservableValue observeSelectionDateTimeObserveWidget = WidgetProperties
					.selection().observe(dateTime);
			IObservableValue dueDateTodoObserveValue = PojoProperties.value(
					Todo.FIELD_DUEDATE).observe(todo);
			ctx.bindValue(observeSelectionDateTimeObserveWidget,
					dueDateTodoObserveValue, null, null);

			// Register for the change
			providers = ctx.getValidationStatusProviders();
			for (Object o : providers) {
				Binding b = (Binding) o;
				b.getTarget().addChangeListener(listener);
			}

		}
	}

	@Persist
	private void save(MDirtyable dirty) {
		model.saveTodo(todo);
		dirty.setDirty(false);
	}

}
