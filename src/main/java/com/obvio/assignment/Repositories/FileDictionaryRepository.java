package com.obvio.assignment.Repositories;

import com.obvio.assignment.models.FileDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileDictionaryRepository extends JpaRepository<FileDictionary,Long> {

    List<FileDictionary> findByWord(String fileName);

    @Query("Select COUNT(DISTINCT fd.fileName) FROM FileDictionary fd")
    Integer totalDocuments();
}
