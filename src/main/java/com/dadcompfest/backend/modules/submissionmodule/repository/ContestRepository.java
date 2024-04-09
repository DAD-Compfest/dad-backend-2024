package com.dadcompfest.backend.modules.submissionmodule.repository;

import com.dadcompfest.backend.modules.submissionmodule.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
}
