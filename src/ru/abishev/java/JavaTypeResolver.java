package ru.abishev.java;

import com.intellij.psi.*;
import ru.abishev.spec.TypeResolver;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public class JavaTypeResolver implements TypeResolver {
    public PsiClassType resolveType(PsiElement position) {
        if (!(position.getParent() instanceof PsiReferenceExpression)) {
            return null;
        }

        PsiReferenceExpression reference = (PsiReferenceExpression) position.getParent();

        PsiExpression qualifierExpression = reference.getQualifierExpression();

        if (qualifierExpression == null) {
            return null;
        }

        PsiType type = qualifierExpression.getType();

        if (!(type instanceof PsiClassType)) {
            return null;
        }

        return (PsiClassType) type;
    }
}
