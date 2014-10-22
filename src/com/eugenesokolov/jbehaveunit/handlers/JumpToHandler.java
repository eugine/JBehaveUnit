package com.eugenesokolov.jbehaveunit.handlers;

import java.io.FileNotFoundException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.packageview.PackagesMessages;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
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

	/**
	 * The constructor.
	 */
	public JumpToHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage().getActivePart().getSite().getPage();
		
		IFile file = (IFile) page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file != null) {
			String fileName = file.getName();
			if (isGroovyFile(fileName)) {

			} else if (isStoryFile(fileName)) {
				switchToGroovyFile(window, page, toGroovyName(fileName));
			}
		}
		return null;
	}

	private String toGroovyName(String storyFileName) {
		String name = storyFileName.substring(0,
				storyFileName.length() - STORY.length());

		String[] words = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(capitalizeWord(word.toLowerCase()));
		}
		return sb.toString();
	}

	private void switchToGroovyFile(IWorkbenchWindow window, IWorkbenchPage page, String fileName) {
		Shell shell = window.getShell();
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(
					shell), SearchEngine.createWorkspaceScope(),
					IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false);
		} catch (JavaModelException e) {
			MessageDialog.openInformation(shell, "JBehave Unit",
					"Ops.. can't open dialog:\n" + e.toString());
			return;
		}
		String files[] = new String[1];
		files[0] = fileName;

		dialog.setTitle("Open type");
		dialog.setMessage(PackagesMessages.GotoType_dialog_message);

		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}
		
		Object[] types= dialog.getResult();
		if (types != null && types.length > 0) {
			if (types[0] != null) {
				IPath path = ((org.eclipse.jdt.internal.core.SourceType) types[0]).getPath();
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
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
