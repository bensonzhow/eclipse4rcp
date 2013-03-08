package com.example.e4.rcp.todo.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.event.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoOverviewPart {
	@Inject 
	private ESelectionService selectionService;
	@Inject 
	private ITodoModel model;
	@Inject 
	private UISynchronize sync;
	private TableViewer viewer;
	private String searchString = "";
	private WritableList writableList;
	private Button btnUpdate;

	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		parent.setLayout(new GridLayout(1, false));

		btnUpdate = new Button(parent, SWT.NONE);
		btnUpdate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		btnUpdate.setText("Update");
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Job job = new Job("Loading") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						final List<Todo> todos = model.getTodos();
						sync.asyncExec(new Runnable() {
							@Override
							public void run() {
								updateViewer(todos);
							}
						});
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			}
		});

		Text search = new Text(parent, SWT.SEARCH | SWT.CANCEL
				| SWT.ICON_SEARCH);

		search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		search.setMessage("Search");

		search.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				searchString = source.getText();
				viewer.refresh();
			}
		});

		search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.CANCEL) {
					Text text = (Text) e.getSource();
					text.setText("");
				}
			}
		});

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setText("Summary");

		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setText("Description");

		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				Todo todo = (Todo) element;
				return todo.getSummary().toLowerCase()
						.contains(searchString.toLowerCase())
						|| todo.getDescription().toLowerCase()
								.contains(searchString.toLowerCase());
			}
		});
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				selectionService.setSelection(selection.getFirstElement());
			}
		});
		
		menuService.registerContextMenu(table, "com.example.e4.rcp.todo.popupmenu.table");

		writableList = new WritableList(model.getTodos(), Todo.class);
		ViewerSupport.bind(
				viewer,
				writableList,
				BeanProperties.values(new String[] { Todo.FIELD_SUMMARY,
						Todo.FIELD_DESCRIPTION }));
	}

	public void updateViewer(List<Todo> list) {
		if (viewer != null) {
			writableList.clear();
			writableList.addAll(list);
		}
	}
	
	@Inject
	@Optional
	private void getNotified(@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE) String topic) {
		if (viewer != null) {
			updateViewer(model.getTodos());
		}
	}

	@PreDestroy
	private void dispose() {

	}

	@Focus
	private void setFocus() {
		btnUpdate.setFocus();
	}
}
