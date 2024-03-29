package com.example.e4.rcp.todo.lifecycle;

import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.dialogs.PasswordDialog;

public class LifeCycleManager {

	@PostContextCreate
	private void postContextCreate() {
		// Inject the EventBRoker into the ITodoModel
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		PasswordDialog dialog = new PasswordDialog(shell);
		if (dialog.open() != Window.OK) {
			System.exit(-1);
		}
	}
	
}
