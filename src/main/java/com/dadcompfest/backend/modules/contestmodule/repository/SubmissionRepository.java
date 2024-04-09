package com.dadcompfest.backend.modules.contestmodule.repository;

import com.dadcompfest.backend.modules.contestmodule.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository  extends JpaRepository<Submission, Long> {
}
