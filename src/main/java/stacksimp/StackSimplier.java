package stacksimp;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StackSimplier {
	private static Pattern DEFAULT_PATTERN = Pattern.compile("^sun|^java|^com.sun");
	
	private Pattern excludeClassPattern;
	private boolean useDefaultExcludeClassPattern = true;

	public String simplify(Throwable exception) {
		StringBuilder buff = new StringBuilder();
		buff.append(exception.getClass().getName()).append(": ").append(exception.getMessage()).append("\n");
		StackTraceElement[] traces = exception.getStackTrace();
		filterAndAppend(buff, traces);
		
		if (exception.getCause() != null) {
			Throwable cause = exception.getCause();
			buff.append("Caused by: ").append(cause.getClass().getName()).append(": ").append(cause.getMessage()).append("\n");
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
		if (matchExcludeClass(stackTrace.getClassName())) {
			return false;
		}
		return true;
	}

	private boolean matchExcludeClass(String className) {
		if (useDefaultExcludeClassPattern && DEFAULT_PATTERN.matcher(className).find()) return true;
		if (excludeClassPattern != null && excludeClassPattern.matcher(className).find()) return true;
		return false;
	}

	public void setExcludeClassPatterns(String[] patterns) {
		String regex = Arrays.stream(patterns)
				.map(pat -> pat.replace(".", "\\."))
				.collect(Collectors.joining("|"));
		excludeClassPattern = Pattern.compile(regex);
	}

	public void setUseDefaultExcludeClassPattern(boolean useDefaultExcludeClassPattern) {
		this.useDefaultExcludeClassPattern = useDefaultExcludeClassPattern;
	}
}