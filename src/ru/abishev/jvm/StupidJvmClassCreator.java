package ru.abishev.jvm;

import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Timur Abishev (timur@abishev.ru)
 */
public class StupidJvmClassCreator implements JvmClassCreator {
    private List<String> classLoaderList = null;
    private ClassLoader classLoader = null;

    private Class getPsiClassClazz(final PsiClass clazz, Project project) {
        try {
//            final CountDownLatch latch = new CountDownLatch(1);
//
//            CompilerManager compilerManager = CompilerManager.getInstance(currentProject);
//
//            compilerManager.make(compilerManager.createProjectCompileScope(currentProject), new CompileStatusNotification() {
//                public void finished(boolean b, int i, int i1, CompileContext compileContext) {
//                    latch.countDown();
//                }
//            });
//
//            latch.await();

            JavaParameters params = new JavaParameters();
            params.configureByProject(project, JavaParameters.CLASSES_ONLY, null);

//            System.out.println("! " + params.getClassPath().getPathList());

            return getClassLoader(params.getClassPath().getPathList()).loadClass(clazz.getQualifiedName());
        } catch (Exception e) {
            return null;
        }
    }


    private ClassLoader getClassLoader(List<String> paths) throws MalformedURLException {
        URL[] urls = new URL[paths.size()];
        int i = 0;
        for (String path : paths) {
            urls[i++] = new File(path).toURI().toURL();
        }

        if (!paths.equals(classLoaderList)) {
            classLoaderList = paths;
            classLoader = new UrlClassLoader(urls, this.getClass().getClassLoader());
        }

        return classLoader;
    }

    @Nullable
    public Object getObjectWithClassForPsiClassType(Project project, PsiClass clazz) {
        Class _clazz = getPsiClassClazz(clazz, project);
        try {
            return _clazz.newInstance();
        } catch (Error e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void reload() {
        classLoader = null;
        classLoaderList = null;
    }
}
