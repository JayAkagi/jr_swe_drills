package com.practice.service;

import com.practice.model.Job;
import com.practice.model.JobStatus;

import java.util.List;

public class JobQueue {

    public void enqueue(Job job) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Job poll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void complete(String jobId, String output) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void fail(String jobId, String errorMessage) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Job> getByStatus(JobStatus status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
