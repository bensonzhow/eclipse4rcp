package com.example.e4.rcp.todo.service.internal;
 
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import com.example.e4.rcp.todo.model.ITodoModel;
 
public class TodoServiceContextFunction implements
		org.eclipse.e4.core.contexts.IContextFunction {
 
	@Override
	public Object compute(IEclipseContext context) {
		System.out.println("Creating a new service");
		ITodoModel service = ContextInjectionFactory.make(
				MyTodoModelImpl.class, context);
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(ITodoModel.class, service);
		return service;
	}
}