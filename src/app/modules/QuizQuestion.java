package app.modules;

// List um Listen auszugeben.
import java.util.List;

public class QuizQuestion {

    private String id;
    private String question;

    // image kann NULL sein, wenn kein Bild vorhanden.
    private String image;

    // Das Feld answers der Klasse QuizQuestion HAT eine Liste von Objekten der Klasse QuizAnswer (Komposition).
    // QuizQuestion kennt nur die QuizAnswer Objekte, nicht die JSON-Datei.
    // Die Liste (Container) wird später beim Laden der JSON-Datei mit QuizAnswer-Objekten befüllt.
    // Diese enthalten die Daten der JSON.
    // Realisiert mit List<> aus java.util.List.
    // final verhindert neue Zuweisungen zu answers. Die Liste der Fragen kann verändert werden, aber answers ist immer answers.
    private List<QuizAnswer> answers;
    private String explanation;

    public QuizQuestion (String id, String question, String image, List<QuizAnswer> answers, String explanation) {
        this.id = id;
        this.question = question;
        this.image = image;
        this.answers = answers;
        this.explanation = explanation;

    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getImage() {
        return image;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }
    public String getExplanation() {
        return explanation;
    }



}
