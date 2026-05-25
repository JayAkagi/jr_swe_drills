package com.practice.service;

import com.practice.model.ModuleResult;
import com.practice.model.Transcript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptGeneratorTest {

    private TranscriptGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new TranscriptGenerator();
    }

    private ModuleResult module(String code, String name, int credits, double score, int semester) {
        return new ModuleResult(code, name, credits, score, semester);
    }

    @Test
    void shouldSetStudentId_whenGenerating() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 75.0, 1)));
        assertEquals("S001", transcript.getStudentId());
    }

    @Test
    void shouldSetStudentName_whenGenerating() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 75.0, 1)));
        assertEquals("Alice", transcript.getStudentName());
    }

    @Test
    void shouldAssignGradeA_whenScoreAtLeast70() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 70.0, 1)));
        assertEquals("A", transcript.getEntries().get(0).getGrade());
        assertEquals(4.0, transcript.getEntries().get(0).getGradePoints());
    }

    @Test
    void shouldAssignGradeB_whenScoreAtLeast60() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 65.0, 1)));
        assertEquals("B", transcript.getEntries().get(0).getGrade());
        assertEquals(3.0, transcript.getEntries().get(0).getGradePoints());
    }

    @Test
    void shouldAssignGradeC_whenScoreAtLeast50() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 55.0, 1)));
        assertEquals("C", transcript.getEntries().get(0).getGrade());
        assertEquals(2.0, transcript.getEntries().get(0).getGradePoints());
    }

    @Test
    void shouldAssignGradeD_whenScoreAtLeast40() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 45.0, 1)));
        assertEquals("D", transcript.getEntries().get(0).getGrade());
        assertEquals(1.0, transcript.getEntries().get(0).getGradePoints());
    }

    @Test
    void shouldAssignGradeF_whenScoreBelow40() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 35.0, 1)));
        assertEquals("F", transcript.getEntries().get(0).getGrade());
        assertEquals(0.0, transcript.getEntries().get(0).getGradePoints());
    }

    @Test
    void shouldComputeWeightedGPA_withDifferentCreditWeights() {
        List<ModuleResult> results = List.of(
            module("CS101", "Intro", 20, 75.0, 1),
            module("CS102", "Advanced", 10, 55.0, 1)
        );
        Transcript transcript = generator.generate("S001", "Alice", results);
        double expectedGPA = (4.0 * 20 + 2.0 * 10) / 30.0;
        assertEquals(Math.round(expectedGPA * 100.0) / 100.0, transcript.getWeightedGPA(), 0.005);
    }

    @Test
    void shouldClassifyAsFirst_whenGPAAtLeast3Point7() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 75.0, 1)));
        assertEquals("First", transcript.getClassification());
    }

    @Test
    void shouldClassifyAsTwoOne_whenGPAAtLeast3Point0() {
        Transcript transcript = generator.generate("S001", "Alice", List.of(module("CS101", "Intro", 10, 65.0, 1)));
        assertEquals("2:1", transcript.getClassification());
    }

    @Test
    void shouldExcludeFailedCredits_fromTotalCreditsPassed() {
        List<ModuleResult> results = List.of(
            module("CS101", "Intro", 10, 75.0, 1),
            module("CS102", "Advanced", 15, 30.0, 1)
        );
        Transcript transcript = generator.generate("S001", "Alice", results);
        assertEquals(25, transcript.getTotalCreditsAttempted());
        assertEquals(10, transcript.getTotalCreditsPassed());
    }

    @Test
    void shouldSortEntries_bySemesterThenModuleCode() {
        List<ModuleResult> results = List.of(
            module("CS201", "Data", 10, 70.0, 2),
            module("CS101", "Intro", 10, 70.0, 1),
            module("CS102", "Logic", 10, 70.0, 1)
        );
        Transcript transcript = generator.generate("S001", "Alice", results);
        List<String> codes = transcript.getEntries().stream()
                .map(e -> e.getModuleCode()).toList();
        assertEquals(List.of("CS101", "CS102", "CS201"), codes);
    }
}
