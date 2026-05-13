// JavaFX GUI; App wird hier aufgerufen nicht mehr in Main.
// Festlegung der verschiedenen UI-Zustände und deren jeweilige Layouts.
// JavaFX gewählt für modernere Designmöglichkeiten im Vergleich zu Swing und Android App Parität.
// !Layouts für Desktop App sollen, wenn möglich, Parität mit Android App aufweisen!

// TODO: Startmenü in MainView wie bei Android!

package app.ui;

import app.core.*;
import app.io.*;

// imports für javaFX GUI

// Platform steuert Dinge, die die globale JavaFX-Laufzeitumgebung betreffen; z.B. Starten und Beenden der JavaFX-Runtime.
// Kontrolliert Lebenszyklus
import javafx.application.Platform;

// Application ist Basisklasse für JavaFX-Apps; ermöglicht launch() und beschreibt start / stop Lebenszyklus.
// Regelt die Instanz der App.
import javafx.application.Application;

// Stage dient zum erzeugen des Fensters; vergleichbar mit Android Activity oder Swing JFrame; Stage hat Scene.
import javafx.stage.Stage;

// Scene legt den Inhaltsbereich des Fensters fest; enthält Layouts und Controls; Scene hat Nodes.
import javafx.scene.Scene;

// Layout Optionen wie BorderPane legt das Hauptlayout fest; top / bottom / left / right / center.
// VBox und Hbox
// Region und Priority
import javafx.scene.layout.*;

// Label ist für Textanzeigen; hier für Titel, Fragen, Erklärung.
import javafx.scene.control.Label;

// Button zum erstellen von (klickbaren) Buttons.
import javafx.scene.control.Button;

// Bildanzeige imports
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Benötigt für Padding
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// Text Format Optionen
import javafx.scene.text.TextAlignment;

// Hier benötigt für Dark Theme einbinden
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Klasse erbt von importierter Klasse Application
public class QuizView extends Application {

    // Klasse als Feld zur späteren Verwendung in Methoden.
    private QuizEngine engine;

    // Felder die über Zustände hinweg gebraucht werden.

    // Root-Layout für alle Panes
    private BorderPane rootLayout;

    // Startmenü
    private VBox startPane;

    // Quiz Container
    private VBox quizContainer;

    // Feste Anzeige der Frage (+ Bild) in der Antwort und Auswertungsphase einer Frage.
    private VBox questionPane;

    // Eigene Box für Text
    private VBox questionTextBox;

    // Eigene Box für Bild
    private VBox imageBox;

    // Wechselnde Anzeige zwischen Anworten und Auswertung einer Frage.
    private VBox interactionPane;

    // Button für "Bestätigen" (Antwort) oder "Nächste Frage" (Auswertung).
    private Button actionButton;

    // Ausgewählter Antwort-Button
    private Button selectedAnswerButton;

    // Alle Antwort Buttons der aktuellen Frage (zum gleichzeitigen abdunkeln bei Auswahl)
    private List<Button> answerButtons = new ArrayList<>();

    // Wechsel zwischen Antwortphase (false) und Auswertungsphase (true) einer Frage
    private boolean evaluationMode = false;

    // Statusbar-Labels Fragen x/y und Frage ID
    private Label questionCounterLabel;
    private Label questionIdLabel;

    // Konstante für Breite
    private static final double SET_WIDTH = 420;

    // @Override sagt dem Compiler „Diese Methode überschreibt eine Methode der Oberklasse“.
    // Die Methode start(Stage) von Application muss implementiert werden.
    // Kurz: Erkennt Fehler bzw. hilft bei deren Erkennung. Verwendung wird immer empfohlen.
    @Override

