package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * In-memory CRUD service for Questions and Answers.
 */
public class QuestionService {
    private final List<Question> questions = new ArrayList<>();
    private final List<Answer> answers   = new ArrayList<>();

    // ===== Questions =====
    public Question createQuestion(String author, String text) {
        Question q = new Question(author, text);
        questions.add(q);
        return q;
    }

    public List<Question> listQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public Question getQuestion(String id) {
        return questions.stream()
            .filter(q -> q.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Question not found: " + id));
    }

    public Question updateQuestionText(String id, String newText) {
        Question q = getQuestion(id);
        q.setText(newText);
        return q;
    }

    public boolean deleteQuestion(String id) {
        // cascade delete answers for this question
        answers.removeIf(a -> a.getQuestionId().equals(id));
        return questions.removeIf(q -> q.getId().equals(id));
    }

    // ===== Answers =====
    public Answer addAnswer(String questionId, String author, String text) {
        // ensure the question exists
        getQuestion(questionId);
        Answer a = new Answer(questionId, author, text);
        answers.add(a);
        return a;
    }

    public List<Answer> listAnswers(String questionId) {
        // ensure the question exists
        getQuestion(questionId);
        return answers.stream()
            .filter(a -> a.getQuestionId().equals(questionId))
            .toList();
    }

    public Answer getAnswer(String questionId, String answerId) {
        // ensure the question exists
        getQuestion(questionId);
        return answers.stream()
            .filter(a -> a.getQuestionId().equals(questionId) && a.getId().equals(answerId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Answer not found: " + answerId));
    }

    public Answer updateAnswerText(String questionId, String answerId, String newText) {
        Answer a = getAnswer(questionId, answerId);
        a.setText(newText);
        return a;
    }

    public boolean deleteAnswer(String questionId, String answerId) {
        // ensure the question exists
        getQuestion(questionId);
        return answers.removeIf(a -> a.getQuestionId().equals(questionId) && a.getId().equals(answerId));
    }
}
