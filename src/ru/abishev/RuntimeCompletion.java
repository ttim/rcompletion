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
import ru.abishev.groovy.GroovyCompletionProvider;
import ru.abishev.groovy.GroovyTypeResolver;
import ru.abishev.java.JavaTypeResolver;
import ru.abishev.jvm.JavaCompletionProvider;
import ru.abishev.spec.ICompletionProvider;
import ru.abishev.spec.TypeResolver;
import ru.abishev.utils.CompletionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeCompletion extends CompletionContributor {
    private final Logger LOG = Logger.getInstance("ru.abishev.runtimecompetion");

    private static final Map<String, TypeResolver> TYPE_RESOLVERS = new HashMap<String, TypeResolver>() {{
        put("JAVA", new JavaTypeResolver());
        put("Groovy", new GroovyTypeResolver());
    }};

    private static final Map<String, ICompletionProvider> COMPLETION_PROVIDERS = new HashMap<String, ICompletionProvider>() {{
        put("JAVA", new JavaCompletionProvider());
        put("Groovy", new GroovyCompletionProvider());
    }};

    private Project currentProject;

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        currentProject = context.getProject();
    }

    @Override
    public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {
        try {
            String languageId = parameters.getOriginalFile().getLanguage().getID();
            TypeResolver typeResolver = TYPE_RESOLVERS.get(languageId);

            if (typeResolver == null) {
                return;
            }

            PsiClassType type = typeResolver.resolveType(parameters.getPosition());

            if (type == null) {
                return;
            }

            PsiClassType.ClassResolveResult classResolveResult = type.resolveGenerics();

            PsiClassType[] args = new PsiClassType[classResolveResult.getSubstitutor().getSubstitutionMap().size()];
            int i = 0;
            for (PsiType _type : classResolveResult.getSubstitutor().getSubstitutionMap().values()) {
                args[i++] = (PsiClassType) _type;
            }

            ICompletionProvider completionProvider = COMPLETION_PROVIDERS.get(languageId);
            if (completionProvider == null) {
                return;
            }

            List<String> completions = completionProvider.getCompletions(currentProject, type, args);

            result.addAllElements(CompletionUtils.getCompletionsFromStrings(completions));
        } catch (Exception e) {
            LOG.warn("Error while runtime completion!", e);
        }
    }
}
