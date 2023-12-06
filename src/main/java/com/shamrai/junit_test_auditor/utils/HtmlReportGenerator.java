package com.shamrai.junit_test_auditor.utils;

import com.shamrai.junit_test_auditor.model.ServiceTestsInfo;
import com.shamrai.junit_test_auditor.model.TestInfo;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.shamrai.junit_test_auditor.utils.CsvFileWriter.AUDIT_RESULTS_FILE;
import static com.shamrai.junit_test_auditor.utils.CsvFileWriter.SERVICE_TESTS_FILE;

@RequiredArgsConstructor
public class HtmlReportGenerator {

    private final String workingDirectory;

    public String generateReport(List<TestInfo> testInfos, List<ServiceTestsInfo> serviceTestsInfos, String stats) {
        var report = new StringBuilder();
        addHtmlHeader(report);
        addReportContent(report, stats);
        addDisabledTestsSection(report, testInfos);
        addLink(report, "./" + AUDIT_RESULTS_FILE, "All tests");
        addServicesSection(report, getServiceTestsInfoTableRowsFilteredByDisabledPercentage(serviceTestsInfos, 30), "Services with more than 30% disabled");
        addServicesSection(report, getServiceTestsInfoTableRowsFilteredByTotalTests(serviceTestsInfos, 9), "Services with more than 9 tests");
        addLink(report, "./" + SERVICE_TESTS_FILE, "All services covered by tests");
        report.append("</body></html>");
        return report.toString();
    }

    private void addHtmlHeader(StringBuilder report) {
        report.append("<html><head>")
                .append("<style>")
                .append(".collapsible { background-color: #777; color: white; cursor: pointer; padding: 18px; width: 100%; border: none; text-align: left; outline: none; font-size: 15px; }")
                .append(".active, .collapsible:hover { background-color: #555; }")
                .append(".content { padding: 0 18px; display: none; overflow: hidden; background-color: #f1f1f1; }")
                .append(".scrollable { overflow-x: auto; }")
                .append("</style>")
                .append("<script>")
                .append("function toggleCollapsible(event) {")
                .append("var content = event.currentTarget.nextElementSibling;")
                .append("content.style.display = content.style.display === 'block' ? 'none' : 'block'; }")
                .append("</script>")
                .append("</head><body>");
    }

    private void addReportContent(StringBuilder report, String stats) {
        report.append("<h1>Current state of project tests</h1>")
                .append("<p>").append(stats).append("</p>");
    }

    private void addDisabledTestsSection(StringBuilder report, List<TestInfo> testInfos) {
        report.append("<button class=\"collapsible\" onclick=\"toggleCollapsible(event)\">Disabled tests</button>")
                .append("<div class=\"content\"><div class=\"scrollable\"><table border=\"1\">")
                .append("<tr><th>Class</th><th>Method</th><th>Path</th><th>Tags</th><th>Is Disabled</th></tr>")
                .append(getTestInfoTableRows(testInfos))
                .append("</table></div></div>");
    }

    private String getTestInfoTableRows(List<TestInfo> testInfos) {
        var rows = new StringBuilder();
        for (TestInfo test : testInfos) {
            if (test.isDisabled()) {
                rows.append("<tr><td>")
                        .append(test.getTestClass()).append("</td><td>")
                        .append(test.getTestMethod()).append("</td><td>")
                        .append(test.getFilePath()).append("</td><td>")
                        .append(test.getTags()).append("</td><td>")
                        .append("Yes").append("</td></tr>");
            }
        }
        return rows.toString();
    }

    private void addServicesSection(StringBuilder report, String tableRows, String title) {
        report.append("<button class=\"collapsible\" onclick=\"toggleCollapsible(event)\">").append(title).append("</button>")
                .append("<div class=\"content\"><table border=\"1\">")
                .append("<tr><th>Service</th><th>Total Tests</th><th>Disabled Tests</th><th>Disabled tests percentage</th></tr>")
                .append(tableRows)
                .append("</table></div>");
    }

    private String getServiceTestsInfoTableRowsFilteredByDisabledPercentage(List<ServiceTestsInfo> serviceTestsInfos, double percentageThreshold) {
        var rows = new StringBuilder();
        serviceTestsInfos.stream()
                .filter(service -> service.getDisabledPercentage() > percentageThreshold)
                .sorted(Comparator.comparingDouble(ServiceTestsInfo::getDisabledPercentage).reversed())
                .forEach(service -> rows.append("<tr><td>")
                        .append(service.getServiceName()).append("</td><td>")
                        .append(service.getTotalTests()).append("</td><td>")
                        .append(service.getDisabledTests()).append("</td><td>")
                        .append(String.format("%.2f%%", service.getDisabledPercentage()))
                        .append("</td></tr>"));
        return rows.toString();
    }

    private String getServiceTestsInfoTableRowsFilteredByTotalTests(List<ServiceTestsInfo> serviceTestsInfos, int testThreshold) {
        var rows = new StringBuilder();
        serviceTestsInfos.stream()
                .filter(service -> service.getTotalTests() > testThreshold)
                .sorted(Comparator.comparingLong(ServiceTestsInfo::getTotalTests).reversed())
                .forEach(service -> rows.append("<tr><td>")
                        .append(service.getServiceName()).append("</td><td>")
                        .append(service.getTotalTests()).append("</td><td>")
                        .append(service.getDisabledTests()).append("</td><td>")
                        .append(String.format("%.2f%%", service.getDisabledPercentage()))
                        .append("</td></tr>"));
        return rows.toString();
    }

    private void addLink(StringBuilder report, String href, String text) {
        report.append("<p><a href=\"").append(href).append("\">").append(text).append("</a></p>");
    }

    public void writeToFile(String report) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(workingDirectory + "/" + "report.html"))) {
            writer.write(report);
        }
    }
}