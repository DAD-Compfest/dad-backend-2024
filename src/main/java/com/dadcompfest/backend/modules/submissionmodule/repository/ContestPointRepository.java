package com.dadcompfest.backend.modules.submissionmodule.repository;

import com.dadcompfest.backend.modules.submissionmodule.model.ContestPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestPointRepository extends JpaRepository<ContestPoint, Long> {
}
