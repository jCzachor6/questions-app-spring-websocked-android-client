package czachor.jakub.questions.app.utils;

import czachor.jakub.questions.app.models.AnswerDto;
import czachor.jakub.questions.app.models.sqlite.Answer;
import czachor.jakub.questions.app.models.sqlite.AnswerState;

public class Mapper {
    public static AnswerDto map(Answer answer) {
        AnswerDto dto = new AnswerDto();
        if (answer != null) {
            dto.setAnswer(answer.getAnswer());
            dto.setId(answer.getId());
            dto.setQuestionId(answer.getQuestionId());
            dto.setCorrectAnswer(answer.getCorrectAnswer());
            dto.setState(AnswerState.fromName(answer.getState()));
        }
        return dto;
    }
}
