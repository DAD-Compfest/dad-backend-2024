package com.dadcompfest.backend.modules.submissionmodule.repository;

import com.dadcompfest.backend.modules.submissionmodule.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository  extends JpaRepository<Submission, Long> {
}
