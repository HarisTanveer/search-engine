package com.obvio.assignment.controllers;

import com.obvio.assignment.dto.SearchResponse;
import com.obvio.assignment.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<List<SearchResponse>> ingest(@RequestParam String query) {
        List<SearchResponse> list = searchService.searchWord(query.toLowerCase());
        return ResponseEntity.ok(list);
    }
}