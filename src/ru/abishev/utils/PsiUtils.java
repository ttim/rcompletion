package ru.abishev.utils;

import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public class PsiUtils {
    @Nullable
    public static PsiClassType resolveCurrentVariablePsiClassType(PsiElement position) {
//        if (!(position instanceof PsiIdentifier)) {
//            return null;
//        }
//
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
