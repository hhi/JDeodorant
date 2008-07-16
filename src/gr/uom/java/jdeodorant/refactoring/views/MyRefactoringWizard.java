package gr.uom.java.jdeodorant.refactoring.views;

import gr.uom.java.jdeodorant.refactoring.manipulators.ReplaceConditionalWithPolymorphism;
import gr.uom.java.jdeodorant.refactoring.manipulators.ReplaceTypeCodeWithStateStrategy;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ui.PartInitException;

public class MyRefactoringWizard extends RefactoringWizard {
	
	private Refactoring refactoring;
	private Action action;
	
	public MyRefactoringWizard(Refactoring refactoring, Action action) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
		setDefaultPageTitle(refactoring.getName());
		this.refactoring = refactoring;
		this.action = action;
	}

	@Override
	protected void addUserInputPages() {
	}
	
	@Override
	public boolean performFinish() {
		boolean finish = super.performFinish();
		action.setEnabled(false);
		Set<IJavaElement> javaElementsToOpenInEditor = new LinkedHashSet<IJavaElement>();
		if(refactoring instanceof ReplaceTypeCodeWithStateStrategy) {
			javaElementsToOpenInEditor.addAll(((ReplaceTypeCodeWithStateStrategy)refactoring).getJavaElementsToOpenInEditor());
		}
		else if(refactoring instanceof ReplaceConditionalWithPolymorphism) {
			javaElementsToOpenInEditor.addAll(((ReplaceConditionalWithPolymorphism)refactoring).getJavaElementsToOpenInEditor());
		}
		for(IJavaElement javaElement : javaElementsToOpenInEditor) {
			try {
				JavaUI.openInEditor(javaElement);
			} catch (PartInitException e) {
				e.printStackTrace();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return finish;
	}
}
