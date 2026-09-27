# Dictionary API

A simple RESTful dictionary API backend built with Spring Boot and MySQL-compatible databases.

The project is designed to provide dictionary data through a self-hosted API instead of relying on external dictionary services. It can be used as a backend service for vocabulary applications, language-learning tools, word games, or any service requiring vocabulary lookup.

> **Note:** This repository contains the backend source code and database schema only. No dictionary dataset is included. Users can import and use their own dataset in any language, provided they have the necessary rights to use that data.

---

## Features

* RESTful API for word searches
* Supports vocabulary details:
* Word
* Phonetic
* Audio URL
* Part of speech
* Definition
* Example
* Synonyms (comma-separated storage, returned as a JSON array)
* Antonyms (comma-separated storage, returned as a JSON array)


* Custom API key authentication for dictionary search requests
* Admin API protected by a secret key for API key management
* Activate and deactivate API keys
* Multi-stage Docker build support
* MySQL-compatible database support
* Spring Data JPA / Hibernate integration
* Environment-variable based configuration

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL / TiDB / MySQL-compatible database
* Docker
* Lombok
* Maven

---

## Project Structure

```text
com.mikey.dictionary
├── config
│   └── FilterConfig.java
├── controller
│   ├── AuthController.java
│   └── WordRestController.java
├── dto
│   ├── api
│   │   └── ApiKeyResponse.java
│   └── word
│       ├── WordMeaningResponse.java
│       └── WordSearchResponse.java
├── entity
│   ├── ApiKey.java
│   ├── Word.java
│   └── WordMeaning.java
├── exception
│   ├── BadCredentialsException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── filter
│   └── AdminAuthFilter.java
├── repository
│   ├── ApiKeyRepository.java
│   └── WordRepository.java
├── service
│   ├── ApiService.java
│   ├── ApiServiceImpl.java
│   ├── WordService.java
│   └── WordServiceImpl.java
└── DictionaryApplication.java

```

---

## Database

The application uses three tables: `words`, `word_meanings`, and `api_keys`.

### Database Schema

```sql
DROP TABLE IF EXISTS word_meanings;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS api_keys;

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
    antonyms VARCHAR(500) NULL,
    CONSTRAINT fk_word FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);

CREATE TABLE api_keys (
    id INT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(64) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_word_meanings_word_id ON word_meanings(word_id);

```

### Data Relationship

A single word can have multiple meanings:

```text
words (1) ───< word_meanings (N)

```

Example representation:

```text
words
└── record
    ├── phonetic
    ├── audio_url
    │
    ├── meaning 1
    │   ├── part_of_speech: noun
    │   ├── definition
    │   ├── example
    │   ├── synonyms
    │   └── antonyms
    │
    └── meaning 2
        ├── part_of_speech: verb
        ├── definition
        ├── example
        ├── synonyms
        └── antonyms

```

---

## Dataset

This repository does not include a pre-populated dataset. The API is intentionally dataset-agnostic. You can import:

* English datasets
* Vietnamese datasets
* Japanese datasets
* Multilingual or other target language datasets
* Custom word lists

### Recommended Schema Mapping

| Dataset Field | Target Database Column | Notes |
| --- | --- | --- |
| Word | `words.word` | Unique string |
| Phonetic / IPA | `words.phonetic` | Optional |
| Audio URL | `words.audio_url` | Optional |
| Part of Speech | `word_meanings.part_of_speech` | Required |
| Definition | `word_meanings.definition` | Required |
| Example | `word_meanings.example` | Optional |
| Synonyms | `word_meanings.synonyms` | Comma-separated (e.g., `fast, quick`) |
| Antonyms | `word_meanings.antonyms` | Comma-separated (e.g., `slow, sluggish`) |

The API splits comma-separated strings in `synonyms` and `antonyms` into clean JSON array lists in the response.

---

## Configuration

The application uses environment variables for sensitive settings.

```properties
spring.application.name=dictionary

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Hibernate / JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=${PORT:8080}

dictionary.admin.secret-key=${SECRET_KEY}

```

### Environment Variables

| Variable | Description | Example |
| --- | --- | --- |
| `DB_URL` | MySQL JDBC connection string | `jdbc:mysql://localhost:3306/dictionary_db?useSSL=false&serverTimezone=UTC` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | `your_db_password` |
| `SECRET_KEY` | Admin authorization secret key | `your_admin_secret_key` |
| `PORT` | Application port (defaults to 8080) | `8080` |

---

## Running Locally

### Prerequisites

* Java 21 or higher
* Maven
* MySQL instance

