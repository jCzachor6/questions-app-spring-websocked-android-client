package czachor.jakub.questions.app.utils;

import czachor.jakub.questions.app.models.AnswerDto;
import czachor.jakub.questions.app.models.sqlite.Answer;

public class Mapper {
    public static AnswerDto map(Answer answer) {
        AnswerDto dto = new AnswerDto();
        if(answer != null) {
            dto.setAnswer(answer.getAnswer());
            dto.setId(answer.getId());
            dto.setQuestionId(answer.getQuestionId());
        }
        return dto;
    }
}
