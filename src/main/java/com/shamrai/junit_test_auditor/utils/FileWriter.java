package com.shamrai.junit_test_auditor.utils;

import com.shamrai.junit_test_auditor.model.TestInfo;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class FileWriter {

    private static final String HEADER = "Class;Method;path;tags;isDisabled";
    private final String filePath;

    public void writeNotificationsInfo(List<TestInfo> notifications) throws IOException {
        File file = new File(filePath);
        PrintWriter writer = new PrintWriter(file);
        writer.println(HEADER);
        for (TestInfo sample : notifications) {
            writer.println(sample.toString());
        }
        writer.close();
    }
}

