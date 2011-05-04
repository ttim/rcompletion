package ru.abishev.utils;

import com.intellij.codeInsight.lookup.LookupElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CompletionUtils {
    public static List<LookupElement> getCompletionsFromStrings(String... strings) {
        return getCompletionsFromStrings(newArrayList(strings));
    }

    public static List<LookupElement> getCompletionsFromStrings(List<String> strings) {
        List<LookupElement> result = newArrayList();
        for (String s : strings) {
            result.add(new StringLookupElement(s));
        }
        return result;
    }

    private static class StringLookupElement extends LookupElement {
        private final String s;

        StringLookupElement(String s) {
            this.s = s;
        }

        @NotNull
        @Override
        public String getLookupString() {
            return s;
        }
    }
}
