package com.dadcompfest.backend.modules.contestmodule.repository;

import com.dadcompfest.backend.modules.contestmodule.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
}
