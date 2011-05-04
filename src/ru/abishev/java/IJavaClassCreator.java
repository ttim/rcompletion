package ru.abishev.java;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public interface IJavaClassCreator {
    @Nullable
    public Object getObjectWithClassForPsiClassType(Project project, PsiClass clazz);
    public void reload();
}
