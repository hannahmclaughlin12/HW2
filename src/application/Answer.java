package application;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Answer {
    private final String id;
    private final String questionId;
    private String author;
    private String text;
    private final Instant createdAt;

    public Answer(String questionId, String author, String text) {
        if (questionId == null || questionId.isBlank())
            throw new IllegalArgumentException("questionId is required.");
        validateAuthor(author);
        validateText(text);
        this.id = UUID.randomUUID().toString();
        this.questionId = questionId;
        this.author = author.trim();
        this.text = text.trim();
        this.createdAt = Instant.now();
    }

    // validation
    public static void validateAuthor(String author) {
        if (author == null || author.trim().length() < 2 || author.trim().length() > 80) {
            throw new IllegalArgumentException("Author must be 2–80 characters.");
        }
    }
    public static void validateText(String text) {
        if (text == null || text.trim().length() < 2 || text.trim().length() > 2000) {
            throw new IllegalArgumentException("Answer text must be 2–2000 characters.");
        }
    }

    // getters/setters
    public String getId() { return id; }
    public String getQuestionId() { return questionId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { validateAuthor(author); this.author = author.trim(); }
    public String getText() { return text; }
    public void setText(String text) { validateText(text); this.text = text.trim(); }
    public Instant getCreatedAt() { return createdAt; }

    @Override public boolean equals(Object o) {
        return (o instanceof Answer a) && Objects.equals(id, a.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() {
        return "Answer{id=%s, qid=%s, author=%s, text=%s}".formatted(id, questionId, author, text);
    }
}
