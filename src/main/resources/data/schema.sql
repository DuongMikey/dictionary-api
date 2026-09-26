
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
CREATE TABLE api_keys (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          api_key VARCHAR(64) NOT NULL UNIQUE,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_word_meanings_word_id ON word_meanings(word_id);