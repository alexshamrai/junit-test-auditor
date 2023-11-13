package com.shamrai.junit_test_auditor.model;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class TestInfo {
    private String testClass;
    private String testMethod;
    private String filePath;
    private Set<String> tags;
    private boolean isDisabled;

    @Override
    public String toString() {
        return testClass + ';' +
                testMethod + ';' +
                filePath + ';' +
                tags + ';' +
                isDisabled;
    }

    public String shortenToString() {
        String shortenedPath = filePath.endsWith(".java") ? filePath.substring(0, filePath.length() - 5) : filePath;
        return shortenedPath + '.' + testMethod + "()";
    }
}

