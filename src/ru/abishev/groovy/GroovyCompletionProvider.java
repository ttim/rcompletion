package ru.abishev.groovy;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import ru.abishev.jvm.JvmClassCreator;
import ru.abishev.jvm.StupidJvmClassCreator;
import ru.abishev.spec.ICompletionProvider;

import java.lang.reflect.Method;
import java.util.List;

import static ru.abishev.utils.CollectionUtils.newArrayList;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public class GroovyCompletionProvider implements ICompletionProvider {
    private final JvmClassCreator JVM_CLASS_CREATOR = new StupidJvmClassCreator();

    public List<String> getCompletions(Project project, PsiClassType clazz, PsiClassType... generics) {
        List<String> completions = newArrayList();

        Object completionObject = JVM_CLASS_CREATOR.getObjectWithClassForPsiClassType(project, clazz.resolve());
        Object[] genericObjects = new Object[generics.length];
        Class[] genericObjectClasses = new Class[generics.length];

        for (int i = 0; i < genericObjects.length; i++) {
            genericObjects[i] = JVM_CLASS_CREATOR.getObjectWithClassForPsiClassType(project, generics[i].resolve());
            genericObjectClasses[i] = genericObjects[i].getClass();
        }

        if (completionObject != null) {
            try {
                Method dynamicInvocationMethod = completionObject.getClass().getMethod("invokeMethod", String.class, Object.class);
                completions = (List<String>) dynamicInvocationMethod.invoke(completionObject, "getCompletionList", genericObjects);
            } catch (Exception e) {
                // todo: ?
//                System.out.println(e);
            }
        }

        JVM_CLASS_CREATOR.reload(); // todo: only when make!

        return completions;
    }
}
