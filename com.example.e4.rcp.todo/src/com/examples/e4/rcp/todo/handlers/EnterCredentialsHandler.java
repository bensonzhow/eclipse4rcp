package com.examples.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.dialogs.PasswordDialog;

public class EnterCredentialsHandler {
	
	@Execute
	private void execute(Shell shell) {
		PasswordDialog dialog = new PasswordDialog(shell);
		
		if (dialog.open() == Window.OK) {
			// TODO: DO SOMETHING WITH THE VALUE
			System.out.println("OK!?");
		}
	}

}