    // Stage = Fenster.
    public void start(Stage stage) {

        // Root-Layout erzeugen; wird für alle Screens benutzt.
        // BorderPane regelt wo Bereiche liegen.
        rootLayout = new BorderPane();

        // Status-Bar für Fragenstatus und ID
        HBox statusBar = new HBox();
        statusBar.setMinHeight(40);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setStyle(
            "-fx-padding: 10;"

        );

        // Links Frage x/y
        questionCounterLabel = new Label();
        questionCounterLabel.setStyle("-fx-font-size: 12px");

        // Rechts Frage ID
        questionIdLabel = new Label("v1");
        questionIdLabel.setStyle("-fx-font-size: 12px");

        // Spacer um rechtes Label nach ganz rechts zu schieben.
        Region spacerStatusBar = new Region();
        HBox.setHgrow(spacerStatusBar, Priority.ALWAYS);

        // Status-Bar mit Frage x/y, Spacer und Frage ID
        statusBar.getChildren().addAll(
                questionCounterLabel,
                spacerStatusBar,
                questionIdLabel
        );

        // Status-Bar oben einfügen
        rootLayout.setTop(statusBar);

        // Quiz Container erzeugen.
        quizContainer = new VBox();
        // Abstand des Quiz Containers zur Status-Bar oben, rechts, unten, links.
        quizContainer.setPadding(new Insets(10, 10, 10, 10));
        rootLayout.setCenter(quizContainer);

        // Question Pane erzeugen. Immer gleich groß mit oder ohne Bild; Großteil obere Hälfte.
        questionPane = new VBox();
        questionPane.setMinHeight(380);
        questionPane.setPrefHeight(380);
        questionPane.setAlignment(Pos.TOP_CENTER);
        questionPane.setSpacing(12);
        questionPane.setPadding(new Insets(6, 10, 6, 10));

        // Feste TextBox für mind. 2 Zeilen Platz bei Bild
        questionTextBox = new VBox();
        questionTextBox.setAlignment(Pos.CENTER);
        questionTextBox.setMinHeight(104);
        questionTextBox.setPrefHeight(104);
        questionTextBox.setPadding(new Insets(0, 0, 48, 0));

        // Feste Bild Box
        imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setMinHeight(230);
        imageBox.setPrefHeight(230);

        questionPane.getChildren().addAll(
                questionTextBox,
                imageBox
        );

        // Interaction Pane erzeugen. Für Antworten Buttons bzw. Auswertung; Immer gleich groß; Großteil untere Hälfte.
        interactionPane = new VBox(8);
        interactionPane.setMinHeight(280);
        interactionPane.setPrefHeight(280);
        interactionPane.setAlignment(Pos.TOP_CENTER);
        interactionPane.setPadding(new Insets(14, 0, 0, 0));

        // Action Button zum Navigieren. Unsichtbar bis Antwort gewählt
        actionButton = new Button("Bestätige Antwort");
        actionButton.setPrefWidth(220);
        actionButton.setStyle("-fx-font-size: 18px;");

        // Verstecken
        actionButton.setVisible(false);
        actionButton.setManaged(false);

        // Action Bar. Platz für Action-Button(s)
        HBox actionBar = new HBox();
        actionBar.setMinHeight(48);
        actionBar.setAlignment(Pos.CENTER);
        actionBar.setPadding(new Insets(0, 0, 26, 0));
        actionBar.getChildren().add(actionButton);

        // Quiz Container mit Question Pane, Interaction Pane und actionBar
        quizContainer.getChildren().addAll(
                questionPane,
                interactionPane,
                actionBar
        );



        actionButton.setOnAction(paneSwitch -> {

            // Antwortphase Frage
            if (!evaluationMode) {

                // Wenn nichts ausgewählt
                if (selectedAnswerButton == null) {

                    return;

                }

                // Antwort prüfen
                int index = interactionPane.getChildren().indexOf(selectedAnswerButton);
                boolean correct = engine.checkAnswer(index + 1);
                engine.inputAnswer(correct);

                // Auswertungsphase Frage
                showEvaluation(correct);
                evaluationMode = true;

                // Button Quiz auswerten, wenn letzte Frage; ansonsten Nächste Frage
                if (engine.isLastQuestion()) {
                    actionButton.setText("Quiz auswerten");
                } else {
                    actionButton.setText("Nächste Frage");
                }
                return;

            }

            // Übergang zur nächsten Frage oder Gesamtauswertung
            if (engine.isLastQuestion()) {
                showResult();

            } else {
                engine.nextQuestion();
                showCurrentQuestion();

            }

        });










        // Titeltext erzeugen
        Label titleLabel = new Label("FIQA");

        // Schriftgröße und dicke festlegen
        titleLabel.setStyle(
                "-fx-font-size: 48px;" +
                "-fx-font-weight: bold;"
        );

        // Start-Button erzeugen
        Button startButton = new Button("Start");

        // Breite und Schriftgröße
        startButton.setPrefWidth(220);
        startButton.setStyle("-fx-font-size: 18px;");

        startButton.setOnAction(onClick -> {

            // Quiz Engine laden
            QuizLoader loader = new QuizLoader();
            engine = new QuizEngine(loader.load("questions/lf09.json"));

            // Erste Frage anzeigen
            showCurrentQuestion();

            // Start-Pane ausblenden und Quiz-Pane anzeigen
            rootLayout.setCenter(quizContainer);

        });





        // Beenden-Button erzeugen
        Button exitButton = new Button("Beenden");

        // Breite und Schriftgröße
        exitButton.setPrefWidth(220);
        exitButton.setStyle("-fx-font-size: 18px;");

        // Beenden der App wenn Beenden-Button gedrückt.
        // setOnAction gibt an, was passieren soll wenn Button gedrückt wird.
        // Lambda-Ausdruck bei onclick (bei Klick) -> (führe folgenden Code aus) {...} (Codeblock der bei Klick ausgeführt wird).
        // normalerweise kurze Schreibweise für Ausdruck (e -> {}).
        exitButton.setOnAction(onClick -> {

            // Beenden der UI-Event-Schleife.
            // Ruft intern Application.stop() auf.
            // Alle Fenster (Stages) werden geschlossen und Java Prozess wird sauber beendet.
            Platform.exit();
        });

        // Start-Pane Container erzeugen VBox = vertikale Anordnung.
        // Regelt wie die Inhalte angezeigt werden.
        startPane = new VBox(8);

        // Alles sich im Container befindende zentrieren; horizontal als auch vertikal.
        startPane.setStyle("-fx-alignment: center;");

        // Pane Titeltext, Start-Button und Beenden-Button zuweisen
        startPane.getChildren().addAll(
                titleLabel,
                startButton,
                exitButton
        );
















        // Start-Pane aufrufen im gesamten "Center-Bereich" der BorderPane platzieren; zentriert nicht den Inhalt, dies erledigt die VBox.
        rootLayout.setCenter(startPane);

        // Scene aus Root-Layout erzeugen mit Breite und Höhe
        Scene scene = new Scene(rootLayout, 620, 820);

        // "Dark-Theme" androidDark.css einbinden
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/androidDark.css")).toExternalForm());

        // Stage Titel und Scene zuweisen und anzeigen
        stage.setTitle("FIQA");
        stage.setScene(scene);
        stage.show();

    }

