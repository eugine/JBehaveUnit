package com.eugenesokolov.jbehaveunit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class LaunchStoryHandler extends AbstractHandler {

	public LaunchStoryHandler() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Launch Story");
		return null;
	}

}