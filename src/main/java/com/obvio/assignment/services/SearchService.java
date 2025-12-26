package com.obvio.assignment.services;

import com.obvio.assignment.Repositories.FileDictionaryRepository;
import com.obvio.assignment.Repositories.WordStatsRepository;
import com.obvio.assignment.dto.SearchResponse;
import com.obvio.assignment.models.FileDictionary;
import com.obvio.assignment.models.WordStats;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final FileDictionaryRepository fileDictionaryRepository;
    private final WordStatsRepository wordStatsRepository;

    public List<SearchResponse> searchWord(String word) {
        List<SearchResponse> results = new ArrayList<>();
        List<FileDictionary> list = fileDictionaryRepository.findByWord(word);
        Integer totalDocumentsContainingWord = 0;
        Optional<WordStats> wordStats = wordStatsRepository.findByWord(word);
        if (wordStats.isPresent()) {
            totalDocumentsContainingWord = wordStats.get().getDocumentFrequency();
        }
        logger.info("Total documents containing the word {} :{}", word, totalDocumentsContainingWord);
        Integer overallTotalDocuments = fileDictionaryRepository.totalDocuments();
        logger.info("Total documents in the search engine: {}", overallTotalDocuments);
        for (FileDictionary fileDictionary : list) {
            results.add(new SearchResponse(fileDictionary.getFileName(), calculateRanking(fileDictionary, overallTotalDocuments, totalDocumentsContainingWord)));
        }

        results.sort(Comparator.comparing(SearchResponse::getRanking).reversed().thenComparing(SearchResponse::getDocumentName));
        if(results.size() >= 3){
            return results.subList(0, 3);
        }
        return results;
    }

    long calculateRanking(FileDictionary fileDictionary, Integer totalDocuments, Integer totalDocumentsContainingWord) {
        double tf = Double.valueOf(fileDictionary.getCount());
        double ndf = (double) totalDocuments / totalDocumentsContainingWord;
        double logNDF = Math.log10(ndf);
        double result = tf * logNDF;
        return Math.round(result*100);
    }
}
