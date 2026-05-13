package app.modules;

import java.util.Collections;
import java.util.List;

public class QuizEngine {

    private int score = 0;

    private List<QuizQuestion> questions;

    // currentIndex zum Bestimmen von aktuelle/nächste Frage und zum Prüfen ob noch Fragen vorhanden sind
    private int currentIndex = 0;

    // Erzeuge Liste aus allen Quizfragen und mische diese und begrenze Größe des Fragenpools
    public QuizEngine(List<QuizQuestion> questions) {
        this.questions = questions;

        // mischen
        Collections.shuffle(this.questions);

        // begrenze Fragenpool wenn über 20
        if (this.questions.size() > 20) {
            // schaue dir mit subList die Elemente der Liste ab Index 20 bis höchster Index an und lösche diese aus der Liste.
            this.questions.subList(20, this.questions.size()).clear();
        }

    }

    // Aktuelle Frage erhalten
    public QuizQuestion getCurrentQuestion() {
        return questions.get(currentIndex);
    }

    // Antwort (Index) bewerten
    public boolean checkAnswer(int input) {

        // choice - 1 da Antwortmöglichkeit mit 1 beginnt anstatt wie index mit 0
        int answerIndex = input - 1;

        // Hole Antworten der aktuellen Frage und die ausgewählte Antwort
        QuizAnswer selectedAnswer = questions.get(currentIndex).getAnswers().get(answerIndex);

        // Gibt zurück ob richtig/falsch
        return selectedAnswer.isCorrect();
    }

    // Punktezahl erhöhen wenn Antwort richtig
    public void inputAnswer(boolean correct) {
        if (correct) {
            score++;
        }
    }

    // Fragenpool Index erhöhen
    public void nextQuestion() {
        currentIndex++;
    }

    // Prüfe ob offene Fragen vorhanden wenn currentIndex kleiner Fragenpool
    public boolean hasNextQuestion() {
        return currentIndex < questions.size();
    }

    // Punktezahl
    public int getScore() {
        return score;
    }


}
