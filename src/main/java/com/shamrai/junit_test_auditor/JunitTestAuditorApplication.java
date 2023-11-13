package com.shamrai.junit_test_auditor;

import com.shamrai.junit_test_auditor.utils.FileWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.shamrai.junit_test_auditor.utils.TestInfoAnalyser.buildServiceTestsInfo;
import static com.shamrai.junit_test_auditor.utils.TestInfoAnalyser.calculateDisabledTests;

@SpringBootApplication
public class JunitTestAuditorApplication implements CommandLineRunner {

    @Autowired
    private TestInfoAggregator testInfoAggregator;

    @Autowired
    private FileWriter fileWriter;

    public static void main(String[] args) {
        SpringApplication.run(JunitTestAuditorApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        var tests = testInfoAggregator.getDisabledTestsWithOwners();
        fileWriter.writeTestInfo(tests);
        System.out.println(calculateDisabledTests(tests));
        var serviceTests = buildServiceTestsInfo(tests);
        fileWriter.writeServiceTestsInfo(serviceTests);
    }
}
