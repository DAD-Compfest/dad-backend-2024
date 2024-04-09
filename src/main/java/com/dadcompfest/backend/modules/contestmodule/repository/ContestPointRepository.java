package com.dadcompfest.backend.modules.contestmodule.repository;

import com.dadcompfest.backend.modules.contestmodule.model.ContestPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface ContestPointRepository extends JpaRepository<ContestPoint, Long> {
}
