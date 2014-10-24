package com.eugenesokolov.jbehaveunit.utils;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

public interface IEclipseHelper {

	public IWorkbenchWindow getActiveWorkbenchWindow();

	public IWorkbenchPage getActivePage();

	public boolean openFile(IWorkbenchPage page, IPath path);

	public boolean openTypeFile(IWorkbenchWindow window, IWorkbenchPage page,
			String initialPattern);

	public boolean openResourceFile(IWorkbenchWindow window,
			IWorkbenchPage page, String initialPattern);

}