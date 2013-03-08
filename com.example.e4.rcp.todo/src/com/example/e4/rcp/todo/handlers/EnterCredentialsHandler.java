package com.example.e4.rcp.todo.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import com.example.e4.rcp.todo.dialogs.PasswordDialog;

public class EnterCredentialsHandler {
	
	@Inject @Preference(nodePath = "com.example.e4.rcp.todo", value="user") String userPref;
	@Inject @Preference(nodePath = "com.example.e4.rcp.todo", value="password") String passwordPref;
	
	@Execute
	private void execute(Shell shell, @Preference(nodePath = "com.example.e4.rcp.todo") IEclipsePreferences prefs) {
		PasswordDialog dialog = new PasswordDialog(shell);
		
		if (userPref != null && !userPref.equals("")) {
			dialog.setUser(userPref);
		}
		if (passwordPref != null && !passwordPref.equals("")) {
			dialog.setPassword(passwordPref);
		}
		
		if (dialog.open() == Window.OK) {
			String user = dialog.getUser();
			String password = dialog.getPassword();
			
			System.out.println(userPref);
			System.out.println(passwordPref);
			
			System.out.println(dialog.storePassword());
			
			if (dialog.storePassword()) {
				prefs.put("user", user);
				prefs.put("password", password);
			}
	
			try {
				System.out.println("flushing");
				prefs.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}

}
