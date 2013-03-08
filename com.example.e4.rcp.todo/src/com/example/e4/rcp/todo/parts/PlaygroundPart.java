package com.example.e4.rcp.todo.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.e4.bundleresoruceloader.IBundleResourceLoader;

public class PlaygroundPart {

	private Label label;
	private Control focus;
	private Text text;
	private Button btnSearch;
	private Text target;

	@PostConstruct
	public void createControls(Composite parent, IBundleResourceLoader loader) {
		parent.setLayout(new GridLayout(2, false));

		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		label = new Label(parent, SWT.NONE);
		label.setImage(loader.loadImage(this.getClass(), Messages.PlaygroundPart_0));

		focus = text;
		
		target = new Text(parent, SWT.BORDER);
		target.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		

	}

	@PreDestroy
	private void dispose() {

	}

	@Focus
	private void setFocus() {
		focus.setFocus();
	}

}
