package com.shamrai.junit_test_auditor.config;

import com.shamrai.junit_test_auditor.TestInfoAggregator;
import com.shamrai.junit_test_auditor.parser.TestsParser;
import com.shamrai.junit_test_auditor.utils.FileWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class JunitTestAuditorConfiguration {

    @Bean
    public TestInfoAggregator testInfoAggregator(TestsParser testsParser,
                                                 @Value("/Users/oshamrai/super-tests") String projectPath) {
        return new TestInfoAggregator(testsParser, projectPath);
    }

    @Bean
    public TestsParser testsParser() {
        return new TestsParser();
    }

    @Bean
    public FileWriter fileWriter(@Value("${results.file}") String filePath) {
        return new FileWriter(filePath);
    }

}
