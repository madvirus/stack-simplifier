package stacksimp;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StackSimplier {
    private boolean useDefaultExcludeClassPattern = true;
    private static ClassNamePatternMatcher DEFAULT_EXCLUDE_MATCHER = new ClassNamePatternMatcher("^sun", "^java", "^com.sun");
    private ClassNamePatternMatcher excludeMatcher;
    private ClassNamePatternMatcher includeMatcher;

    public String simplify(Throwable exception) {
        StringBuilder buff = new StringBuilder();
        buff.append(exception.getClass().getName()).append(": ").append(exception.getMessage()).append(System.lineSeparator());
        StackTraceElement[] traces = exception.getStackTrace();
        filterAndAppend(buff, traces);

        if (exception.getCause() != null) {
            Throwable cause = exception.getCause();
            buff.append("Caused by: ").append(cause.getClass().getName()).append(": ").append(cause.getMessage()).append(System.lineSeparator());
            filterAndAppend(buff, cause.getStackTrace());
        }
        return buff.toString();
    }

    private void filterAndAppend(StringBuilder buff, StackTraceElement[] traces) {
        boolean firstIncluded = false;
        for (StackTraceElement stackTrace : traces) {
            if (!firstIncluded) {
                firstIncluded = true;
                buff.append("\tat ").append(stackTrace).append("\n");
            } else if (macthIncludeTrace(stackTrace)) {
                buff.append("\tat ").append(stackTrace).append("\n");
            }
        }
    }

    private boolean macthIncludeTrace(StackTraceElement stackTrace) {
        if (matchIncludeClass(stackTrace.getClassName())) {
            return true;
        }
        if (matchExcludeClass(stackTrace.getClassName())) {
            return false;
        }
        return true;
    }

    private boolean matchIncludeClass(String className) {
        return includeMatcher != null && includeMatcher.match(className);
    }

    private boolean matchExcludeClass(String className) {
        if (useDefaultExcludeClassPattern && DEFAULT_EXCLUDE_MATCHER.match(className)) return true;
        if (excludeMatcher != null && excludeMatcher.match(className)) return true;
        return false;
    }

    public void setUseDefaultExcludeClassPattern(boolean useDefaultExcludeClassPattern) {
        this.useDefaultExcludeClassPattern = useDefaultExcludeClassPattern;
    }

    public void setExcludeClassPatterns(String... patterns) {
        excludeMatcher = new ClassNamePatternMatcher(patterns);
    }

    public boolean isUseDefaultExcludeClassPattern() {
        return useDefaultExcludeClassPattern;
    }

    public void setIncludeClassPatterns(String... patterns) {
        includeMatcher = new ClassNamePatternMatcher(patterns);
    }

    private static class ClassNamePatternMatcher {
        private List<String> patterns;
        private Pattern pattern;

        public ClassNamePatternMatcher(String... patterns) {
            this.patterns = Arrays.asList(patterns);
            String regex = Arrays.stream(patterns)
                    .map(pat -> pat.replace(".", "\\."))
                    .collect(Collectors.joining("|"));
            pattern = Pattern.compile(regex);
        }

        boolean match(String className) {
            return pattern.matcher(className).find();
        }
    }
}