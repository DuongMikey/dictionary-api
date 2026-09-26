package com.mikey.dictionary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "word",nullable = false, length = 100, unique = true)
    private String word;

    @Column(name = "phonetic", length = 100)
    private String phonetic;

    @Column(name = "audio_url")
    private String audioUrl;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WordMeaning> meanings;

    public Word(String word, String phonetic, String audioUrl) {
        this.word = word;
        this.phonetic = phonetic;
        this.audioUrl = audioUrl;
    }

    public void addMeaning(WordMeaning meaning){
        if (meanings == null){
            meanings = new ArrayList<>();
        }
        meanings.add(meaning);
        meaning.addWord(this);
    }
}

