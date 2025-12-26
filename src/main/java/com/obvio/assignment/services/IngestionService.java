package com.obvio.assignment.services;

import lombok.extern.slf4j.Slf4j;
import com.obvio.assignment.Repositories.FileDictionaryRepository;
import com.obvio.assignment.Repositories.WordStatsRepository;
import com.obvio.assignment.models.FileDictionary;
import com.obvio.assignment.models.WordStats;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {
    private final FileDictionaryRepository fileDictionaryRepository;
    private final WordStatsRepository wordStatsRepository;

    @Transactional
    public void ParseInputFile(MultipartFile[] files) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if(fileDictionaryRepository.findFirstByFileName(fileName).isPresent()) {
                log.warn("File already exists: {}", fileName);
                throw new IllegalArgumentException("File already exists: " + fileName);
            }

            log.info("Processing file: {}, size: {} bytes", fileName, file.getSize());
            HashMap<String, Integer> wordCountsInFile = new HashMap<>();
            Set<String> uniqueWordsInFile = new HashSet<>();
            try {
                InputStream inputStream = file.getInputStream();
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .forEach(line -> {
                            line = line.replaceAll("\\p{Punct}+", "").toLowerCase();
                            String[] words = line.split(" ");
                            for (String word : words) {
                                if (!word.isEmpty()) {
                                    wordCountsInFile.put(word, wordCountsInFile.getOrDefault(word, 0) + 1);
                                    uniqueWordsInFile.add(word);
                                }
                            }
                        });
                saveDocumentMap(wordCountsInFile, fileName);
                updateWordStats(uniqueWordsInFile);
            } catch (IOException e) {
                log.error("Error reading file: {}", fileName, e);
                throw new IllegalArgumentException("Error reading file: " + fileName);
            }
        }
    }

    private void saveDocumentMap(HashMap<String, Integer> map, String fileName) {
        List<FileDictionary> fileDictionaryList = new ArrayList<>();
        for (Map.Entry<String, Integer> key : map.entrySet()) {
            fileDictionaryList.add(FileDictionary.builder()
                    .fileName(fileName)
                    .word(key.getKey())
                    .count(key.getValue())
                    .build()
            );
        }
        fileDictionaryRepository.saveAll(fileDictionaryList);
    }

    private void updateWordStats(Set<String> uniqueWordsInFile) {
        for (String word : uniqueWordsInFile) {
            Optional<WordStats> existingWordStats = wordStatsRepository.findByWord(word);
            if (existingWordStats.isPresent()) {
                WordStats wordStats = existingWordStats.get();
                wordStats.setDocumentFrequency(wordStats.getDocumentFrequency() + 1);
                wordStatsRepository.save(wordStats);
            } else {
                WordStats newWordStats = WordStats.builder()
                        .word(word)
                        .documentFrequency(1)
                        .build();
                wordStatsRepository.save(newWordStats);
            }
        }
    }
}
