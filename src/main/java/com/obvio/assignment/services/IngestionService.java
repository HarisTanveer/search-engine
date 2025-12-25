package com.obvio.assignment.services;

import com.obvio.assignment.AssignmentApplication;
import com.obvio.assignment.Repositories.FileDictionaryRepository;
import com.obvio.assignment.models.FileDictionary;
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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class IngestionService {
    private final Logger logger = Logger.getLogger(AssignmentApplication.class.getName());
    private final FileDictionaryRepository fileDictionaryRepository;

    @Transactional
    public void ParseInputFile(MultipartFile[] files) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();

            if(fileDictionaryRepository.findFirstByFileName(fileName).isPresent()) {
                logger.log(Level.WARNING, "File already exists: " + fileName);
                throw new IllegalArgumentException("File already exists: " + fileName);
            }

            logger.info("Processing file: " + fileName + ", size: " + file.getSize() + " bytes");
            HashMap<String, Integer> map = new HashMap<>();
            try {
                InputStream inputStream = file.getInputStream();
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .forEach(line -> addToDocumentMap(fileName, line, map));
                saveDocumentMap(map, fileName);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error reading file: " + fileName, e);
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

    private void addToDocumentMap(String fileName, String lineContent, HashMap<String, Integer> map) {
        logger.info(fileName + ": " + lineContent);
        lineContent = lineContent.replaceAll("\\p{Punct}+", "").toLowerCase();
        String[] words = lineContent.split(" ");
        for (String word : words) {
            if(!map.containsKey(word)) {
                map.put(word, 1);
            } else {
                map.put(word, map.get(word) + 1);
            }
        }
    }
}
