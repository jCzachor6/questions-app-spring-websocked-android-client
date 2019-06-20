package czachor.jakub.quesionsapp.server.models;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnswerHolder {
    private Question question;
    private QuestionState state;
    private Map<List<Long>, Long> answered;
}
