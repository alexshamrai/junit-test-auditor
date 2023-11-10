package com.shamrai.junit_test_auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JunitTestAuditorApplication implements CommandLineRunner {

    @Autowired
    private TestInfoAggregator testInfoAggregator;

    public static void main(String[] args) {
        SpringApplication.run(JunitTestAuditorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var tests = testInfoAggregator.getDisabledTestsWithOwners();
        System.out.println(tests);
    }
}
