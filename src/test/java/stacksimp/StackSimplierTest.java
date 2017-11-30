package stacksimp;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class StackSimplierTest {
    private final String[] patterns = {"org.junit", "com.intellij"};

    @Test
    public void simplify() throws Exception {
        StackSimplier simplier = new StackSimplier();
        simplier.setExcludeClassPatterns(patterns);
        try {
            URL url = new URL("http://bad.server.xx");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.getResponseCode();
        } catch(Exception ex) {
            String simplified = simplier.simplify(ex);
            System.out.println(simplified);
            assertSimplifiedStackTrace(simplified);
        }
    }

    private void assertSimplifiedStackTrace(String simplified) {
        String[] traceLines = lines(simplified);
        int stackTraceLineNum = -1;

        for (String line : traceLines) {
            if (!line.startsWith("\tat")) {
                stackTraceLineNum = 0;
            } else {
                stackTraceLineNum++;
            }
            if (stackTraceLineNum > 1) {
                assertStackTractContainsNoExcludeClassPattern(line);
            }
        }
    }

    private String[] lines(String str) {
        return str.split("\n");
    }

    private void assertStackTractContainsNoExcludeClassPattern(String line) {
        for (String pattern : patterns) {
            if (pattern.startsWith("^")) {
                assertFalse(line.startsWith("\tat " + pattern.substring(1)),line + " dont't startWith " + pattern);
            }
        }
    }
}