    private void showCurrentQuestion() {



        // Question Pane Kinder zuweisen; Struktur sicherstellen.
        questionPane.getChildren().setAll(questionTextBox, imageBox);

        // Gib Liste aller aktuellen enthaltenen UI-Elemente zurück und entferne diese.
        questionTextBox.getChildren().clear();
        imageBox.getChildren().clear();
        interactionPane.getChildren().clear();

        // Fragebox und Bildbox dürfen niemals wachsen
        VBox.setVgrow(questionTextBox, Priority.NEVER);
        VBox.setVgrow(imageBox, Priority.NEVER);



        // Rücksetzen auf Antwortphase.
        selectedAnswerButton = null;
        evaluationMode = false;

        // Action Button verstecken und benennen
        actionButton.setVisible(false);
        actionButton.setManaged(false);
        actionButton.setText("Bestätige Antwort");

        // Antwort Buttons zurücksetzen
        answerButtons.clear();

        // Aktueller Fragen Index und Gesamtzahl Fragenpool für Statusbar Anzeige
        int currentIndex = engine.getCurrentIndex() + 1;
        int totalQuestions = engine.getQuestionCount();

        // Index und Gesamtzahl zusammensetzen Format z.B. Frage 01/20
        questionCounterLabel.setText("Frage " + twoDigit(currentIndex) + "/" + twoDigit(totalQuestions));

        // Fragen ID holen
        questionIdLabel.setText(engine.getCurrentQuestion().getId());

        // Aktuelle Frage holen.
        QuizQuestion question = engine.getCurrentQuestion();

        // Fragetext erzeugen.
        Label questionLabel = new Label(question.getQuestion());

        // Text automatisch der Fensterbreite anpassen.
        questionLabel.setWrapText(true);
        // Ausrichtung
        questionLabel.setTextAlignment(TextAlignment.CENTER);
        // Gewünschte Breite
        questionLabel.setPrefWidth(SET_WIDTH);
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        // Bild anzeigen, falls vorhanden.
        if (question.getImage() != null) {

            questionTextBox.getChildren().add(questionLabel);

            // Bildpfad holen "questions/" + question.getImage(),
            // laden .getResourceAsStream,
            // ein JavaFX-Image erstellen new Image(getClass().getClassLoader()) und
            // die UI Bilddarstellung erzeugen new ImageView().
            ImageView imageView = new ImageView(
                    new Image(getClass().getClassLoader().getResourceAsStream("questions/" + question.getImage()))
            );

            // Seitenverhältnis beibehalten.
            imageView.setPreserveRatio(true);
            // Skaliere Inhalt
            imageView.setFitWidth(SET_WIDTH);

            imageBox.getChildren().add(imageView);

        } else {

            // Fragenbox und Bildbox "zusammenführen" für zentrierte Fragenanzeige

            // Fragen Pane ersetzt alle Kinder (setAll) mit nur Fragenbox
            // Bildbox fällt raus
            questionPane.getChildren().setAll(questionTextBox);

            // Fragentext wird der Fragenbox hinzugefügt; Fragentext ist Kind von Fragenbox nicht FragenPane direkt.
            questionTextBox.getChildren().add(questionLabel);

            // FragenBox darf wachsen und ganzen Platz von Fragenbox + Bildbox einnehmen
            VBox.setVgrow(questionTextBox, Priority.ALWAYS);

        }

        if (question.getAnswers().size() > 2) {
            Collections.shuffle(question.getAnswers());
        }

        // Antwort-Buttons dynamisch (2-4) erzeugen
        // Für Antworten aus Antwortenpool
        for (QuizAnswer answer : question.getAnswers()) {

            // Erzeuge Button pro Antwort
            Button answerButton = new Button(answer.getText());
            answerButton.setPrefWidth(SET_WIDTH);
            answerButton.setStyle("-fx-font-size: 16px;");

            answerButtons.add(answerButton);

            answerButton.setOnAction(onClick -> {

                // Methode für Button Transparenz aufrufen
                selectedAnswerButton(answerButton);

                // Action Button anzeigen
                actionButton.setVisible(true);
                actionButton.setManaged(true);
            });

            interactionPane.getChildren().add(answerButton);

        }

        // Alle Buttons abdunkeln damit keiner vorausgewählt erscheint
        for (Button button : answerButtons) {
            button.setOpacity(1.0);

        }

    }

