package com.practice.service;

import com.practice.model.Job;
import com.practice.model.JobStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobQueueTest {

    private JobQueue queue;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        queue = new JobQueue();
        now = LocalDateTime.of(2024, 6, 1, 10, 0, 0);
    }

    private Job makeJob(String id, int priority, int maxAttempts) {
        return new Job(id, "PROCESS", priority, "{}", maxAttempts, now);
    }

    @Test
    void shouldReturnJob_whenEnqueuedAndPolled() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        Job polled = queue.poll();
        assertNotNull(polled);
        assertEquals("JOB-001", polled.getJobId());
    }

    @Test
    void shouldReturnHigherPriorityFirst_whenTwoJobsEnqueued() {
        Job low = makeJob("JOB-LOW", 1, 3);
        Job high = makeJob("JOB-HIGH", 10, 3);
        queue.enqueue(low);
        queue.enqueue(high);
        Job polled = queue.poll();
        assertEquals("JOB-HIGH", polled.getJobId());
    }

    @Test
    void shouldReturnInFifoOrder_whenSamePriority() {
        Job first = makeJob("JOB-FIRST", 5, 3);
        Job second = makeJob("JOB-SECOND", 5, 3);
        queue.enqueue(first);
        queue.enqueue(second);
        Job polled = queue.poll();
        assertEquals("JOB-FIRST", polled.getJobId());
    }

    @Test
    void shouldReturnNull_whenQueueIsEmpty() {
        Job result = queue.poll();
        assertNull(result);
    }

    @Test
    void shouldMarkJobRunning_whenPolled() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        Job polled = queue.poll();
        assertEquals(JobStatus.RUNNING, polled.getStatus());
    }

    @Test
    void shouldMarkJobCompleted_whenCompleteCalledSuccessfully() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        queue.poll();
        queue.complete("JOB-001", "Done");
        assertEquals(JobStatus.COMPLETED, job.getStatus());
    }

    @Test
    void shouldRequeueJob_whenFailedBelowMaxAttempts() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        queue.poll();
        queue.fail("JOB-001", "Timeout");
        assertEquals(JobStatus.QUEUED, job.getStatus());
        assertEquals(1, job.getAttemptCount());
    }

    @Test
    void shouldMarkJobDead_whenFailedAtMaxAttempts() {
        Job job = makeJob("JOB-001", 5, 1);
        queue.enqueue(job);
        queue.poll();
        queue.fail("JOB-001", "Persistent error");
        assertEquals(JobStatus.DEAD, job.getStatus());
    }

    @Test
    void shouldReturnCorrectJobs_whenGetByStatusCalled() {
        Job j1 = makeJob("JOB-001", 5, 3);
        Job j2 = makeJob("JOB-002", 3, 3);
        queue.enqueue(j1);
        queue.enqueue(j2);
        queue.poll();
        List<Job> running = queue.getByStatus(JobStatus.RUNNING);
        assertEquals(1, running.size());
        assertEquals("JOB-001", running.get(0).getJobId());
    }

    @Test
    void shouldReturnAllJobsInOrder_whenMultipleJobsEnqueued() {
        Job j1 = makeJob("JOB-001", 1, 3);
        Job j2 = makeJob("JOB-002", 5, 3);
        Job j3 = makeJob("JOB-003", 10, 3);
        queue.enqueue(j1);
        queue.enqueue(j2);
        queue.enqueue(j3);
        assertEquals("JOB-003", queue.poll().getJobId());
        assertEquals("JOB-002", queue.poll().getJobId());
        assertEquals("JOB-001", queue.poll().getJobId());
    }

    @Test
    void shouldRequeueWithCorrectPriority_whenJobRequeued() {
        Job j1 = makeJob("JOB-001", 5, 3);
        Job j2 = makeJob("JOB-002", 10, 3);
        queue.enqueue(j1);
        queue.enqueue(j2);
        queue.poll();
        queue.fail("JOB-002", "Error");
        Job next = queue.poll();
        assertEquals("JOB-002", next.getJobId());
    }

    @Test
    void shouldNotRequeue_whenJobIsDeadAfterMaxAttempts() {
        Job job = makeJob("JOB-001", 5, 1);
        queue.enqueue(job);
        queue.poll();
        queue.fail("JOB-001", "Fatal error");
        Job next = queue.poll();
        assertNull(next);
    }

    @Test
    void shouldStoreErrorMessage_whenJobFails() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        queue.poll();
        queue.fail("JOB-001", "Connection refused");
        assertEquals("Connection refused", job.getErrorMessage());
    }

    @Test
    void shouldReturnEmptyList_whenNoJobsMatchStatus() {
        Job job = makeJob("JOB-001", 5, 3);
        queue.enqueue(job);
        List<Job> completed = queue.getByStatus(JobStatus.COMPLETED);
        assertTrue(completed.isEmpty());
    }
}
