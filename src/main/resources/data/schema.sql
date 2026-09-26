
drop table if exists word_meanings;
drop table if exists words;

CREATE TABLE words (
                       id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       word VARCHAR(100) NOT NULL UNIQUE,
                       phonetic VARCHAR(100) NULL,
                       audio_url VARCHAR(255) NULL
);

CREATE TABLE word_meanings (
                               id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               word_id INT NOT NULL,
                               part_of_speech VARCHAR(50) NOT NULL,
                               definition TEXT NOT NULL,
                               example TEXT NULL,
                               synonyms VARCHAR(500) NULL,
                               antonyms VARCHAR(500) NULL
);
CREATE INDEX idx_word_meanings_word_id ON word_meanings(word_id);