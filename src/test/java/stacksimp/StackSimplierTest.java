package stacksimp;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackSimplierTest {

    @Test
    public void when_Use_DefaultExclude_And_AdditonalExclude() throws Exception {
        String[] patterns = {"org.junit", "com.intellij"};
        StackSimplier simplier = new StackSimplier();
        simplier.setExcludeClassPatterns(patterns);
        assertSimplifier(simplier,
                new String[]{"org.junit", "com.intellij", "^sun", "^java", "^com.sun"},
                new String[0]);
    }

    @Test
    public void when_DoNot_UseDefaultExclude_And_Use_AdditronalExclude() throws Exception {
        String[] patterns = {"org.junit", "com.intellij"};
        StackSimplier simplier = new StackSimplier();
        simplier.setUseDefaultExcludeClassPattern(false);
        simplier.setExcludeClassPatterns(patterns);
        assertSimplifier(simplier,
                new String[]{"org.junit", "com.intellij"},
                new String[]{"^sun", "^java"});
    }

    @Test
    public void when_Use_AdditronalExclude_And_Use_Include() throws Exception {
        String[] patterns = {"org", "com.intellij"};
        StackSimplier simplier = new StackSimplier();
        simplier.setUseDefaultExcludeClassPattern(false);
        simplier.setExcludeClassPatterns(patterns);
        simplier.setIncludeClassPatterns("org.junit");
        assertSimplifier(simplier,
                new String[]{"org", "com.intellij"},
                new String[]{"org.junit"});
    }

    private void assertSimplifier(StackSimplier simplier, String[] excludePatterns, String[] includePatterns) {
        try {
            URL url = new URL("http://bad.server.xx");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.getResponseCode();
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String simplified = simplier.simplify(ex);
            assertSimplifiedStackTrace(sw.toString(), simplified, excludePatterns, includePatterns);
        }
    }

    private void assertSimplifiedStackTrace(String full, String simplified, String[] excludePatterns, String[] includePatterns) {
        Set<String> mustHaveLines = Arrays.stream(lines(full))
                .filter(s -> s.startsWith("\tat "))
                .filter(s -> matchPatterns(s, includePatterns))
                .collect(Collectors.toSet());

        String[] traceLines = lines(simplified);
        int stackTraceLineNum = -1;
        for (String line : traceLines) {
            if (!line.startsWith("\tat")) {
                stackTraceLineNum = 0;
            } else {
                stackTraceLineNum++;
            }
            if (stackTraceLineNum > 1) {
                boolean excludeMatch = matchPatterns(line, excludePatterns);
                boolean includeMatch = matchPatterns(line, includePatterns);
                assertFalse(excludeMatch && !includeMatch,
                        line + " do not have to contains exlucePatterns");
            }
        }

        for (String mustHaveLine : mustHaveLines) {
            assertTrue(Arrays.stream(traceLines).anyMatch(line -> line.trim().equals(mustHaveLine.trim())),
                    String.format("simplified result must contains %s", mustHaveLine));
        }
    }

    private String[] lines(String str) {
        return str.split("\n");
    }

    private boolean matchPatterns(String line, String[] patterns) {
        for (String pattern : patterns) {
            if (pattern.startsWith("^")) {
                if (line.startsWith("\tat " + pattern.substring(1))) return true;
            } else {
                if (line.contains(pattern)) return true;
            }
        }
        return false;
    }
}
