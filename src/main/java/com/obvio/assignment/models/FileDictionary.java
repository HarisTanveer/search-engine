package com.obvio.assignment.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_dictionary")
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
