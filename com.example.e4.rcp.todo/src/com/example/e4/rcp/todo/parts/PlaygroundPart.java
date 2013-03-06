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

	@PostConstruct
	public void createControls(Composite parent, IBundleResourceLoader loader) {
		parent.setLayout(new GridLayout(5, false));

		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnSearch = new Button(parent, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnSearch.setText("Search");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		label = new Label(parent, SWT.NONE);
		label.setImage(loader.loadImage(this.getClass(), "images/vogella.png"));

		focus = text;
	}

	@PreDestroy
	private void dispose() {

	}

	@Focus
	private void setFocus() {
		focus.setFocus();
	}

}
