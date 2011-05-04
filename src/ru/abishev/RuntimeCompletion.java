package ru.abishev;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import ru.abishev.java.JavaCompletionProvider;
import ru.abishev.spec.ICompletionProvider;
import ru.abishev.utils.CompletionUtils;
import ru.abishev.utils.PsiUtils;

import java.util.List;

public class RuntimeCompletion extends CompletionContributor {
    private final Logger LOG = Logger.getInstance("ru.abishev.runtimecompetion");

    private final ICompletionProvider JAVA_PROVIDER = new JavaCompletionProvider();

    private Project currentProject;

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        currentProject = context.getProject();
    }


    @Override
    public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {
        try {
            PsiClassType type = PsiUtils.resolveCurrentVariablePsiClassType(parameters.getPosition());

            if (type == null) {
                return;
            }

            PsiClassType.ClassResolveResult classResolveResult = type.resolveGenerics();

            PsiClassType[] args = new PsiClassType[classResolveResult.getSubstitutor().getSubstitutionMap().size()];
            int i = 0;
            for (PsiType _type : classResolveResult.getSubstitutor().getSubstitutionMap().values()) {
                args[i++] = (PsiClassType) _type;
            }

            List<String> completions = JAVA_PROVIDER.getCompletions(currentProject, type, args);

            result.addAllElements(CompletionUtils.getCompletionsFromStrings(completions));
        } catch (Exception e) {
            LOG.warn("Error while runtime completion!", e);
        }
    }
}
