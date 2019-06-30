package czachor.jakub.questions.app.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class QuestionDTO implements Serializable {
    private Long id;
    private String question;
    private List<String> answers;
    private List<Long> correctAnswers;
    private Long timeInSeconds;
    private QuestionState state;
    private Map<String, Long> answered;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<Long> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Long> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public QuestionState getState() {
        return state;
    }

    public void setState(QuestionState state) {
        this.state = state;
    }

    public Map<String, Long> getAnswered() {
        return answered;
    }

    public void setAnswered(Map<String, Long> answered) {
        this.answered = answered;
    }
}
