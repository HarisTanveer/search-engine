package com.obvio.assignment.Repositories;

import com.obvio.assignment.models.WordStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordStatsRepository extends JpaRepository<WordStats, Long> {
    Optional<WordStats> findByWord(String word);
}
