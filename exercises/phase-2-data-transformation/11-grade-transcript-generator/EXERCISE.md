# Exercise 11 — Student Grade Transcript Generator

## Your Task
Implement the `generate()` method inside:
```
src/main/java/com/practice/service/TranscriptGenerator.java
```
Do not modify any other file.

---

## Scenario
Given a student's ID, name, and a list of module results, generate a formal academic transcript that converts scores to letter grades, computes a weighted GPA, determines the degree classification, and counts credits passed.

---

## The Models (already built for you)

**`ModuleResult`** — raw result for one module
| Field | Type | Description |
|---|---|---|
| moduleCode | String | Module identifier (e.g. "CS101") |
| moduleName | String | Human-readable module name |
| credits | int | Credit weighting of the module |
| scorePercent | double | Numeric score (0–100) |
| semester | int | Semester number (1, 2, …) |

**`TranscriptEntry`** — enriched result for one module
| Field | Type | Description |
|---|---|---|
| moduleCode | String | Copied from ModuleResult |
| moduleName | String | Copied from ModuleResult |
| credits | int | Copied from ModuleResult |
| scorePercent | double | Copied from ModuleResult |
| grade | String | Letter grade (A/B/C/D/F) |
| gradePoints | double | Numeric grade points |

**`Transcript`** — the full academic transcript
| Field | Type | Description |
|---|---|---|
| studentId | String | Student identifier |
| studentName | String | Student full name |
| entries | List\<TranscriptEntry\> | All entries, sorted by semester then moduleCode |
| weightedGPA | double | Weighted average grade points, rounded to 2dp |
| classification | String | Degree classification |
| totalCreditsAttempted | int | Sum of all module credits |
| totalCreditsPassed | int | Sum of credits for modules where grade ≠ F |

---

## Method to Implement
`Transcript generate(String studentId, String studentName, List<ModuleResult> results)`

---

## Grading Scale
| Score | Grade | Grade Points |
|---|---|---|
| ≥ 70 | A | 4.0 |
| ≥ 60 | B | 3.0 |
| ≥ 50 | C | 2.0 |
| ≥ 40 | D | 1.0 |
| < 40 | F | 0.0 |

## Degree Classification
| Weighted GPA | Classification |
|---|---|
| ≥ 3.7 | First |
| ≥ 3.0 | 2:1 |
| ≥ 2.0 | 2:2 |
| ≥ 1.0 | Third |
| < 1.0 | Fail |

---

## Criteria
1. Each `ModuleResult` maps to exactly one `TranscriptEntry` with the correct grade and grade points.
2. `weightedGPA` = sum(gradePoints × credits) / totalCreditsAttempted, rounded to 2 decimal places.
3. `classification` is determined from the rounded `weightedGPA`.
4. `totalCreditsPassed` counts only modules where the grade is not F.
5. Entries are sorted by semester ascending, then by moduleCode ascending within the same semester.

---

## Running the Tests
```bash
mvn test
```
There are 12 tests. They all fail until your implementation is correct.
You are done when all 12 pass.

---

## Hint (read only if stuck)
<details>
<summary>Click to reveal</summary>
Map each ModuleResult to a TranscriptEntry by checking score thresholds in order (≥70 → A, ≥60 → B, etc.). For GPA, use a stream to sum gradePoints×credits, then divide by totalCreditsAttempted. Use Math.round(gpa * 100.0) / 100.0 or a similar approach to round to 2dp. Sort the entries with Comparator.comparingInt(TranscriptEntry::getSemester).thenComparing(TranscriptEntry::getModuleCode).
</details>
