// Main bleibt erhalten für Funktionstest / debugging mit der Konsole; App startet mit JavaFX in QuizView bzw. MainView wenn ausgelagert.

package app;

import app.core.*;
import app.io.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Loader erstellen.
        QuizLoader loader = new QuizLoader();

        // JSON mit loader aus QuizEngine einlesen.
        // Liste von Fragen zurückgeben lassen.
        QuizEngine engine = new QuizEngine(loader.load("questions/lf00.json"));

        Scanner scanner = new Scanner(System.in);

        // Solange eine nächste Frage möglich ist...
        while (engine.hasNextQuestion()) {

            // ...erhalte aktuelle Frage
            QuizQuestion question = engine.getCurrentQuestion();

            // Gib ID und Frage aus
            System.out.println(question.getId());
            System.out.println(question.getQuestion());

            // Bildabfrage wenn nicht null dann Ausgabe.
            if (question.getImage() != null) {
                System.out.println("[Bild: " + question.getImage() + "]");
            }

            // Alle Antwortmöglichkeiten nummeriert ausgeben
            int answerNumber = 1;
            for (QuizAnswer answer : question.getAnswers()) {
                System.out.println(answerNumber + ": " + answer.getText());
                answerNumber++;
            }

            // Antwortmöglichkeit eingeben
            System.out.println("Antwort: ");
            int input = scanner.nextInt();

            // Antwort prüfen
            boolean correct = engine.checkAnswer(input);

            // Punktezahl erhöhen wenn Antwort richtig
            engine.inputAnswer(correct);

            System.out.println();

            // Richtig oder Falsch
            if (correct) {
                System.out.println("Richtig!");
            } else {
                System.out.println("Falsch.");
            }

            System.out.println();

            // Erklärung ausgeben.
            System.out.println(question.getExplanation());
            System.out.println();

            System.out.println();

            // Springe zur nächsten Frage
            engine.nextQuestion();
        }

        // Ausgabe der Gesamtpunkte nach 20 Fragen
        System.out.println("Ergebnis: " + engine.getScore() + " von 20 Fragen richtig.");


    }
}