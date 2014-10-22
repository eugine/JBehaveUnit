package com.eugenesokolov.jbehaveunit.handlers;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jdt.internal.core.search.JavaSearchScope;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.packageview.GotoResourceAction;
import org.eclipse.jdt.internal.ui.packageview.PackagesMessages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;

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
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage()
				.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file == null)
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		String fileName = file.getName();
		if (isGroovyFile(fileName)) {

		} else if (isStoryFile(fileName)) {
			switchToGroovyFile(
					HandlerUtil.getActiveWorkbenchWindowChecked(event),
					toGroovyName(fileName));
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

	private void switchToGroovyFile(IWorkbenchWindow window, String fileName) {

		Shell shell = window.getShell();
		SelectionDialog dialog = null;
		try {
			dialog = JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(
					shell), SearchEngine.createWorkspaceScope(),
					IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false,
					fileName);
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
