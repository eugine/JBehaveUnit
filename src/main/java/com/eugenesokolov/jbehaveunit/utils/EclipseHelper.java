package com.eugenesokolov.jbehaveunit.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.ide.IDE;

public class EclipseHelper {

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	public static IWorkbenchPage getActivePage() {
		return getActiveWorkbenchWindow().getActivePage().getActivePart().getSite().getPage();
	}

	public static boolean openFile(IWorkbenchPage page, IPath path) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean openTypeFile(IWorkbenchWindow window, IWorkbenchPage page, String initialPattern) {
		Shell shell = window.getShell();
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(shell),
					SearchEngine.createWorkspaceScope(), IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false, initialPattern);
		} catch (JavaModelException e) {
			MessageDialog.openInformation(shell, "JBehave Unit", "Ops.. can't open dialog:\n" + e.toString());
			return false;
		}

		dialog.setTitle("Open type");
		if (dialog.open() != IDialogConstants.CANCEL_ID) {
			Object[] types = dialog.getResult();
			if (types != null && types.length > 0 && types[0] != null) {
				return openFile(page, ((SourceType) types[0]).getPath());
			}
		}
		return false;
	}

	public static boolean openResourceFile(IWorkbenchWindow window, IWorkbenchPage page, String initialPattern) {
		Shell shell = window.getShell();
		FilteredResourcesSelectionDialog dialog = null;
		dialog = new FilteredResourcesSelectionDialog(shell, false, ResourcesPlugin.getWorkspace().getRoot(), 
						IResource.FILE | IResource.FOLDER | IResource.PROJECT);
		dialog.setTitle("Open Resource");
		if (initialPattern != null) {
			dialog.setInitialPattern(initialPattern);
		}
		if (dialog.open() != IDialogConstants.CANCEL_ID) {
			Object[] result = dialog.getResult();
			if (result != null && result.length > 0 && result[0] != null) {
				IResource file = (IResource) result[0];
				return openFile(page, file.getFullPath());
			}
		}
		return false;
	}

}
