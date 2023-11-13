package com.shamrai.junit_test_auditor.utils;

import com.shamrai.junit_test_auditor.model.ServiceTestsInfo;
import com.shamrai.junit_test_auditor.model.TestInfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlReportGenerator {

    public String generateReport(List<TestInfo> testInfos, List<ServiceTestsInfo> serviceTestsInfos, String stats) {
        var report = new StringBuilder();
        addHtmlHeader(report);
        addReportContent(report, stats);
        addDisabledTestsSection(report, testInfos);
        addLink(report, "./auditResults.csv", "All tests");
        addServicesSection(report, serviceTestsInfos, "Services with more than 30% disabled", 30, true);
        addServicesSection(report, serviceTestsInfos, "Services with more than 9 tests", 9, false);
        addLink(report, "./serviceTests.csv", "All services covered by tests");
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

    private void addServicesSection(StringBuilder report, List<ServiceTestsInfo> serviceTestsInfos, String title, int testThreshold, boolean sortByTotalTests) {
        report.append("<button class=\"collapsible\" onclick=\"toggleCollapsible(event)\">").append(title).append("</button>")
                .append("<div class=\"content\"><table border=\"1\">")
                .append(getServiceTestsInfoTableRows(serviceTestsInfos, testThreshold, sortByTotalTests))
                .append("</table></div>");
    }

    private String getServiceTestsInfoTableRows(List<ServiceTestsInfo> serviceTestsInfos, int testThreshold, boolean sortByTotalTests) {
        var rows = new StringBuilder();
        serviceTestsInfos.stream()
                .filter(service -> (sortByTotalTests ? service.getTotalTests() : service.getDisabledPercentage()) > testThreshold)
                .sorted((s1, s2) -> sortByTotalTests ? Long.compare(s2.getTotalTests(), s1.getTotalTests()) : Double.compare(s2.getDisabledPercentage(), s1.getDisabledPercentage()))
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

    public void writeToFile(String report, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(report);
        }
    }
}
