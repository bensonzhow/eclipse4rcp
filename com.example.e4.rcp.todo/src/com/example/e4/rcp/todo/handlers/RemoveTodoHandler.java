package com.example.e4.rcp.todo.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class RemoveTodoHandler {

	@Execute
	public void execute(ITodoModel model,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo, Shell shell) {
		if (todo != null) {
			model.deleteTodo(todo.getId());
		} else {
			MessageDialog.openInformation(shell, "Deletion not possible",
					"No todo selected");
		}
	}
}
