package com.eugenesokolov.jbehaveunit.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
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

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class JumpToHandler extends AbstractHandler {
	private static final String STORY = ".story";
	private static final String GROOVY = ".groovy";

	public JumpToHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage().getActivePart().getSite().getPage();

		IFile file = (IFile) page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file != null) {
			String fileName = file.getName();
			if (isGroovyFile(fileName)) {
				switchToStoryFile(window, page, toStoryName(fileName));
			} else if (isStoryFile(fileName)) {
				switchToGroovyFile(window, page, toGroovyName(fileName));
			}
		}
		return null;
	}
	
	private String[] splitByCamelCase(String camelCaseString) {
		if (camelCaseString == null) {
			return null;
		}
		if (camelCaseString.isEmpty()) {
			return new String[0];
		}
		final char[] c = camelCaseString.toCharArray();
		final List<String> list = new ArrayList<String>();
		int tokenStart = 0;
		int currentType = Character.getType(c[tokenStart]);
		for (int pos = tokenStart + 1; pos < c.length; pos++) {
			final int type = Character.getType(c[pos]);
			if (type == currentType) {
				continue;
			}
			if (type == Character.LOWERCASE_LETTER && currentType == Character.UPPERCASE_LETTER) {
				final int newTokenStart = pos - 1;
				if (newTokenStart != tokenStart) {
					list.add(new String(c, tokenStart, newTokenStart - tokenStart));
					tokenStart = newTokenStart;
				}
			} 
			currentType = type;
		}
		list.add(new String(c, tokenStart, c.length - tokenStart));
		return list.toArray(new String[list.size()]);
	}

	private String toStoryName(String groovyFileName) {
		String name = groovyFileName.substring(0, groovyFileName.length() - GROOVY.length());
		
		String [] words = splitByCamelCase(name);
		
		String loopDelim = "";
		
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(loopDelim);
			sb.append(word);
			
			loopDelim = "_";
		}
		
		return sb.toString();
	}

	private String toGroovyName(String storyFileName) {
		String name = storyFileName.substring(0, storyFileName.length() - STORY.length());

		String[] words = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(capitalizeWord(word.toLowerCase()));
		}
		return sb.toString();
	}

	private void switchToStoryFile(IWorkbenchWindow window, IWorkbenchPage page, String storyName) {
		Shell shell = window.getShell();
		FilteredResourcesSelectionDialog dialog = null;
		dialog = new FilteredResourcesSelectionDialog(shell, false, ResourcesPlugin.getWorkspace().getRoot(), 
				IResource.FILE | IResource.FOLDER | IResource.PROJECT);
		dialog.setTitle("Open Resource");
		dialog.setInitialPattern(storyName);
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}
		Object[] result = dialog.getResult();
		for (int i = 0; i < result.length; i++) {
			IResource file = (IResource) result[i];
			openFile(page, file.getFullPath());
		}
	}

	private void switchToGroovyFile(IWorkbenchWindow window, IWorkbenchPage page, String groovyName) {
		Shell shell = window.getShell();
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createTypeDialog(shell, 
					new ProgressMonitorDialog(shell), 
					SearchEngine.createWorkspaceScope(), 
					IJavaElementSearchConstants.CONSIDER_ALL_TYPES, 
					false, groovyName);
		} catch (JavaModelException e) {
			MessageDialog.openInformation(shell, "JBehave Unit",
					"Ops.. can't open dialog:\n" + e.toString());
			return;
		}
		String files[] = new String[1];
		files[0] = groovyName;

		dialog.setTitle("Open type");

		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}

		Object[] types = dialog.getResult();
		if (types != null && types.length > 0) {
			if (types[0] != null) {
				openFile(page, ((org.eclipse.jdt.internal.core.SourceType) types[0]).getPath());
			}
		}
	}

	private void openFile(IWorkbenchPage page, IPath path) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		try {
			IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private boolean isGroovyFile(String fileName) {
		return fileName.endsWith(GROOVY);
	}

	private boolean isStoryFile(String fileName) {
		return fileName.endsWith(STORY);
	}

	private String capitalizeWord(String word) {
		if (word == null) {
			return "";
		}
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}
}
