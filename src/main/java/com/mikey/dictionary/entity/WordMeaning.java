package com.mikey.dictionary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "word_meanings")
@Getter
@Setter
@NoArgsConstructor
public class WordMeaning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;


    @Column(name = "part_of_speech", nullable = false, length = 50)
    private String partOfSpeech;

    @Column(name = "definition", nullable = false, columnDefinition = "TEXT")
    private String definition;

    @Column(name = "example", columnDefinition = "TEXT")
    private String example;

    @Column(name = "synonyms", length = 500)
    private String synonyms;

    @Column(name = "antonyms", length = 500)
    private String antonyms;

    public WordMeaning(String partOfSpeech, String definition, String example) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
    }

    public WordMeaning( String partOfSpeech, String definition, String example, String synonyms, String antonyms) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }

    public void addWord(Word word) {
        this.word = word;
    }
}