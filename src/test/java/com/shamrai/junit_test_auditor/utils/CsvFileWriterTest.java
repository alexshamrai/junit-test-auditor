package com.shamrai.junit_test_auditor.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


class CsvFileWriterTest {

    private final CsvFileWriter csvFileWriter = new CsvFileWriter("target/");


    @Test
    void writeTestInfoTest() throws IOException {
        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
        Path mockPath = fileSystem.getPath("/test/");
        Files.createDirectories(mockPath);

        CsvFileWriter writer = new CsvFileWriter(mockPath.toString());

        List<TestInfo> testInfos = Arrays.asList(
                new TestInfo(/* parameters */),
                new TestInfo(/* parameters */)
        );

        writer.writeTestInfo(testInfos);

        Path writtenFilePath = mockPath.resolve("auditResults.csv");
        List<String> lines = Files.readAllLines(writtenFilePath);

        assertEquals("Class|Method|path|tags|isDisabled", lines.get(0));
        // Add more assertions for each line of expected output
    }

    @Test
    void setResponsibleUsers() {
        var provider = new JenkinsResponsibleUsersProvider(properties, stagelessTestsFrontendClient);
        var gateKeepers = Sets.newHashSet("user1", "user2");

        assertThatCode(() -> provider.setResponsibleUsers(gateKeepers)).doesNotThrowAnyException();
    }

    @Test
    void writeServiceTestsInfo() {
    }
}