### 1. Database Setup

Create your database:

```sql
CREATE DATABASE dictionary_db;

```

Run the SQL script located in the database section to create `words`, `word_meanings`, and `api_keys`.

### 2. Configure Environment Variables

Set the environment variables in your terminal:

```bash
export DB_URL="jdbc:mysql://localhost:3306/dictionary_db"
export DB_USERNAME="root"
export DB_PASSWORD="your_password"
export SECRET_KEY="your_admin_secret"
export PORT=8080

```

On Windows (Command Prompt):

```cmd
set DB_URL=jdbc:mysql://localhost:3306/dictionary_db
set DB_USERNAME=root
set DB_PASSWORD=your_password
set SECRET_KEY=your_admin_secret
set PORT=8080

```

### 3. Build and Run

Run via Maven wrapper:

```bash
./mvnw spring-boot:run

```

Or on Windows:

```cmd
mvnw.cmd spring-boot:run

```

Alternatively, build and run the JAR file:

```bash
./mvnw clean package -DskipTests
java -jar target/dictionary-0.0.1-SNAPSHOT.jar

```

---

## Run with Docker

You can also build and run the application using Docker.

### 1. Build the Docker Image

```bash
docker build -t dictionary-api .

```

### 2. Run the Container

```bash
docker run -d -p 8080:8080 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/dictionary_db" \
  -e DB_USERNAME="root" \
  -e DB_PASSWORD="your_password" \
  -e SECRET_KEY="your_admin_secret" \
  --name dictionary-service dictionary-api

```

> **Note:** If MySQL is running locally on the host machine, use `host.docker.internal` instead of `localhost` in the connection URL.

---

## API Documentation

### 1. Public Search Endpoint

Requires an active API key passed as a path variable.

#### Search for a Word

* **Method:** `GET`
* **URL:** `/api/{apiKey}/dictionary/search?word={word}`
* **Example Request:**
  `GET /api/3fa85f64-5717-4562-b3fc-2c963f66afa6/dictionary/search?word=hello`

**Response (`200 OK`):**

```json
{
  "word": "hello",
  "phonetic": "/həˈloʊ/",
  "audioUrl": "https://example.com/audio/hello.mp3",
  "meanings": [
    {
      "partOfSpeech": "noun",
      "synonyms": [
        "greeting",
        "salutation"
      ],
      "antonyms": [
        "goodbye"
      ],
      "definition": "An expression of greeting.",
      "example": "She gave a friendly hello."
    }
  ]
}

```

---

### 2. Admin Endpoints

All admin endpoints require the `Auth` header matching the configured `SECRET_KEY`.

#### Generate an API Key

* **Method:** `POST`
* **URL:** `/api/admin/keys/`
* **Header:** `Auth: <SECRET_KEY>`
* **Response (`200 OK`):**

```json
{
  "id": 1,
  "key": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "CreateAt": "2026-03-29T10:00:00",
  "isActive": true
}

```

#### List API Keys

* **Method:** `GET`
* **URL:** `/api/admin/keys`
* **Header:** `Auth: <SECRET_KEY>`
* **Response (`200 OK`):**

```json
[
  {
    "id": 1,
    "key": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "CreateAt": "2026-03-29T10:00:00",
    "isActive": true
  }
]

```

#### Deactivate an API Key

* **Method:** `DELETE`
* **URL:** `/api/admin/keys/{id}`
* **Header:** `Auth: <SECRET_KEY>`
* **Response (`204 No Content`)**

---

## Authentication Flow

```text
Client Request
      │
      ▼
Check API key validity
      │
      ├── Inactive or Not Found ──► 401 Unauthorized
      │
      └── Active Key
            │
            ▼
      Search database for word
            │
            ├── Word found ─────────► 200 OK with word payload
            │
            └── Word not found ─────► 404 Not Found

```

---

## Error Responses

The API returns structured error payloads managed via `@RestControllerAdvice`:

| Status Code | Meaning |
| --- | --- |
| `400 Bad Request` | Validation error or missing parameter |
| `401 Unauthorized` | Invalid or inactive API key / Missing admin Auth header |
| `404 Not Found` | Word not found in the database |

**Example Error Response:**

```json
{
  "status": 404,
  "message": "cant find word: testword",
  "timestamp": "2026-03-29T10:15:30.123456"
}

```

---

## License

This project was built quickly over a single afternoon to serve as the backend for my personal vocabulary app.

Feel free to use, modify, break, or adapt the code for your own learning projects, side hustles, or whatever you need—no formal license, restrictions, or strings attached.