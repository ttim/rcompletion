package ru.abishev.spec;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public interface TypeResolver {
    PsiClassType resolveType(PsiElement position);
}
