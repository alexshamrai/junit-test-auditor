package com.shamrai.junit_test_auditor.utils;

import com.shamrai.junit_test_auditor.model.ServiceTestsInfo;
import com.shamrai.junit_test_auditor.model.TestInfo;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestInfoAnalyser {

    public static String calculateDisabledTests(List<TestInfo> tests) {
        long totalTests = tests.size();
        long disabledTests = tests.stream()
                .filter(TestInfo::isDisabled)
                .count();
        double disabledPercentage = totalTests > 0 ? (double) disabledTests / totalTests * 100 : 0;

        return String.format("Total tests: %d, Disabled tests: %d, Percentage of disabled tests: %.2f%%",
                totalTests, disabledTests, disabledPercentage);
    }

    public static List<ServiceTestsInfo> buildServiceTestsInfo(List<TestInfo> testInfos) {
        var testsByService = groupTestsByService(testInfos);
        return populateServiceTestInfoWithData(testsByService);
    }

    private static List<ServiceTestsInfo> populateServiceTestInfoWithData(Map<String, List<TestInfo>> testsByService) {
        return testsByService.entrySet().stream()
                .map(entry -> createServiceTestsInfo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private static Map<String, List<TestInfo>> groupTestsByService(List<TestInfo> testInfos) {
        return testInfos.stream()
                .flatMap(testInfo -> testInfo.getTags().stream()
                        .map(tag -> new AbstractMap.SimpleEntry<>(tag, testInfo)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    private static ServiceTestsInfo createServiceTestsInfo(String serviceName, List<TestInfo> tests) {
        long totalTests = tests.size();
        long disabledTests = tests.stream()
                .filter(TestInfo::isDisabled)
                .count();
        double disabledPercentage = totalTests > 0 ? (double) disabledTests / totalTests * 100 : 0;

        return new ServiceTestsInfo(serviceName, tests, totalTests, disabledTests, disabledPercentage);
    }
}