    // Button Transparenz festlegen
    private void selectedAnswerButton(Button selectedButton) {

        // Alle Buttons abdunkeln
        for (Button button : answerButtons) {
            button.setOpacity(0.5);

        }

        // Ausgewählten Button hervorheben
        selectedButton.setOpacity(1.0);

        // Referenz merken
        selectedAnswerButton = selectedButton;

    }


    // Auswertung mit Erklärung im interactionPane anzeigen
    private void showEvaluation(boolean correct) {

        interactionPane.getChildren().clear();

        // Bewertung
        Label resultLabel = new Label(
                correct ? "+ RICHTIG +" : "- FALSCH -"
        );
        // Bewertung Style
        resultLabel.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        resultLabel.setAlignment(Pos.TOP_CENTER);
        resultLabel.setTextAlignment(TextAlignment.CENTER);



        // Wenn Antwort falsch hole richtige Antwort
        Label correctAnswerLabel = new Label(
                correct ? "\n" : "Richtige Antwort:\n" + engine.getCurrentQuestion().getCorrectAnswerText()
        );


        correctAnswerLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold"
        );

        // Zeilenumbruch wenn Text zu lang
        correctAnswerLabel.setWrapText(true);

        // Text zentrieren
        correctAnswerLabel.setAlignment(Pos.TOP_CENTER);
        correctAnswerLabel.setTextAlignment(TextAlignment.CENTER);

