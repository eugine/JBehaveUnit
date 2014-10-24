package com.eugenesokolov.jbehaveunit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.eugenesokolov.jbehaveunit.utils.EclipseHelper;
import com.eugenesokolov.jbehaveunit.utils.FileNameHelper;
import com.eugenesokolov.jbehaveunit.utils.IEclipseHelper;
import com.eugenesokolov.jbehaveunit.utils.IFileNameHelper;

public class JumpToHandler extends AbstractHandler {
	
	private IFileNameHelper fileNameHelper;
	private IEclipseHelper eclipseHelper;
	
	public JumpToHandler() {
		setFileNameHelper(new FileNameHelper());
		setEclipseHelper(new EclipseHelper());
	}
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = eclipseHelper.getActiveWorkbenchWindow();
		IWorkbenchPage page = eclipseHelper.getActivePage();

		IFile file = (IFile) page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file != null) {
			String fileName = file.getName();
			if (fileNameHelper.isGroovyFile(fileName)) {
				eclipseHelper.openResourceFile(window, page, fileNameHelper.toStoryName(fileName));
			} else if (fileNameHelper.isStoryFile(fileName)) {
				eclipseHelper.openTypeFile(window, page, fileNameHelper.toGroovyName(fileName));
			}
		}
		return null;
	}

	public void setFileNameHelper(IFileNameHelper fileNameHelper) {
		this.fileNameHelper = fileNameHelper;
	}

	public void setEclipseHelper(IEclipseHelper eclipseHelper) {
		this.eclipseHelper = eclipseHelper;
	}
}
