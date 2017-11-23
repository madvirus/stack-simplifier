# How to use

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

## Use

```java
StackSimplier simp = new StackSimplier();
simp.setExcludeClassPatterns(new String[] { 
        "org.junit", "org.tomcat", "org.eclipse", "org.springframework", 
        "org.apache.tomcat", "oracle.jdbc", "SpringCGLIB" });
String msg = simplifier.simplify(ex);
```
