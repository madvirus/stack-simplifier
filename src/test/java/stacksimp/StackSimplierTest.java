package stacksimp;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class StackSimplierTest {
    @Test
    public void simplify() throws Exception {
        StackSimplier simplier = new StackSimplier();
        simplier.setExcludeClassPatterns(new String[] {"org.junit", "com.intellij"});
        try {
            URL url = new URL("http://bad.server.xx");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.getResponseCode();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
            System.out.println("------");
            String simplified = simplier.simplify(ex);
            System.out.println(simplified);
            assertNoContains(simplified, "\tat org.junit");
        }
    }

    private void assertNoContains(String simplified, String str) {
        assertFalse(simplified.contains(str));
    }

}
