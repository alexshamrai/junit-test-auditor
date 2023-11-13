package com.shamrai.junit_test_auditor.utils;

import com.shamrai.junit_test_auditor.model.ServiceTestsInfo;
import com.shamrai.junit_test_auditor.model.TestInfo;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class CsvFileWriter {

    private static final String TEST_INFO_HEADER = "Class|Method|path|tags|isDisabled";
    private static final String SERVICE_TESTS_INFO_HEADER = "ServiceName|Tests|TotalTests|DisabledTests|DisabledPercentage";

    private final String filePath;

    public void writeTestInfo(List<TestInfo> testInfos) throws IOException {
        writeToFile(filePath + "auditResults.csv", TEST_INFO_HEADER, testInfos, TestInfo::toString);
    }

    public void writeServiceTestsInfo(List<ServiceTestsInfo> serviceTestsInfos) throws IOException {
        writeToFile(filePath + "serviceTests.csv", SERVICE_TESTS_INFO_HEADER, serviceTestsInfos, ServiceTestsInfo::toString);
    }

    private <T> void writeToFile(String file, String header, List<T> data, Function<T, String> mapper) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(header);
            data.forEach(item -> writer.println(mapper.apply(item)));
        }
    }
}
