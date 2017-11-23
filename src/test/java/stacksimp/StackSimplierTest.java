package stacksimp;

import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
            System.out.println(simplier.simplify(ex));
        }
    }

}
