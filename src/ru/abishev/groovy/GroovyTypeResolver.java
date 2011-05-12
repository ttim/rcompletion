package ru.abishev.groovy;

import com.intellij.psi.*;
import ru.abishev.spec.TypeResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public class GroovyTypeResolver implements TypeResolver {
    private static <T> T dynamicInvokeWithoutArgs(Object obj, String methodName) {
        try {
            return (T) obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public PsiClassType resolveType(PsiElement position) {
        PsiElement qualifiedExpression = dynamicInvokeWithoutArgs(position.getParent(), "getQualifierExpression");
        PsiType type = dynamicInvokeWithoutArgs(qualifiedExpression, "getType");

        if (!(type instanceof PsiClassType)) {
            return null;
        }

        return (PsiClassType) type;
    }
}
