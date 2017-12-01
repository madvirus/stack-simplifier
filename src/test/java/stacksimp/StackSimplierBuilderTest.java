package stacksimp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackSimplierBuilderTest {
    @Test
    void basicBuild() {
        StackSimplier simp = StackSimplierBuilder.builder().build();
        assertTrue(simp.isUseDefaultExcludeClassPattern());
    }

    @Test
    void noUseDefault() {
        StackSimplier simp = StackSimplierBuilder.builder().noUseDefault().build();
        assertFalse(simp.isUseDefaultExcludeClassPattern());
    }

    @Test
    void excludeSpring() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeSpring().build();
        assertContains(simp.getExcludePatterns(), "^org.springframework", "SpringCGLIB");
    }

    @Test
    void excludeMybatis() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeMybatis().build();
        assertContains(simp.getExcludePatterns(), "^org.mybatis", "^org.apache.ibatis");
    }

    @Test
    void excludeHibernate() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeHibernate().build();
        assertContains(simp.getExcludePatterns(), "^org.hibernate");
    }

    @Test
    void excludeTomcat() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeTomcat().build();
        assertContains(simp.getExcludePatterns(), "^org.apache.tomcat", "^org.apache.catalina", "^org.apache.coyote");
    }

    @Test
    void excludeJunit() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeJunit().build();
        assertContains(simp.getExcludePatterns(), "^org.junit");
    }

    @Test
    void excludeJdbcDriver() {
        StackSimplier simp = StackSimplierBuilder.builder().excludeJdbcDriver().build();
        assertContains(simp.getExcludePatterns(), "^oracle.jdbc.driver");
    }

    @Test
    void includeJunit() {
        StackSimplier simp = StackSimplierBuilder.builder().includeSunProxy().build();
        assertContains(simp.getIncludePatterns(), "^com.sun.proxy");
    }

    @Test
    void composite() {
        StackSimplier simp = StackSimplierBuilder.builder()
                .noUseDefault()
                .excludeSpring()
                .excludeMybatis()
                .excludeHibernate()
                .excludeTomcat()
                .excludeJdbcDriver()
                .excludeJunit()
                .includeSunProxy()
                .build();

        assertContains(simp.getExcludePatterns(),
                "^org.springframework", "SpringCGLIB",
                "^org.mybatis", "^org.apache.ibatis",
                "^org.hibernate",
                "^org.apache.tomcat", "^org.apache.catalina", "^org.apache.coyote",
                "^oracle.jdbc.driver", "^com.mysql.jdbc", "^com.microsoft.sqlserver.jdbc",
                "^org.junit");
    }

    private void assertContains(List<String> excludePatterns, String... patterns) {
        for (String pat : patterns) {
            assertTrue(excludePatterns.contains(pat), "must contains " + pat);
        }
    }

}
