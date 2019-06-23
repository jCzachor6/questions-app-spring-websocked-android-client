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
    @Generated(hash = 1403800245)
    public Answer(Long id, @NotNull Long questionId, @NotNull String answer) {
        this.id = id;
        this.questionId = questionId;
        this.answer = answer;
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
}
