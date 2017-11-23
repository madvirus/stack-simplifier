# Usage

## Maven Repository

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.madvirus</groupId>
    <artifactId>stack-simplifier</artifactId>
    <version>0.0.2</version>
</dependency>
```

## StackSimplier

```java
StackSimplier simp = new StackSimplier();
simp.setExcludeClassPatterns(new String[] { 
        "org.junit", "org.tomcat", "org.eclipse", "org.springframework", 
        "org.apache.tomcat", "oracle.jdbc", "SpringCGLIB" });
String msg = simplifier.simplify(ex);
```

The trace message that StackSimplier#simplify method creates contains 

* stack trace of which class name doesn't matches exclusion class pattern
* but includes first stack trace whether it match or not

### default exclusion class patterns(regex)

* ^sun|^java|^com.sun

### patterns of setExcludeClassPatterns

setExcludeClassPatterns:
* specifiy additional exclusion class name pattern
* replace dot notation by \\.

### sample

```java
@Test
public void simplify() throws Exception {
    StackSimplier simplier = new StackSimplier();
    simplier.setExcludeClassPatterns(new String[] {"org.junit", "com.intellij"});
    try {
        URL url = new URL("http://bad.server.xx");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.getResponseCode();
    } catch(Exception ex) {
        ex.printStackTrace(System.out); // (1)
        System.out.println(simplier.simplify(ex)); // (2)
    }
}
```

raw exception trace(1):

```
java.net.UnknownHostException: bad.server.xx
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:184)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:589)
	at java.net.Socket.connect(Socket.java:538)
	at sun.net.NetworkClient.doConnect(NetworkClient.java:180)
	at sun.net.www.http.HttpClient.openServer(HttpClient.java:463)
	at sun.net.www.http.HttpClient.openServer(HttpClient.java:558)
	at sun.net.www.http.HttpClient.<init>(HttpClient.java:242)
	at sun.net.www.http.HttpClient.New(HttpClient.java:339)
	at sun.net.www.http.HttpClient.New(HttpClient.java:357)
	at sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1202)
	at sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1138)
	at sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1032)
	at sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:966)
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1546)
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1474)
	at java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:480)
	at stacksimp.StackSimplierTest.simplify(StackSimplierTest.java:17)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	...(skip, very very long message)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)

```

simplified exception trace(2):
```
java.net.UnknownHostException: bad.server.xx
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:184)
	at stacksimp.StackSimplierTest.simplify(StackSimplierTest.java:17)
```