package com.example.e4.rcp.todo.handlers;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectiveSwitchHandler {

	public void switchPerspective(MPerspective activePerspective,
			MApplication app, EPartService partService,
			EModelService modelService) {
		List<MPerspective> perspectives = modelService.findElements(app, null, MPerspective.class, null);
		
		for (MPerspective perspective : perspectives) {
			partService.switchPerspective(perspective);
		}
	}

}
