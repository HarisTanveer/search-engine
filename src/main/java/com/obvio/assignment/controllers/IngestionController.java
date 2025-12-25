package com.obvio.assignment.controllers;

import com.obvio.assignment.services.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class IngestionController{
    private final IngestionService ingestionService;

    @PostMapping("")
    public ResponseEntity<String> ingest(@RequestBody() MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new ResponseEntity<>("No files selected", HttpStatus.BAD_REQUEST);
        }

        try {
            ingestionService.ParseInputFile(files);
            return new ResponseEntity<>("Successfully uploaded " + files.length + " files", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading files: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}