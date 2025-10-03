package application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Stand-alone console demo for the Q&A system.
 * Run this class directly (has a main()).
 */
public class QADemoConsole {

    private final QuestionService svc = new QuestionService();
    private final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        new QADemoConsole().run();
    }

    private void run() {
        seed(); // optional demo data; delete if you want a blank start
        println("\n=== Q&A Console Demo ===");
        String choice;
        do {
            choice = mainMenu();
            switch (choice) {
                case "1" -> listQuestions();
                case "2" -> addQuestion();
                case "3" -> updateQuestion();
                case "4" -> deleteQuestion();
                case "5" -> manageAnswers();
                case "0" -> println("Goodbye!");
                default -> println("Unknown option.");
            }
        } while (!"0".equals(choice));
    }

    private String mainMenu() {
        println("\nMain Menu");
        println("1) List questions");
        println("2) Add question");
        println("3) Update question text");
        println("4) Delete question");
        println("5) Manage answers for a question");
        println("0) Exit");
        print("Select: ");
        return in.nextLine().trim();
    }

    // ===== Questions =====

    private void listQuestions() {
        List<Question> qs = svc.listQuestions();
        if (qs.isEmpty()) { println("(no questions yet)"); return; }
        println("\nQuestions:");
        int i = 1;
        for (Question q : qs) {
            println("%d) id=%s | by=%s | \"%s\"".formatted(i++, q.getId(), q.getAuthor(), q.getText()));
        }
    }

    private void addQuestion() {
        try {
            print("Author: ");
            String a = in.nextLine();
            print("Question text: ");
            String t = in.nextLine();
            Question q = svc.createQuestion(a, t);
            println("Added question id=%s".formatted(q.getId()));
        } catch (IllegalArgumentException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    private void updateQuestion() {
        listQuestions();
        print("Enter question id to update: ");
        String id = in.nextLine().trim();
        print("New text: ");
        String newText = in.nextLine();
        try {
            svc.updateQuestionText(id, newText);
            println("Updated.");
        } catch (IllegalArgumentException | NoSuchElementException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    private void deleteQuestion() {
        listQuestions();
        print("Enter question id to delete: ");
        String id = in.nextLine().trim();
        boolean ok = svc.deleteQuestion(id);
        println(ok ? "Deleted (and any answers for it)." : "No question with that id.");
    }

    // ===== Answers for a selected question =====

    private void manageAnswers() {
        listQuestions();
        print("Enter question id to manage answers: ");
        String qid = in.nextLine().trim();
        try {
            svc.getQuestion(qid); // ensure exists
        } catch (NoSuchElementException e) {
            println("ERROR: " + e.getMessage());
            return;
        }

        String choice;
        do {
            println("\nAnswers menu for question: " + qid);
            println("1) List answers");
            println("2) Add answer");
            println("3) Update answer text");
            println("4) Delete answer");
            println("0) Back");
            print("Select: ");
            choice = in.nextLine().trim();

            switch (choice) {
                case "1" -> listAnswers(qid);
                case "2" -> addAnswer(qid);
                case "3" -> updateAnswer(qid);
                case "4" -> deleteAnswer(qid);
                case "0" -> { /* back */ }
                default -> println("Unknown option.");
            }
        } while (!"0".equals(choice));
    }

    private void listAnswers(String qid) {
        try {
            var as = svc.listAnswers(qid);
            if (as.isEmpty()) { println("(no answers yet)"); return; }
            println("\nAnswers:");
            int i = 1;
            for (var a : as) {
                println("%d) id=%s | by=%s | \"%s\""
                        .formatted(i++, a.getId(), a.getAuthor(), a.getText()));
            }
        } catch (NoSuchElementException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    private void addAnswer(String qid) {
        try {
            print("Author: ");
            String a = in.nextLine();
            print("Answer text: ");
            String t = in.nextLine();
            var ans = svc.addAnswer(qid, a, t);
            println("Added answer id=%s".formatted(ans.getId()));
        } catch (IllegalArgumentException | NoSuchElementException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    private void updateAnswer(String qid) {
        listAnswers(qid);
        print("Enter answer id to update: ");
        String aid = in.nextLine().trim();
        print("New text: ");
        String newText = in.nextLine();
        try {
            svc.updateAnswerText(qid, aid, newText);
            println("Updated.");
        } catch (IllegalArgumentException | NoSuchElementException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    private void deleteAnswer(String qid) {
        listAnswers(qid);
        print("Enter answer id to delete: ");
        String aid = in.nextLine().trim();
        try {
            boolean ok = svc.deleteAnswer(qid, aid);
            println(ok ? "Deleted." : "No answer with that id.");
        } catch (NoSuchElementException ex) {
            println("ERROR: " + ex.getMessage());
        }
    }

    // ===== optional demo data =====
    private void seed() {
        var q1 = svc.createQuestion("Sam", "How do I center a div in CSS?");
        var q2 = svc.createQuestion("Ava", "What is the difference between == and equals in Java?");
        svc.addAnswer(q1.getId(), "Riley", "Use flexbox: display:flex; justify-content:center; align-items:center;");
        svc.addAnswer(q1.getId(), "Kai", "For simple inline elements, text-align:center on the container.");
        svc.addAnswer(q2.getId(), "Mina", "== compares references; equals compares content (when overridden).");
    }

    // tiny IO helpers
    private void println(String s) { System.out.println(s); }
    private void print(String s) { System.out.print(s); }
}
