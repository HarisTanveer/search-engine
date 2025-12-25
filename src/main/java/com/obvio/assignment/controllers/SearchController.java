package com.obvio.assignment.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/search")
public class SearchController {

    @PostMapping("")
    public ResponseEntity<String> ingest(){

        return ResponseEntity.ok("Hello World");
    }
}