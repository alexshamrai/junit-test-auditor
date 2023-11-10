package com.shamrai.junit_test_auditor.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.shamrai.junit_test_auditor.model.TestInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.substringBetween;

@Slf4j
public class TestsParser {

    private static final String TESTS_PATH = "src/test/java";
    private static final String TESTS_CLASS_MARKER = "extends BaseTest";
    private static final String DISABLED = "@Disabled";
    private static final String TEST = "@Test";
    private static final String TAG = "@Tag";

    public List<TestInfo> listMethodCalls(File projectDir) {

        List<TestInfo> testsInfo = new ArrayList<>();

        new DirExplorer((level, path, file) -> path.endsWith(".java") && path.contains(TESTS_PATH), (level, path, file) -> {

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration testMethod, Object arg) {
                        super.visit(testMethod, arg);
                        log.info("Processing {}:", path);
                        if (!hasTestAnnotation(testMethod)) {
                            return;
                        }

                        var testClass = (ClassOrInterfaceDeclaration) testMethod.getParentNode().get();
                        var tags = getTags(testClass, testMethod);
                        var disabledTag = getDisabledTag(testMethod);
                        var test = TestInfo.builder()
                                .filePath(path)
                                .testClass(testClass.getNameAsString())
                                .testMethod(testMethod.getName().asString())
                                .tags(tags)
                                .isDisabled(!disabledTag.isEmpty())
                                .build();

                        testsInfo.add(test);
                    }
                }.visit(StaticJavaParser.parse(file), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).explore(projectDir);
        return testsInfo;
    }

    private String getDisabledTag(MethodDeclaration testMethod) {
        var classDisabledTag = getClassDisabledTag(getClassAnnotations(testMethod));
        if (classDisabledTag.isPresent()) {
            return classDisabledTag.get();
        }
        var testMethodChildNodes = getChildNodes(testMethod);
        return testMethodChildNodes.stream()
                .filter(node -> node.contains(DISABLED))
                .findAny()
                .orElse("");
    }

    private List<String> getClassAnnotations(MethodDeclaration testMethod) {
        var tokenRange = testMethod.getParentNode().flatMap(Node::getTokenRange).get();
        var codeLines = tokenRange.toString().split("\n");
        var beforeClassLines = new ArrayList<String>();
        for (String line : codeLines) {
            if (line.trim().contains(TESTS_CLASS_MARKER)) {
                break;
            }
            beforeClassLines.add(line);
        }
        return beforeClassLines;
    }

    private boolean hasTestAnnotation(MethodDeclaration testMethod) {
        var testMethodChildNodes = getChildNodes(testMethod);
        return testMethodChildNodes.stream()
                .anyMatch(node -> node.contains(TEST));
    }

    private Optional<String> getClassDisabledTag(List<String> classAnnotations) {
        return classAnnotations.stream()
                .filter(annotation -> annotation.startsWith(DISABLED))
                .findAny();
    }

    private Set<String> getTags(ClassOrInterfaceDeclaration testClass, MethodDeclaration testMethod) {
        return Stream.concat(extractTags(testClass).stream(), extractTags(testMethod).stream())
                .collect(Collectors.toSet());
    }

    private Set<String> extractTags(Node node) {
        return getChildNodes(node).stream()
                .filter(s -> s.contains(TAG))
                .map(s -> substringBetween(s, "(", ")").trim())
                .collect(Collectors.toSet());
    }

    private List<String> getChildNodes(Node node) {
        return node.getChildNodes().stream().map(Node::toString).toList();
    }
}
