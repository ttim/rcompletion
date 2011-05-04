package ru.abishev.spec;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;

import java.util.List;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public interface ICompletionProvider {
    public List<String> getCompletions(Project project, PsiClassType clazz, PsiClassType... generics);
}
