package com.obvio.assignment.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String word;
    private Integer count;

    @Builder
    FileDictionary(String fileName, String word, Integer count) {
        this.fileName = fileName;
        this.word = word;
        this.count = count;
    }
}
