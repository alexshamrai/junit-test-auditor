package com.shamrai.junit_test_auditor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTestsInfo {
    private String serviceName;
    private List<TestInfo> tests;
    private long totalTests;
    private long disabledTests;
    private double disabledPercentage;

    @Override
    public String toString() {
        return serviceName + ";" +
                testsToString() + ";" +
                totalTests + ";" +
                disabledTests + ";" +
                disabledPercentage;
    }

    private String testsToString() {
        var testsToString = tests.stream()
                .map(TestInfo::shortenToString)
                .collect(Collectors.joining(","));
        return "[" + testsToString + "]";
    }

}

