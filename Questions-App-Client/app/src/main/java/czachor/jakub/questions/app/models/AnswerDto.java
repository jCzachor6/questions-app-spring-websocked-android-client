package czachor.jakub.questions.app.models;

import java.io.Serializable;

import czachor.jakub.questions.app.models.sqlite.AnswerState;

public class AnswerDto implements Serializable {
    private Long id;
    private Long questionId;
    private String answer;
    private String correctAnswer;
    private AnswerState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public AnswerState getState() {
        return state;
    }

    public void setState(AnswerState state) {
        this.state = state;
    }
}