        // Erklärung
        Label explanation = new Label(
                "Erklärung:\n" + engine.getCurrentQuestion().getExplanation()
        );

        explanation.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold"
        );

        explanation.setWrapText(true);
        explanation.setAlignment(Pos.TOP_CENTER);
        explanation.setTextAlignment(TextAlignment.CENTER);

        // Spacer zwischen Textelementen.
        Region spacerResult = new Region();
        VBox.setVgrow(spacerResult, Priority.ALWAYS);

        Region spacerCorrectAnswer = new Region();
        VBox.setVgrow(spacerCorrectAnswer, Priority.ALWAYS);

        Region spacerExplanation = new Region();
        VBox.setVgrow(spacerExplanation, Priority.ALWAYS);

        interactionPane.getChildren().addAll(resultLabel, spacerResult, correctAnswerLabel, spacerCorrectAnswer, explanation, spacerExplanation);

    }

    // Gesamtergebnis
    private void showResult() {

        // Action Button ausblenden
        actionButton.setVisible(false);
        actionButton.setManaged(false);

        // Alte Quiz-Inhalte entfernen
        questionPane.getChildren().clear();
        interactionPane.getChildren().clear();

        // Gesamtergebnis Text
        Label resultLabel = new Label(
                "Ergebnis:\n" + twoDigit(engine.getScore()) + "/" + twoDigit(engine.getQuestionCount())
        );

        // Text zentrieren
        resultLabel.setTextAlignment(TextAlignment.CENTER);
        resultLabel.setAlignment(Pos.CENTER);
        resultLabel.setPrefWidth(SET_WIDTH);
        // Groß und Fett
        resultLabel.setStyle(
                "-fx-font-size: 48px;" +
                "-fx-font-weight: bold;"
        );

        // Neue Runde Button
        Button newRoundButton = new Button("Neue Runde");
        newRoundButton.setPrefWidth(220);
        newRoundButton.setStyle("-fx-font-size: 18px;");

        newRoundButton.setOnAction(onClick -> {

            // Neue Runde mit neuer Engine
            QuizLoader loader = new QuizLoader();
            engine = new QuizEngine(loader.load("questions/lf09.json"));

            showCurrentQuestion();

            rootLayout.setCenter(quizContainer);

        });

        // Hauptmenü Button
        Button mainMenuButton = new Button("Hauptmenü");
        mainMenuButton.setPrefWidth(220);
        mainMenuButton.setStyle("-fx-font-size: 18px;");

        // Bei Klick Quiz Zustand zurücksetzen und ins Hauptmenü
        mainMenuButton.setOnAction(onClick -> {

            // Quiz Zustand zurücksetzen
            engine = null;
            evaluationMode = false;
            selectedAnswerButton = null;

            // Zurück ins Hauptmenü
            rootLayout.setCenter(startPane);

        });

        // Ergebnis Pane
        VBox resultPane = new VBox(8);
        resultPane.setAlignment(Pos.CENTER);
        resultPane.getChildren().addAll(
                resultLabel,
                newRoundButton,
                mainMenuButton
        );

        // Zentrieren
        rootLayout.setCenter(resultPane);

    }

    // Hilfmethode um auf 2 Zeichen zu erhöhen, sprich z.B Frage 01/20 nicht 1/20
    private String twoDigit(int value) {
        return String.format("%02d", value);
    }










    // App starten "launch(args)" ruft intern "start(Start stage)" auf. main Steht konventionell ganz unten.
    public static void main(String[] args) {
        launch(args);
    }




}
