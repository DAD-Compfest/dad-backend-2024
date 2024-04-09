package com.dadcompfest.backend.modules.submissionmodule.enums;

public enum SubmissionStatus {
    TRUE("TRUE"),
    FALSE("FALSE"),
    OTHER("OTHER");

    private final String status;

    SubmissionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
