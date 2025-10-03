package application;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Question {
    private final String id;
    private String author;
    private String text;
    private final Instant createdAt;

    public Question(String author, String text) {
        validateAuthor(author);
        validateText(text);
        this.id = UUID.randomUUID().toString();
        this.author = author.trim();
        this.text = text.trim();
        this.createdAt = Instant.now();
    }

    // === validation ===
    public static void validateAuthor(String author) {
        if (author == null || author.trim().length() < 2 || author.trim().length() > 80) {
            throw new IllegalArgumentException("Author must be 2–80 characters.");
        }
    }
    public static void validateText(String text) {
        if (text == null || text.trim().length() < 5 || text.trim().length() > 1000) {
            throw new IllegalArgumentException("Question text must be 5–1000 characters.");
        }
    }

    // === getters/setters (with validation) ===
    public String getId() { return id; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { validateAuthor(author); this.author = author.trim(); }
    public String getText() { return text; }
    public void setText(String text) { validateText(text); this.text = text.trim(); }
    public Instant getCreatedAt() { return createdAt; }

    @Override public boolean equals(Object o) {
        return (o instanceof Question q) && Objects.equals(id, q.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() {
        return "Question{id=%s, author=%s, text=%s}".formatted(id, author, text);
    }
}
