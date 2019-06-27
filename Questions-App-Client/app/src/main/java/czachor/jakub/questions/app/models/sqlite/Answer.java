package czachor.jakub.questions.app.models.sqlite;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Answer {
    @Id
    private Long id;
    @NotNull
    private Long questionId;
    @NotNull
    private String answer;
    @NotNull
    private String correctAnswer;
    @NotNull
    private String state;

    @Generated(hash = 55578741)
    public Answer(Long id, @NotNull Long questionId, @NotNull String answer,
            @NotNull String correctAnswer, @NotNull String state) {
        this.id = id;
        this.questionId = questionId;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.state = state;
    }

    @Generated(hash = 53889439)
    public Answer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return this.answer;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
