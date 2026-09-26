package com.mikey.dictionary.repository;

import com.mikey.dictionary.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WordRepository extends JpaRepository<Word,Integer> {
    @Query("SELECT w FROM Word w LEFT JOIN FETCH w.meanings WHERE LOWER(w.word) = LOWER(:word)")
    Optional<Word> searchWord(@Param("word") String word);
}
