package com.shamrai.junit_test_auditor;

import com.shamrai.junit_test_auditor.model.TestInfo;
import com.shamrai.junit_test_auditor.parser.TestsParser;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TestInfoAggregator {

    private final TestsParser testParser;
    private final String projectPath;

    public List<TestInfo> getDisabledTestsWithOwners() {

        File projectDir = new File(projectPath);
        return testParser.listMethodCalls(projectDir);
    }
}
