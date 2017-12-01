package stacksimp;

import java.util.ArrayList;
import java.util.List;

public class StackSimplierBuilder {
    public static StackSimplierBuilder builder() {
        return new StackSimplierBuilder();
    }

    private boolean useDefault = true;
    private boolean exSpring;
    private boolean exMybatis;
    private boolean exHibernate;
    private boolean exTomcat;
    private boolean exJunit;
    private boolean exJdbcDriver;
    private boolean inSunProxy;

    public StackSimplier build() {
        List<String> exPats = new ArrayList<>();
        if (exSpring) {
            exPats.add("^org.springframework");
            exPats.add("SpringCGLIB");
        }
        if (exMybatis) {
            exPats.add("^org.mybatis");
            exPats.add("^org.apache.ibatis");
        }
        if (exHibernate) {
            exPats.add("^org.hibernate");
        }
        if (exTomcat) {
            exPats.add("^org.apache.tomcat");
            exPats.add("^org.apache.catalina");
            exPats.add("^org.apache.coyote");
        }
        if (exJdbcDriver) {
            exPats.add("^oracle.jdbc.driver");
            exPats.add("^com.mysql.jdbc");
            exPats.add("^com.microsoft.sqlserver.jdbc");
        }
        if (exJunit) {
            exPats.add("^org.junit");
        }

        List<String> inPats = new ArrayList<>();
        if (inSunProxy) {
            inPats.add("^com.sun.proxy");
        }

        StackSimplier simplier = new StackSimplier();
        if (!useDefault) {
            simplier.setUseDefaultExcludeClassPattern(false);
        }
        simplier.setExcludeClassPatterns(exPats);
        if (!inPats.isEmpty()) {
            simplier.setIncludeClassPatterns(inPats);
        }
        return simplier;
    }

    public StackSimplierBuilder noUseDefault() {
        useDefault = false;
        return this;
    }

    public StackSimplierBuilder excludeSpring() {
        exSpring = true;
        return this;
    }

    public StackSimplierBuilder excludeMybatis() {
        exMybatis = true;
        return this;
    }

    public StackSimplierBuilder excludeHibernate() {
        exHibernate = true;
        return this;
    }

    public StackSimplierBuilder excludeTomcat() {
        exTomcat = true;
        return this;
    }
    public StackSimplierBuilder excludeJunit() {
        exJunit = true;
        return this;
    }

    public StackSimplierBuilder excludeJdbcDriver() {
        exJdbcDriver = true;
        return this;
    }

    public StackSimplierBuilder includeSunProxy() {
        inSunProxy = true;
        return this;
    }
}
