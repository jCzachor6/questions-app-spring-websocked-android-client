package czachor.jakub.quesionsapp.server.util;

import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.models.dto.QuestionDTO;
import czachor.jakub.quesionsapp.server.models.dto.QuestionLookupDTO;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static List<QuestionLookupDTO> map(List<AnswerHolder> questions) {
        return questions.stream().map(Mapper::mapLookup).collect(Collectors.toList());
    }

    public static QuestionDTO map(AnswerHolder answerHolder) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(answerHolder.getQuestion().getId());
        dto.setQuestion(answerHolder.getQuestion().getQuestion());
        dto.setAnswers(answerHolder.getQuestion().getAnswers());
        dto.setCorrectAnswers(answerHolder.getQuestion().getCorrectAnswers());
        dto.setTimeInSeconds(answerHolder.getQuestion().getTimeInSeconds());
        dto.setState(answerHolder.getState());
        dto.setAnswered(answerHolder.getAnswered());
        return dto;
    }

    public static QuestionLookupDTO mapLookup(AnswerHolder answerHolder) {
        QuestionLookupDTO dto = new QuestionLookupDTO();
        dto.setId(answerHolder.getQuestion().getId());
        dto.setQuestion(answerHolder.getQuestion().getQuestion());
        dto.setState(answerHolder.getState());
        return dto;
    }
}
