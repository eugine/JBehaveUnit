package com.eugenesokolov.jbehaveunit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import static com.eugenesokolov.jbehaveunit.utils.EclipseHelper.*;
import static com.eugenesokolov.jbehaveunit.utils.FileNameHelper.*;

public class JumpToHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		IWorkbenchPage page = getActivePage();

		IFile file = (IFile) page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file != null) {
			String fileName = file.getName();
			if (isGroovyFile(fileName)) {
				openResourceFile(window, page, toStoryName(fileName));
			} else if (isStoryFile(fileName)) {
				openTypeFile(window, page, toGroovyName(fileName));
			}
		}
		return null;
	}
}
